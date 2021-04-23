package org.phantancy.fgocalc.common;

import org.phantancy.fgocalc.data.ConditionData;

import java.util.HashMap;
import java.util.Map;

/**
 * 合并参数
 * 提供公式需要参数
 */
public class ParamsMerger {
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

    // double atk,
    public static double mergeAtk(
            double svtAtk,
            double fouAtk,
            double essenceAtk
    ) {
        return svtAtk + fouAtk + essenceAtk;
    }

    // 卡牌倍率 cardDmgMultiplier
    public static Map<String, Double> cardDmgMultiplierMap = new HashMap<String, Double>() {{
        put(Constant.CARD_QUICK, 0.8);
        put(Constant.CARD_ARTS, 1.0);
        put(Constant.CARD_BUSTER, 1.5);
        put(Constant.CARD_EX, 1.0);
    }};

    public static double mergecardDmgMultiplier(String cardType) {
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
    public static double mergeDmgPositionMod(int position) {
        return dmgPositionModMap.get(position);
    }

    // double effectiveBuff,
    //魔放
    public static double mergeEffectiveBuff(String cardColor, double quick, double arts, double buster) {
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
    public static double mergeDmgFirstCardMod(String firstcardType) {
        return isBuster(firstcardType) ? 0.5 : 0;
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

    public static double mergeclassAtkMod(String svtClass) {
        svtClass = svtClass.toLowerCase();
        return classAtkModMap.get(svtClass);
    }

    //职阶相性
    public static double mergeAffinityMod(String affinity) {
        return ConditionData.affinityMap.get(affinity);
    }

    //阵营相性
    public static double mergeAttributeMod(String attribue) {
        return ConditionData.attributeMap.get(attribue);
    }

    // double atkBuff,
    // double defBuff,
    // double specialBuff,
    // double specialDefBuff,
    // double selfDmgBuff,
    // double selfDmgDefBuff,
    // double npPowerBuff
    public static double mergeBuffDebuff(double buff, double debuff) {
        return buff - debuff;
    }

    // double criticalMod,
    public static double mergeDmgCriticalMod(boolean isCritical) {
        return isCritical ? 2.0 : 1.0;
    }

    // double criticalBuff,
    // double starRate,
    public static double mergeCriticalBuff(boolean isCritical, String cardType, double buff, double debuff, double quick, double arts, double buster) {
        if (isCritical && !isNp(cardType)) {
            switch (getCardColor(cardType)) {
                case Constant.CARD_QUICK:
                    return mergeBuffDebuff(buff + quick, debuff);
                case Constant.CARD_ARTS:
                    return mergeBuffDebuff(buff + arts, debuff);
                case Constant.CARD_BUSTER:
                    return mergeBuffDebuff(buff + buster, debuff);
                default:
                    return mergeBuffDebuff(buff, debuff);
            }
        }
        return 0;
    }

    // double exDmgBuff,
    public static double mergeExDmgBuff(String cardType, boolean isSameColor) {
        if (isEx(cardType)) {
            return isSameColor ? 3.5 : 2.0;
        }
        return 1.0;
    }

    // double busterChainMod 判断3张卡是否是红卡，ex卡不计算此项
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

    //double na,
    //double hits,
    public static double mergeNaHits(String cardType, double q, double a, double b, double ex, double np) {
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

    //double cardNpMultiplier,
    public static Map<String, Double> cardNpMultiplierMap = new HashMap<String, Double>() {{
        put(Constant.CARD_QUICK, 0.8);
        put(Constant.CARD_ARTS, 1.0);
        put(Constant.CARD_BUSTER, 1.5);
        put(Constant.CARD_EX, 1.0);
    }};

    public static double mergecardNpMultiplier(String cardType) {
        String color = getCardColor(cardType);
        return cardDmgMultiplierMap.get(color);
    }

    //double positionMod,
    public static Map<Integer, Double> npPositionModMap = new HashMap<Integer, Double>() {{
        put(1, 1.0);
        put(2, 1.5);
        put(3, 2.0);
        put(4, 1.0);
    }};

    public static double mergeNpPositionMod(int position) {
        return npPositionModMap.get(position);
    }

    //double firstCardMod
    public static double mergeNpFirstCardMod(String firstcardType) {
        return isArts(firstcardType) ? 1.0 : 0;
    }

    //double criticalMod,
    public static double mergeNpCriticalMod(boolean isCritical, String cardType) {
        return !isNp(cardType) && isCritical ? 2.0 : 1.0;
    }

    //double overkillMod,
    public static double mergeNpOverkillMod(boolean isCritical) {
        return isCritical ? 1.5 : 1.0;
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


    // double cardStarMultiplier,
    static Map<Integer, Double> quickStarMultiplierMap = new HashMap<Integer, Double>() {{
        put(1, 0.8);
        put(2, 1.3);
        put(3, 1.8);
    }};
    static Map<Integer, Double> artsStarMultiplierMap = new HashMap<Integer, Double>() {{
        put(1, 0d);
        put(2, 0d);
        put(3, 0d);
    }};
    static Map<Integer, Double> busterStarMultiplierMap = new HashMap<Integer, Double>() {{
        put(1, 0.1);
        put(2, 0.15);
        put(3, 0.2);
    }};

    public static double mergeCardStarMultiplier(String cardType, int position) {
        String cardColor = getCardColor(cardType);
        if (isQuick(cardColor)) {
            return quickStarMultiplierMap.get(position);
        }
        if (isArts(cardColor)) {
            return artsStarMultiplierMap.get(position);
        }
        if (isBuster(cardColor)) {
            return busterStarMultiplierMap.get(position);
        }
        if (isEx(cardColor)) {
            return 1d;
        }
        return 1d;
    }

    // double effectiveBuff,
    // double firstCardMod,
    public static double mergeStarFirstCardMod(String firstCardType) {
        return isQuick(firstCardType) ? 0.2 : 0d;
    }

    // double starRateBuff,
    public static double mergeStarCtriticalMod(boolean isCritical) {
        return isCritical ? 0.2 : 0;
    }

    // double criticalMod,
    // double enemyStarBuff,
    // double randomMod,
    // double overkillMultiplier,
    // double overkillAdd
    // double cardStarRate,
    // double overkillAdd,
    public static double mergeOverkillAdd(boolean isOverkill) {
        return isOverkill ? 0.3 : 0;
    }
    // double enemyAmount

    //是否暴击
    public static boolean isCritical(int position, boolean isCritical1, boolean isCritical2, boolean isCritical3) {
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>() {{
            put(1, isCritical1);
            put(2, isCritical2);
            put(3, isCritical3);
            put(4, false);
        }};
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
    static Map<String, String> cardColorMap = new HashMap<String, String>() {{
        put(Constant.CARD_QUICK, Constant.COLOR_QUICK);
        put(Constant.CARD_ARTS, Constant.COLOR_ARTS);
        put(Constant.CARD_BUSTER, Constant.COLOR_BUSTER);
        put(Constant.NP_QUICK, Constant.COLOR_QUICK);
        put(Constant.NP_ARTS, Constant.COLOR_ARTS);
        put(Constant.NP_BUSTER, Constant.COLOR_BUSTER);
    }};

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
        if (isCardsSameColor(cardType1, cardType2, cardType3) && isBuster(cardType1)) {
            return true;
        }
        return false;
    }

    //判断宝具卡位置
    public static int getNpPosition(String cardType1, String cardType2, String cardType3) {
        String[] cards = {cardType1, cardType2, cardType3};
        for (int i = 0; i < cards.length; i++) {
            if (isNp(cards[i])) {
                return i+1;
            }
        }
        return -1;
    }

}
