package com.phantancy.fgocalc.item;

import android.util.Log;

/**
 * Created by PY on 2017/2/6.
 */
public class Card {
    /***
    ATK×攻击补正×[卡牌伤害倍率×位置加成×(1+卡牌BUFF)+首位加成]×
     职阶补正×职阶相性补正×阵营相性补正×乱数补正×(1+攻击力BUFF—敌方防御力BUFF)×
     (1+特攻威力BUFF—敌方特防威力BUFF+暴击威力BUFF)×暴击补正×EX攻击奖励
    +(固定伤害BUFF—敌方固定伤害BUFF)
    + ATK×Buster Chain加成/
     */
    public double atkCor = 0.23;
    public double atkPercent = 0;
    public String cardType;
    public int position = 0;
    public double positionBuff = 0;
    public double cardBuff = 0;
    public double aCardBuff = 0;//蓝魔放
    public double qCardBuff = 0;//绿魔放
    public double firstCard = 0;
    public String firstCardType;
    public double classCor = 0;
    public String classType;
//    public double random = getRan();
    public double atkBuff = 0;
    public double specialBuff = 0;
    public double criticalBuff = 0;
    public double criticalCor = 1;
    public double exPresent = 1;//同色3.5，不同色2.0
    public double solidAtkBuff = 0;
    public boolean ifCritical;
    public boolean ifSameColor;

    public Card(String cardType, int position, double cardBuff, String firstCardType,
                String classType, double atkBuff,
                double specialBuff, double criticalBuff, boolean ifCritical, double solidAtkBuff,
                boolean ifSameColor) {
        this.cardType = cardType;
        this.position = position;
        this.cardBuff = cardBuff;
        this.firstCardType = firstCardType;
        this.classType = classType;
        this.atkBuff = atkBuff;
        this.specialBuff = specialBuff;
        this.criticalBuff = criticalBuff;
        this.ifCritical = ifCritical;
        this.solidAtkBuff = solidAtkBuff;
        this.ifSameColor = ifSameColor;
        /***
         ATK×攻击补正×[卡牌伤害倍率×位置加成×(1+卡牌BUFF)+首位加成]×
         职阶补正×职阶相性补正×阵营相性补正×乱数补正×(1+攻击力BUFF—敌方防御力BUFF)×
         (1+特攻威力BUFF—敌方特防威力BUFF+暴击威力BUFF)×暴击补正×EX攻击奖励
         +(固定伤害BUFF—敌方固定伤害BUFF)
         + ATK×Buster Chain加成/
         */
        atkPercentF();
        positionF();
        firstCardF();
        classCorF();
        criticalCorF();
        exPresentF();
        Log.d("CARD","卡牌倍率" + this.atkPercent + " 位置加成" + this.positionBuff + " 首位加成" + this.firstCard
                + " 阶职补正" + this.classCor  + " 暴击补正" + this.criticalCor +
                " ex奖励" + this.exPresent);
    }

    private void atkPercentF(){
        switch (cardType) {
            case "b":
                atkPercent = 1.5;
                break;
            case "a":
                atkPercent = 1.0;
                break;
            case "q":
                atkPercent = 0.8;
                break;
            case "ex":
                atkPercent = 1.0;
        }
    }

    private void positionF(){
        switch (position) {
            case 1:
                positionBuff = 1.0;
                break;
            case 2:
                positionBuff = 1.2;
                break;
            case 3:
                positionBuff = 1.4;
                break;
            case 4:
                positionBuff = 1.0;
                break;
        }
    }

    private void firstCardF(){
        switch (firstCardType) {
            case "b":
                firstCard = 0.5;
                break;
            case "a":
                firstCard = 0;
                break;
            case "q":
                firstCard = 0;
                break;
        }
    }

    private void classCorF(){
        String classCache = classType.toLowerCase();
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
        }
    }

    private void criticalCorF(){
        if (ifCritical) {
            criticalCor = 2.0;
        }else{
            criticalCor = 1;
        }
    }

    private void exPresentF(){
        if (ifSameColor && position == 4) {
            exPresent = 3.5;
        }else if(!ifSameColor && position == 4){
            exPresent = 2.0;
        }else{
            exPresent = 1.0;
        }
    }

    private double getRan(){
        return Math.random() * 0.2 + 0.9;
    }
}
