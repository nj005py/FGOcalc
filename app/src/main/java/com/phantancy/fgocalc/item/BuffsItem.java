package com.phantancy.fgocalc.item;

import java.io.Serializable;

/**
 * Created by HATTER on 2017/11/6.
 * 用于计算的buff合集
 */

public class BuffsItem implements Serializable {
    private double enemyDefence = 0,//敌方防御
            specialDefence = 0,//敌方特攻防御
            solidDefence = 0,//敌方固定防御
            atkUp = 0,//加攻
            criticalUp = 0,//暴击提升
            busterUp = 0,//红魔放
            artsUp = 0,//蓝魔放
            quickUp = 0,//绿魔放
            specialUp = 0,//特攻
            trumpUp = 0,//宝具威力提升
            trumpSpecialUp = 0,//宝具特攻
            npUp = 0,//np获取量
            solidAtk = 0,//固定伤害
            starUp = 0,//掉星率
            extraTimes = 0;//附加倍率

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

    public double getBusterUp() {
        return busterUp;
    }

    public void setBusterUp(double busterUp) {
        this.busterUp = busterUp;
    }

    public double getArtsUp() {
        return artsUp;
    }

    public void setArtsUp(double artsUp) {
        this.artsUp = artsUp;
    }

    public double getQuickUp() {
        return quickUp;
    }

    public void setQuickUp(double quickUp) {
        this.quickUp = quickUp;
    }

    public double getAtkUp() {
        return atkUp;
    }

    public void setAtkUp(double atkUp) {
        this.atkUp = atkUp;
    }

    public double getCriticalUp() {
        return criticalUp;
    }

    public void setCriticalUp(double criticalUp) {
        this.criticalUp = criticalUp;
    }

    public double getSpecialUp() {
        return specialUp;
    }

    public void setSpecialUp(double specialUp) {
        this.specialUp = specialUp;
    }

    public double getTrumpUp() {
        return trumpUp;
    }

    public void setTrumpUp(double trumpUp) {
        this.trumpUp = trumpUp;
    }

    public double getTrumpSpecialUp() {
        return trumpSpecialUp;
    }

    public void setTrumpSpecialUp(double trumpSpecialUp) {
        this.trumpSpecialUp = trumpSpecialUp;
    }

    public double getNpUp() {
        return npUp;
    }

    public void setNpUp(double npUp) {
        this.npUp = npUp;
    }

    public double getSolidAtk() {
        return solidAtk;
    }

    public void setSolidAtk(double solidAtk) {
        this.solidAtk = solidAtk;
    }

    public double getStarUp() {
        return starUp;
    }

    public void setStarUp(double starUp) {
        this.starUp = starUp;
    }

    public double getExtraTimes() {
        return extraTimes;
    }

    public void setExtraTimes(double extraTimes) {
        this.extraTimes = extraTimes;
    }
}
