package org.phantancy.fgocalc.item;

/**
 * Created by HATTER on 2017/11/6.
 * 把被动buff，外加的buff，各种条件整合，创建一张指令卡
 */

public class CommandCard {
    //公开的用于计算公式直接计算
    public int atk;
    public String cardType;
    public int cardPosition;
    public final double atkCor = 0.23;//攻击补正常数
    public double atkTimes = 0;//卡牌伤害倍率
    public double positionBuff = 0;
    public double cardBuff = 0;//卡牌buff
    public double firstCardBuff = 0;
    public double classCor = 0;
    public double atkBuff = 0;
    public double specialBuff = 0;
    public double criticalBuff = 0;
    public double criticalCor = 1;
    public double exReward = 1;//同色3.5，不同色2.0
    public double solidBuff = 0;
    public double weakCor = 1;
    public boolean ifCritical;
    public double enemyDefence = 0;
    public double busterChain = 0;
    public double randomCor = 0;
    public double teamCor = 0;
    public double solidDefence = 0;
    public double specialDefence = 0;
    //np
    public double na;
    public int hits;
    public double npTimes = 0;//np倍率
    public double npBuff;
    public double overkill = 1;
    public boolean ifOverkill = false;
    public double npFirstCardBuff = 0;
    public double npPositionBuff = 1.0;
    //star
    public double starOccur = 0;
    public double cardStarCor = 0;
    public double starOccurBuff = 0;
    public double enemyStarBuff = 0;
    public double starOverkillMuti = 1;
    public double starOverkillPlus = 0.3;
    public double starCriticalCor = 0;
    public double starFirstCardBuff = 0;
    //宝具卡的条件
    public ConditionTrump conT;
    //私有的
    //从者属性+内部机制
    private boolean ifSameColor;
    private String class_type,
            firstCardType;
    private int arts_hit,
            buster_hit,
            quick_hit,
            ex_hit,
            solid_atk,
            weak_type;
    private double quick_na,
            arts_na,
            buster_na,
            ex_na,
            trump_na,
            nd,
            arts_buff,
            buster_buff,
            quick_buff,
            atk_buff,
            special_buff,
            critical_buff,
            star_occur,
            star_occur_extra;
    //外部buff
    private double artsUp,
            quickUp,
            busterUp,
            criticalUp,
            solidAtk;
    private ServantItem sItem;
    private BuffsItem bItem;
    private ConditionAtk conAtk;
    private ConditionNp conNp;
    private ConditionStar conS;

    public ConditionStar getConS() {
        return conS;
    }

    //atk构造函数
    public CommandCard(int cardPosition, ConditionAtk conAtk,ConditionTrump conT) {
        this.cardPosition = cardPosition;
        this.conAtk = conAtk;
        this.conT = conT;
        cardForAtk();
    }

    public CommandCard(int cardPosition, ConditionNp conNp) {
        this.cardPosition = cardPosition;
        this.conNp = conNp;
        cardForNp();
    }

    public CommandCard(int cardPosition, ConditionStar conS) {
        this.cardPosition = cardPosition;
        this.conS = conS;
        cardForStar();
    }

    private void cardForAtk(){
        if (conAtk != null) {
            sItem = conAtk.getServantItem();
            bItem = conAtk.getBuffsItem();
            atk = conAtk.getAtk();
            atkBuff = atk_buff + (bItem.getAtkUp() / 100);
            specialBuff = bItem.getSpecialUp() / 100;
            enemyDefence = bItem.getEnemyDefence() / 100;
            randomCor = conAtk.getRandomCor();
            teamCor = conAtk.getTeamCor();
            getServant();
            posForAtk();
            getFirstCardBuffForAtk();
            timesForAll();
            classCorForAtk();
            criticalCorForAll();
            exRewardForAtk();
            weakCorForAtk();
            getSolidAtkForAtk();
        }
    }

    private void cardForNp(){
        if (conNp != null) {
            sItem = conNp.getServantItem();
            bItem = conNp.getBuffsItem();
            randomCor = conNp.getRandomCor();
            npBuff = bItem.getNpUp() / 100;
            getServant();
            posForNp();
            getFirstCardBuffForNp();
            timesForAll();
            criticalCorForAll();
        }
    }

    private void cardForStar(){
        if (conS != null) {
            sItem = conS.getServantItem();
            bItem = conS.getBuffsItem();
            getServant();
            posForStar();
            overkillForStar();
            criticalCorForAll();
            getFirstCardBuffForStar();
            starOccur = star_occur;
            starOccurBuff = star_occur_extra + bItem.getStarUp() / 100;
            randomCor = conS.getRandomCor();
        }
    }
    
    private void getServant(){
        if (sItem != null) {
            class_type = sItem.getClass_type();
            arts_hit = sItem.getArts_hit();
            buster_hit = sItem.getBuster_hit();
            quick_hit = sItem.getQuick_hit();
            ex_hit = sItem.getEx_hit();
            quick_na = sItem.getQuick_na();
            arts_na = sItem.getArts_na();
            buster_na = sItem.getBuster_na();
            ex_na = sItem.getEx_na();
            trump_na = sItem.getTrump_na();
            nd = sItem.getNd();
            arts_buff = sItem.getArts_buff();
            buster_buff = sItem.getBuster_buff();
            quick_buff = sItem.getQuick_buff();
            atk_buff = sItem.getAtk_buff();
            special_buff = sItem.getSpecial_buff();
            critical_buff = sItem.getCritical_buff();
            solid_atk = sItem.getSolid_buff();
            star_occur = sItem.getStar_occur();
            star_occur_extra = sItem.getStar_occur_extra();
        }
    }

    private void posForAtk(){
        switch (cardPosition) {
            case 1:
                cardType = conAtk.getCardType1();
                positionBuff = 1.0;
                ifCritical = conAtk.isIfCr1();
                break;
            case 2:
                cardType = conAtk.getCardType2();
                positionBuff = 1.2;
                ifCritical = conAtk.isIfCr2();
                break;
            case 3:
                cardType = conAtk.getCardType3();
                positionBuff = 1.4;
                ifCritical = conAtk.isIfCr3();
                break;
            case 4:
                cardType = "ex";
                positionBuff = 1.0;
                ifCritical = false;
                break;
        }
    }

    private void posForNp(){
        switch (cardPosition) {
            case 1:
                cardType = conNp.getCardType1();
                npPositionBuff = 1.0;
                ifCritical = conNp.isIfCr1();
                ifOverkill = conNp.isIfok1();
                overkillForNp();
                break;
            case 2:
                cardType = conNp.getCardType2();
                npPositionBuff = 1.5;
                ifCritical = conNp.isIfCr2();
                ifOverkill = conNp.isIfok2();
                overkillForNp();
                break;
            case 3:
                cardType = conNp.getCardType3();
                npPositionBuff = 2.0;
                ifCritical = conNp.isIfCr3();
                ifOverkill = conNp.isIfok3();
                overkillForNp();
                break;
            case 4:
                cardType = "ex";
                npPositionBuff = 1.0;
                ifCritical = false;
                ifOverkill = conNp.isIfok3();
                overkillForNp();
                break;
        }
    }

    private void posForStar(){
        switch (cardPosition) {
            case 1:
                cardType = conS.getCardType1();
                ifCritical = conS.isIfCr1();
                ifOverkill = conS.isIfok1();
                switch (cardType) {
                    case "b":
                        cardStarCor = 0.1;
                        break;
                    case "a":
                        cardStarCor = 0;
                        break;
                    case "q":
                        cardStarCor = 0.2;
                        break;
                }
                break;
            case 2:
                cardType = conS.getCardType2();
                ifCritical = conS.isIfCr2();
                ifOverkill = conS.isIfok2();
                switch (cardType) {
                    case "b":
                        cardStarCor = 0.15;
                        break;
                    case "a":
                        cardStarCor = 0;
                        break;
                    case "q":
                        cardStarCor = 1.3;
                        break;
                }
                break;
            case 3:
                cardType = conS.getCardType3();
                ifCritical = conS.isIfCr3();
                ifOverkill = conS.isIfok3();
                switch (cardType) {
                    case "b":
                        cardStarCor = 0.2;
                        break;
                    case "a":
                        cardStarCor = 0;
                        break;
                    case "q":
                        cardStarCor = 1.8;
                        break;
                }
                break;
            case 4:
                cardType = "ex";
                ifCritical = false;
                ifOverkill = conS.isIfok3();
                cardStarCor = 1;
                break;
        }
        switch (cardType) {
            case "b":
                hits = sItem.getBuster_hit();
                cardBuff = buster_buff + bItem.getBusterUp() / 100;
                break;
            case "a":
                hits = sItem.getArts_hit();
                cardBuff = arts_buff + bItem.getArtsUp() / 100;
                break;
            case "q":
                hits = sItem.getQuick_hit();
                cardBuff = quick_buff + bItem.getQuickUp() / 100;
                break;
            case "ex":
                hits = sItem.getEx_hit();
                break;
        }
    }

    private void getFirstCardBuffForAtk(){
        firstCardType = conAtk.getCardType1();
        if (firstCardType.equals("np")) {
            firstCardType = sItem.getTrump_color();
        }
        switch (firstCardType) {
            case "b":
                firstCardBuff = 0.5;
                break;
            case "a":
                firstCardBuff = 0;
                break;
            case "q":
                firstCardBuff = 0;
                break;
        }
    }

    private void getFirstCardBuffForNp(){
        firstCardType = conNp.getCardType1();
        if (firstCardType.equals("np")) {
            firstCardType = sItem.getTrump_color();
        }
        switch (firstCardType) {
            case "b":
                npFirstCardBuff = 0;
                break;
            case "a":
                npFirstCardBuff = 1.0;
                break;
            case "q":
                npFirstCardBuff = 0;
                break;
        }
    }

    private void getFirstCardBuffForStar(){
        firstCardType = conS.getCardType1();
        if (firstCardType.equals("np")) {
            firstCardType = sItem.getTrump_color();
        }
        switch (firstCardType) {
            case "b":
                starFirstCardBuff = 0;
                break;
            case "a":
                starFirstCardBuff = 0;
                break;
            case "q":
                starFirstCardBuff = 0.2;
                break;
        }
    }

    private void timesForAll(){
        String cType = "";
        cType = getCardColor(cardType,sItem);
        switch (cType) {
            case "b":
                atkTimes = 1.5;
                npTimes = 0;
                busterUp = bItem.getBusterUp() / 100;
                cardBuff = buster_buff + busterUp;
                na = buster_na;
                hits = buster_hit;
                break;
            case "a":
                atkTimes = 1.0;
                npTimes = 3.0;
                artsUp = bItem.getArtsUp() / 100;
                cardBuff = arts_buff + artsUp;
                na = arts_na;
                hits = arts_hit;
                break;
            case "q":
                atkTimes = 0.8;
                npTimes = 1.0;
                quickUp = bItem.getQuickUp() / 100;
                cardBuff = quick_buff + quickUp;
                na = quick_na;
                hits = quick_hit;
                break;
            case "ex":
                atkTimes = 1.0;
                npTimes = 1.0;
                cardBuff = 0;
                na = ex_na;
                hits = ex_hit;
                break;
        }
    }

    private void classCorForAtk(){
        String classCache = class_type.toLowerCase();
        switch (classCache) {
            case "saber":
                classCor = 1.00;
                break;
            case "archer":
                classCor = 0.95;
                break;
            case "lancer":
                classCor = 1.05;
                break;
            case "rider":
                classCor = 1.0;
                break;
            case "caster":
                classCor = 0.9;
                break;
            case "assassin":
                classCor = 0.9;
                break;
            case "berserker":
                classCor = 1.1;
                break;
            case "ruler":
                classCor = 1.1;
                break;
            case "shielder":
                classCor = 1.0;
                break;
            case "alterego":
                classCor = 1.0;
                break;
            case "avenger":
                classCor = 1.1;
                break;
            case "beast":
                classCor = 1.0;
                break;
            case "mooncancer":
                classCor = 1.0;
                break;
            case "foreigner":
                classCor = 1.0;
                break;
        }
    }

    private void criticalCorForAll(){
        if (ifCritical) {
            criticalCor = 2.0;
            criticalUp = bItem.getCriticalUp() / 100;
            criticalBuff = criticalUp + critical_buff;
            starCriticalCor = 0.2;
        }else{
            criticalCor = 1;
            criticalBuff = 0;
            starCriticalCor = 0;
        }
    }

    private void exRewardForAtk(){
        busterChain = conAtk.getBusterChain();
        ifSameColor = conAtk.isIfsameColor();
        if (ifSameColor && cardPosition == 4) {
            exReward = 3.5;
            busterChain = 0;
        }else if(!ifSameColor && cardPosition == 4){
            exReward = 2.0;
        }else{
            exReward = 1.0;
        }
    }

    private void weakCorForAtk(){
        weak_type = conAtk.getWeakType();
        String cacheClass = class_type.toLowerCase();
        switch (weak_type) {
            case 1:
                weakCor = 1.0;
                break;
            case 2:
                if (cacheClass.equals("berserker") || cacheClass.equals("alterego")) {
                    weakCor = 1.5;
                } else {
                    weakCor = 2.0;
                }
                break;
            case 3:
                weakCor = 0.5;
                break;
            case 4:
                weakCor = 2.0;
                break;
        }
    }

    private void getSolidAtkForAtk(){
        solidAtk = bItem.getSolidAtk();
        solidBuff = solidAtk + solid_atk;
    }

    private void overkillForNp(){
        if (ifOverkill) {
            overkill = 1.5;
        }else{
            overkill = 1;
        }
    }

    private void overkillForStar(){
        if (ifOverkill) {
            starOverkillPlus = 0.3;
        }else{
            starOverkillPlus = 0;
        }
    }

    //获取卡色
    private String getCardColor(String cardType,ServantItem item){
        if (item != null) {
            if (cardType.equals("np")) {
                return item.getTrump_color();
            }else{
                return cardType;
            }
        }
        return null;
    }
}
