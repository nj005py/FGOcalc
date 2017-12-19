package org.phantancy.fgocalc.item;

import java.io.Serializable;

/**
 * Created by HATTER on 2017/11/7.
 */

public class ConditionTrump implements Serializable {
    private int atk;
    private int hpTotal = 0,hpLeft = 0;
    //环境类型的参数例如选卡、是否暴击、职阶相性、阵营相性、乱数补正
    private String trumpColor;
    private int weakType = 1;//职阶相性类型
    private double teamCor = 1.0, //阵营相性
            randomCor = 0.9,//乱数补正
            trumpTimes = 0,//宝具倍率
            atkCor = 0.23,
            cardTimes,
            cardBuff,
            classCor,
            weakCor,
            atkBuff,
            enemyDefence = 0,//敌方防御
            specialDefence = 0,//敌方特攻防御
            solidDefence = 0,//敌方固定防御;
            specialBuff,
            trumpPowerBuff,
            trumpBuff,
            solidBuff,
            trumpDown = 0;

    private ServantItem servantItem;
    private BuffsItem buffsItem;

    public double getTrumpDown() {
        return trumpDown;
    }

    public void setTrumpDown(double trumpDown) {
        this.trumpDown = trumpDown;
    }

    public double getCardBuff() {
        return cardBuff;
    }

    public void setCardBuff(double cardBuff) {
        this.cardBuff = cardBuff;
    }

    public double getClassCor() {
        return classCor;
    }

    public void setClassCor(double classCor) {
        this.classCor = classCor;
    }

    public double getWeakCor() {
        return weakCor;
    }

    public void setWeakCor(double weakCor) {
        this.weakCor = weakCor;
    }

    public double getAtkBuff() {
        return atkBuff;
    }

    public void setAtkBuff(double atkBuff) {
        this.atkBuff = atkBuff;
    }

    public double getEnemyDefence() {
        return enemyDefence;
    }

    public void setEnemyDefence(double enemyDefence) {
        this.enemyDefence = enemyDefence;
    }

    public double getSpecialDefence() {
        return specialDefence;
    }

    public void setSpecialDefence(double specialDefence) {
        this.specialDefence = specialDefence;
    }

    public double getSolidDefence() {
        return solidDefence;
    }

    public void setSolidDefence(double solidDefence) {
        this.solidDefence = solidDefence;
    }

    public double getSpecialBuff() {
        return specialBuff;
    }

    public void setSpecialBuff(double specialBuff) {
        this.specialBuff = specialBuff;
    }

    public double getTrumpPowerBuff() {
        return trumpPowerBuff;
    }

    public void setTrumpPowerBuff(double trumpPowerBuff) {
        this.trumpPowerBuff = trumpPowerBuff;
    }

    public double getTrumpBuff() {
        return trumpBuff;
    }

    public void setTrumpBuff(double trumpBuff) {
        this.trumpBuff = trumpBuff;
    }

    public double getSolidBuff() {
        return solidBuff;
    }

    public void setSolidBuff(double solidBuff) {
        this.solidBuff = solidBuff;
    }

    public double getCardTimes() {
        return cardTimes;
    }

    public void setCardTimes(double cardTimes) {
        this.cardTimes = cardTimes;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getHpTotal() {
        return hpTotal;
    }

    public void setHpTotal(int hpTotal) {
        this.hpTotal = hpTotal;
    }

    public int getHpLeft() {
        return hpLeft;
    }

    public void setHpLeft(int hpLeft) {
        this.hpLeft = hpLeft;
    }

    public String getTrumpColor() {
        return trumpColor;
    }

    public void setTrumpColor(String trumpColor) {
        this.trumpColor = trumpColor;
    }

    public int getWeakType() {
        return weakType;
    }

    public void setWeakType(int weakType) {
        this.weakType = weakType;
    }

    public double getTeamCor() {
        return teamCor;
    }

    public void setTeamCor(double teamCor) {
        this.teamCor = teamCor;
    }

    public double getRandomCor() {
        return randomCor;
    }

    public void setRandomCor(double randomCor) {
        this.randomCor = randomCor;
    }

    public double getTrumpTimes() {
        return trumpTimes;
    }

    public void setTrumpTimes(double trumpTimes) {
        this.trumpTimes = trumpTimes;
    }

    public double getAtkCor() {
        return atkCor;
    }

    public void setAtkCor(double atkCor) {
        this.atkCor = atkCor;
    }

    public ServantItem getServantItem() {
        return servantItem;
    }

    public void setServantItem(ServantItem servantItem) {
        this.servantItem = servantItem;
    }

    public BuffsItem getBuffsItem() {
        return buffsItem;
    }

    public void setBuffsItem(BuffsItem buffsItem) {
        this.buffsItem = buffsItem;
    }
}
