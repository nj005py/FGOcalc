package com.phantancy.fgocalc.item;

import android.util.Log;

/**
 * Created by PY on 2017/2/6.
 */
public class CardNP {
    /**
     * 每Hit主动NP获得量×攻击Hit数×［卡牌NP倍率×位置加成×（1+卡牌Buff）＋首位加成］×
     * （1+NP Buff）×暴击补正×Overkill补正×敌补正
     */
    public String cardType;
    public double na;
    public int hits;
    public double ana;
    public double bna;
    public double qna;
    public double ena;
    public int ahits;
    public int bhits;
    public int qhits;
    public int ehits;
    public double times;//倍率
    public int position;
    public double positionBuff = 0;
    public double cardBuff;
    public double aCardBuff = 0;//蓝魔放
    public double qCardBuff = 0;//绿魔放
    public double firstCard = 0;//首位加成
    public String firstCardType;//首卡类型
    public double npBuff;
    public double criticalCor = 1;
    public boolean ifCritical;
    public double overkill = 1;
    public boolean ifOverkill = false;

    public CardNP(String cardType,double ana,double bna,double qna,double ena,int ahits,int bhits,int qhits,int ehits,
                  int position, double aCardBuff,double qCardBuff, String firstCardType, double npBuff,boolean ifCritical,
                  boolean ifOverkill) {

        this.cardType = cardType;
        this.ana = ana;
        this.bna = bna;
        this.qna = qna;
        this.ena = ena;
        this.ahits = ahits;
        this.bhits = bhits;
        this.qhits = qhits;
        this.ehits = ehits;
        this.position = position;
        this.aCardBuff = aCardBuff;
        this.qCardBuff = qCardBuff;
        this.firstCardType = firstCardType;
        this.npBuff = npBuff;
        this.ifCritical = ifCritical;
        this.ifOverkill = ifOverkill;

        cardF();
        positionF();
        firstCardF();
        criticalCorF();
        overkillF();
        Log.d("CARD","卡牌倍率" + this.times + " 位置加成" + this.positionBuff + " 首位加成" + this.firstCard
                + " NP Buff" + this.npBuff +  " 暴击补正" + this.criticalCor + " overKill" + this.overkill +
        "\nNP获取率:" + na + " hits" + hits);
    }

    private void cardF(){
        switch (cardType) {
            case "b":
                times = 0;
                na = bna;
                hits = bhits;
                cardBuff = 0;
                break;
            case "a":
                times = 3.0;
                na = ana;
                hits = ahits;
                cardBuff = aCardBuff;
                break;
            case "q":
                times = 1.0;
                na = qna;
                hits = qhits;
                cardBuff = qCardBuff;
                break;
            case "ex":
                times = 1.0;
                na = ena;
                hits = ehits;
                cardBuff = 0;
                break;
        }
    }

    private void positionF(){
        switch (position) {
            case 1:
                positionBuff = 1.0;
                break;
            case 2:
                positionBuff = 1.5;
                break;
            case 3:
                positionBuff = 2.0;
                break;
            case 4:
                positionBuff = 1.0;
                break;
        }
    }

    private void firstCardF(){
        switch (firstCardType) {
            case "b":
                firstCard = 0;
                break;
            case "a":
                firstCard = 1.0;
                break;
            case "q":
                firstCard = 0;
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

    private void overkillF(){
        if (ifOverkill) {
            overkill = 1.5;
        }else{
            overkill = 1;
        }
    }
}
