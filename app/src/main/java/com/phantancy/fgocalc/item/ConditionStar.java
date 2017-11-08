package com.phantancy.fgocalc.item;

import java.io.Serializable;

/**
 * Created by HATTER on 2017/11/6.
 */

public class ConditionStar implements Serializable {
    private int atk;
    //环境类型的参数例如选卡、是否暴击、职阶相性、阵营相性、乱数补正
    private String cardType1, cardType2, cardType3;//1,2,3号位选卡
    private boolean ifCr1, ifCr2, ifCr3;//1-3号位是否暴击
    private boolean ifok1 = false, ifok2 = false, ifok3 = false;//1-3overkill
    private double randomCor = -0.1;//敌补正
    private ServantItem servantItem;
    private BuffsItem buffsItem;

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public String getCardType1() {
        return cardType1;
    }

    public void setCardType1(String cardType1) {
        this.cardType1 = cardType1;
    }

    public String getCardType2() {
        return cardType2;
    }

    public void setCardType2(String cardType2) {
        this.cardType2 = cardType2;
    }

    public String getCardType3() {
        return cardType3;
    }

    public void setCardType3(String cardType3) {
        this.cardType3 = cardType3;
    }

    public boolean isIfCr1() {
        return ifCr1;
    }

    public void setIfCr1(boolean ifCr1) {
        this.ifCr1 = ifCr1;
    }

    public boolean isIfCr2() {
        return ifCr2;
    }

    public void setIfCr2(boolean ifCr2) {
        this.ifCr2 = ifCr2;
    }

    public boolean isIfCr3() {
        return ifCr3;
    }

    public void setIfCr3(boolean ifCr3) {
        this.ifCr3 = ifCr3;
    }

    public boolean isIfok1() {
        return ifok1;
    }

    public void setIfok1(boolean ifok1) {
        this.ifok1 = ifok1;
    }

    public boolean isIfok2() {
        return ifok2;
    }

    public void setIfok2(boolean ifok2) {
        this.ifok2 = ifok2;
    }

    public boolean isIfok3() {
        return ifok3;
    }

    public void setIfok3(boolean ifok3) {
        this.ifok3 = ifok3;
    }

    public double getRandomCor() {
        return randomCor;
    }

    public void setRandomCor(double randomCor) {
        this.randomCor = randomCor;
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
