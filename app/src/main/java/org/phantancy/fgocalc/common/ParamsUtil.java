package org.phantancy.fgocalc.common;

import androidx.collection.SimpleArrayMap;

import org.phantancy.fgocalc.entity.ServantEntity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数处理工具
 */
public class ParamsUtil {
    private static DecimalFormat calcResFormatter = new DecimalFormat("#");

    public static String dmgResFormat(double x) {
        return calcResFormatter.format(x);
    }

    private static DecimalFormat npcFormatter = new DecimalFormat("0.00%");

    public static String npGenResFormat(double x) {
        return npcFormatter.format(x);
    }

    /**
     * 伤害计算
     * double atk,
     * double cardDmgMultiplier,
     * double postitionMod,
     * double effectiveBuff,
     * double firstCardMod,
     * double classAtkMod,
     * double affinityMod,
     * double attributeMod,
     * double randomMod,
     * double atkBuff,
     * double defBuff,
     * double specialBuff,
     * double specialDefBuff,
     * double criticalBuff,
     * double criticalMod,
     * double exDmgBuff,
     * double selfDmgBuff,
     * double selfDmgDefBuff,
     * double busterChainMod
     * double npDmgMultiplier,
     * double npPowerBuff,
     * double npSpecialBuff,
     * double selfDmgDefBuff
     */

    // 卡牌倍率 cardDmgMultiplier
    public static Map<String, Double> cardDmgMultiplierMap = new HashMap<String, Double>() {{
        put(Constant.CARD_QUICK, 0.8);
        put(Constant.CARD_ARTS, 1.0);
        put(Constant.CARD_BUSTER, 1.5);
        put(Constant.CARD_EX, 1.0);
        put(Constant.NP_QUICK, 0.8);
        put(Constant.NP_ARTS, 1.0);
        put(Constant.NP_BUSTER, 1.5);
    }};

    //判断卡牌倍率
    public static double getCardDmgMultiplier(String cardType) {
        return cardDmgMultiplierMap.get(cardType);
    }

    // double postitionMod,
    public static Map<Integer, Double> dmgPositionModMap = new HashMap<Integer, Double>() {{
        put(1, 1.0);
        put(2, 1.2);
        put(3, 1.4);
        put(4, 1.0);
    }};

    //位置补正
    public static double getDmgPositionMod(int position) {
        return dmgPositionModMap.get(position);
    }

    // double effectiveBuff,
    //判断有效魔放
    public static double getEffectiveBuff(String cardColor, double quick, double arts, double buster) {
        String x = getCardColor(cardColor);
        switch (x) {
            case Constant.COLOR_QUICK:
                return quick;
            case Constant.COLOR_ARTS:
                return arts;
            case Constant.COLOR_BUSTER:
                return buster;
            default:
                return quick;
        }
    }

    // double firstCardMod,
    //获取首卡补正
    public static double getDmgFirstCardMod(String firstcardType) {
        return isBusterColor(firstcardType) ? 0.5 : 0;
    }

    //获取职阶系数
    //职阶系数
    // double classAtkMod,
    public static Map<String, Double> classAtkModMap = new HashMap<String, Double>() {{
        put("saber", 1.0);
        put("archer", 0.95);
        put("lancer", 1.05);
        put("rider", 1.0);
        put("caster", 0.9);
        put("assassin", 0.9);
        put("berserker", 1.1);
        put("ruler", 1.1);
        put("shielder", 1.0);
        put("alterego", 1.0);
        put("avenger", 1.1);
        put("beast", 1.0);
        put("mooncancer", 1.0);
        put("foreigner", 1.0);
    }};

    public static double getClassAtkMod(String svtClass) {
        svtClass = svtClass.toLowerCase();
        return classAtkModMap.get(svtClass);
    }

    //职阶相性
//    public static double mergeAffinityMod(String affinity) {
//        return ConditionData.affinityMap.get(affinity);
//    }

    //阵营相性
//    public static double mergeAttributeMod(String attribue) {
//        return ConditionData.attributeMap.get(attribue);
//    }

    // double atkBuff,
    // double defBuff,
    // double specialBuff,
    // double specialDefBuff,
    // double selfDmgBuff,
    // double selfDmgDefBuff,
    // double npPowerBuff
    public static double getBuffDebuff(double buff, double debuff) {
        return buff - debuff;
    }

    // double criticalMod,
    public static double getDmgCriticalMod(boolean isCritical) {
        return isCritical ? 2.0 : 1.0;
    }

    // double criticalBuff,
    // double starRate,
    public static double getCriticalBuff(boolean isCritical, String cardType, double buff, double debuff, double quick, double arts, double buster) {
        if (isCritical && !isNp(cardType)) {
            switch (getCardColor(cardType)) {
                case Constant.CARD_QUICK:
                    return getBuffDebuff(buff + quick, debuff);
                case Constant.CARD_ARTS:
                    return getBuffDebuff(buff + arts, debuff);
                case Constant.CARD_BUSTER:
                    return getBuffDebuff(buff + buster, debuff);
                default:
                    return getBuffDebuff(buff, debuff);
            }
        }
        return 0;
    }

    // double exDmgBuff,
    public static double getExDmgBouns(String cardType, boolean isSameColor) {
        if (isEx(cardType)) {
            return isSameColor ? 3.5 : 2.0;
        }
        return 1.0;
    }

    // double busterChainMod 判断3张卡是否是红卡，ex卡，宝具卡不计算此项
    public static double mergeBusterChainMod(String cardType, boolean isBusterChain) {
        return isBuster(cardType) && isBusterChain ? 0.2 : 0;
    }

    // double npDmgMultiplier,
    public static double mergeNpDmgMultiplier(double base, double extra) {
        return base + extra;
    }


    // double npSpecialBuff,


    /**
     * np获取计算
     * double na,
     * double hits,
     * double cardNpMultiplier,
     * double positionMod,
     * double effectiveBuff,
     * double firstCardMod,
     * double npBuff,
     * double criticalMod,
     * double overkillMod,
     * double randomMod
     */

    //np获取率
    public static double getNa(String cardType, double q, double a, double b, double ex, double np) {
        if (isQuick(cardType)) {
            return q;
        }
        if (isArts(cardType)) {
            return a;
        }
        if (isBuster(cardType)) {
            return b;
        }
        if (isEx(cardType)) {
            return ex;
        }
        if (isNp(cardType)) {
            return np;
        }
        return q;
    }

    //打击次数
    public static int getHits(String cardType, int q, int a, int b, int ex, int np) {
        if (isQuick(cardType)) {
            return q;
        }
        if (isArts(cardType)) {
            return a;
        }
        if (isBuster(cardType)) {
            return b;
        }
        if (isEx(cardType)) {
            return ex;
        }
        if (isNp(cardType)) {
            return np;
        }
        return q;
    }

    //卡牌np率
    public static Map<String, Double> cardNpMultiplierMap = new HashMap<>();

    static {
        cardNpMultiplierMap.put(Constant.CARD_QUICK, 1.0);
        cardNpMultiplierMap.put(Constant.CARD_ARTS, 3.0);
        cardNpMultiplierMap.put(Constant.CARD_BUSTER, 0.0);
        cardNpMultiplierMap.put(Constant.CARD_EX, 1.0);
    }

    public static double getCardNpMultiplier(String cardType) {
        String color = getCardColor(cardType);
        return cardNpMultiplierMap.get(color);
    }

    //np位置补正
    public static Map<Integer, Double> npPositionModMap = new HashMap<>();

    static {
        npPositionModMap.put(1, 1.0);
        npPositionModMap.put(2, 1.5);
        npPositionModMap.put(3, 2.0);
        npPositionModMap.put(4, 1.0);
    }

    public static double getNpPositionMod(int position) {
        return npPositionModMap.get(position);
    }

    //np首卡染色
    public static double getNpFirstCardMod(String firstcardType) {
        return isArtsColor(firstcardType) ? 1.0 : 0;
    }

    //暴击np补正
    public static double getNpCriticalMod(boolean isCritical, String cardType) {
        return (!isNp(cardType) && isCritical) ? 2.0 : 1.0;
    }

    //overkill np补正
    public static double getNpOverkillMod(boolean isOverkill) {
        return isOverkill ? 1.5 : 1.0;
    }
    //double randomMod

    /**
     * 暴击星获取计算
     * double starRate,
     * double cardStarMultiplier,
     * double effectiveBuff,
     * double firstCardMod,
     * double starRateBuff,
     * double criticalMod,
     * double enemyStarBuff,
     * double randomMod,
     * double overkillMultiplier,
     * double overkillAdd
     * double cardStarRate,
     * double overkillAdd,
     * double enemyAmount
     */


    // 卡牌补正Quick
    static Map<Integer, Double> quickStarMultiplierMap = new HashMap<Integer, Double>();
    static {
        quickStarMultiplierMap.put(1, 0.8);
        quickStarMultiplierMap.put(2, 1.3);
        quickStarMultiplierMap.put(3, 1.8);
    }
    // 卡牌补正Arts
    static Map<Integer, Double> artsStarMultiplierMap = new HashMap<Integer, Double>();
    static {
        artsStarMultiplierMap.put(1, 0d);
        artsStarMultiplierMap.put(2, 0d);
        artsStarMultiplierMap.put(3, 0d);
    }
    // 卡牌补正Buster
    static Map<Integer, Double> busterStarMultiplierMap = new HashMap<Integer, Double>();
    static {
        busterStarMultiplierMap.put(1, 0.1);
        busterStarMultiplierMap.put(2, 0.15);
        busterStarMultiplierMap.put(3, 0.2);
    }

    //卡牌补正
    public static double getCardStarMultiplier(String cardType, int position) {
        //判断卡色
        if (isQuickColor(cardType)) {
            return quickStarMultiplierMap.get(position);
        }
        if (isArtsColor(cardType)) {
            return artsStarMultiplierMap.get(position);
        }
        if (isBuster(cardType)) {
            return busterStarMultiplierMap.get(position);
        }
        if (isEx(cardType)) {
            return 1d;
        }
        return 1d;
    }

    //打星首卡补正
    public static double getStarFirstCardMod(String firstCardType) {
        return isQuickColor(firstCardType) ? 0.2 : 0d;
    }

    //暴击打星补正
    public static double getStarCtriticalMod(boolean isCritical) {
        return isCritical ? 0.2 : 0;
    }

    //打星overkill加算
    public static double getOverkillAdd(boolean isOverkill) {
        return isOverkill ? 0.3 : 0;
    }

    private static Map<String,Double> cardStarRateMap = new HashMap<>();
    static {
        cardStarRateMap.put(Constant.NP_QUICK,0.8);
        cardStarRateMap.put(Constant.NP_ARTS,0.0);
        cardStarRateMap.put(Constant.NP_BUSTER,0.1);
    }
    //卡牌星星发生率
    public static double getCardStarRate(String cardType) {
        return cardStarRateMap.get(cardType);
    }

    //是否overkill
    public static boolean isOverkill(int position, boolean isOverkill1, boolean isOverkill2,
                                     boolean isOverkill3, boolean isOverkill4) {
        SimpleArrayMap<Integer, Boolean> map = new SimpleArrayMap<>();
        map.put(1, isOverkill1);
        map.put(2, isOverkill2);
        map.put(3, isOverkill3);
        map.put(4, isOverkill4);
        return map.get(position);
    }

    //是否暴击
    public static boolean isCritical(int position, boolean isCritical1, boolean isCritical2, boolean isCritical3) {
        SimpleArrayMap<Integer, Boolean> map = new SimpleArrayMap<>();
        map.put(1, isCritical1);
        map.put(2, isCritical2);
        map.put(3, isCritical3);
        map.put(4, false);
        return map.get(position);
    }

    //判断宝具卡
    public static boolean isNp(String cardType) {
        if (cardType.contains("np")) {
            return true;
        }
        return false;
    }

    //判断ex
    public static boolean isEx(String cardType) {
        if (cardType.equals(Constant.CARD_EX)) {
            return true;
        }
        return false;
    }

    //判断Buster卡
    public static boolean isBuster(String cardType) {
        if (cardType.equals(Constant.CARD_BUSTER)) {
            return true;
        }
        return false;
    }

    //判断arts卡
    public static boolean isArts(String cardType) {
        if (cardType.equals(Constant.CARD_ARTS)) {
            return true;
        }
        return false;
    }

    //判断quick卡
    public static boolean isQuick(String cardType) {
        if (cardType.equals(Constant.CARD_QUICK)) {
            return true;
        }
        return false;
    }

    //判断卡色
    static Map<String, String> cardColorMap = new HashMap<String, String>();

    static {
        {
            cardColorMap.put(Constant.CARD_QUICK, Constant.COLOR_QUICK);
            cardColorMap.put(Constant.CARD_ARTS, Constant.COLOR_ARTS);
            cardColorMap.put(Constant.CARD_BUSTER, Constant.COLOR_BUSTER);
            cardColorMap.put(Constant.NP_QUICK, Constant.COLOR_QUICK);
            cardColorMap.put(Constant.NP_ARTS, Constant.COLOR_ARTS);
            cardColorMap.put(Constant.NP_BUSTER, Constant.COLOR_BUSTER);
            cardColorMap.put(Constant.CARD_EX, Constant.CARD_EX);
        }
    }

    public static String getCardColor(String cardType) {
        return cardColorMap.get(cardType);
    }

    //判断同色
    public static boolean isCardsSameColor(String cardType1, String cardType2, String cardType3) {
        String x = getCardColor(cardType1);
        String y = getCardColor(cardType2);
        String z = getCardColor(cardType3);
        if (x.equals(y) && y.equals(z)) {
            return true;
        }
        return false;
    }

    //判断红链
    public static boolean isCardsBusterChain(String cardType1, String cardType2, String cardType3) {
        if (isCardsSameColor(cardType1, cardType2, cardType3) && isBusterColor(cardType1)) {
            return true;
        }
        return false;
    }

    //判断宝具卡位置
    public static int getNpPosition(String cardType1, String cardType2, String cardType3) {
        String[] cards = {cardType1, cardType2, cardType3};
        for (int i = 0; i < cards.length; i++) {
            if (isNp(cards[i])) {
                return i + 1;
            }
        }
        return -1;
    }

    //判断是否是绿色卡
    public static boolean isQuickColor(String cardType) {
        if (getCardColor(cardType).equals(Constant.COLOR_QUICK)) {
            return true;
        }
        return false;
    }

    //判断是否是蓝色卡
    public static boolean isArtsColor(String cardType) {
        if (getCardColor(cardType).equals(Constant.COLOR_ARTS)) {
            return true;
        }
        return false;
    }

    //判断是否是红色卡
    public static boolean isBusterColor(String cardType) {
        if (getCardColor(cardType).equals(Constant.COLOR_BUSTER)) {
            return true;
        }
        return false;
    }

}
