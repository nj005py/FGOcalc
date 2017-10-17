package com.phantancy.fgocalc.item;

import android.util.Log;

import com.phantancy.fgocalc.R;

public class CardItem {
    //私有的
    //weak_type 1白值，2克制，3被克
    private String name, nickname, class_type, card_Type,firstCardType;
    private int id, star, arts_hit, buster_hit, quick_hit, ex_hit, solid_buff,position,weak_type;
    private double quick_na, arts_na, buster_na, ex_na, trump_na, nd, arts_buff, buster_buff, quick_buff,
            atk_buff, special_buff, critical_buff,aCardBuff,qCardBuff,bCardBuff;
    private boolean ifSameColor;
    //公开的
    public String cardType;
    public int cardPosition;
	public final double atkCor = 0.23;//攻击补正常数
	public double atkTimes = 0;//卡牌伤害倍率
	public double positionBuff = 0;
	public double cardBuff = 0;
	public double firstCardBuff = 0;
	public double classCor = 0;
	public double atkBuff = 0;
	public double specialBuff = 0;
	public double criticalBuff = 0;
	public double criticalCor = 1;
	public double exReward = 1;//同色3.5，不同色2.0
	public double solidAtkBuff = 0;
    public double weakCor = 1;
	public boolean ifCritical;
    //np
    public double na;
    public int hits;
    public double npTimes = 0;//np倍率
    public double npBuff;
    public double overkill = 1;
    public boolean ifOverkill = false;
    public double npFirstCardBuff = 0;
    public double npPositionBuff = 1.0;

    //从者，位置，卡色，首卡，同色，暴击,克制,红魔放，蓝魔放，绿魔放，暴击，攻击
	public CardItem(ServantItem servantItem,int position,String card_Type,String firstCardType,
                    boolean ifSameColor,boolean ifCritical,int weak_type,double bCardBuff,
                    double aCardBuff,double qCardBuff,double critical_buff,double atk_buff){
        this.position = position;
        this.card_Type = card_Type;
        this.firstCardType = firstCardType;
        this.ifSameColor = ifSameColor;
        this.ifCritical = ifCritical;
        this.weak_type = weak_type;
        this.aCardBuff = aCardBuff;
        this.bCardBuff = bCardBuff;
        this.qCardBuff = qCardBuff;
        this.criticalBuff = critical_buff;
        this.atk_buff = atk_buff;
        specialBuff = special_buff;
        criticalBuff = critical_buff;
        solidAtkBuff = solid_buff;
        atkBuff = atk_buff;
        init(servantItem);
        Log.d("CARD","卡牌倍率" + this.atkTimes + " 位置加成" + this.positionBuff + " 首位加成" + this.firstCardBuff
                + " 阶职补正" + this.classCor  + " 暴击补正" + this.criticalCor + " 克制" + weakCor +
                " ex奖励" + this.exReward + " 卡牌buff" + cardBuff + " 固定伤害" + solidAtkBuff);
        Log.d("CARD","卡牌倍率" + this.npTimes + " 位置加成" + this.npPositionBuff + " 首位加成" + this.npFirstCardBuff
                + " NP Buff" + this.npBuff +  " 暴击补正" + this.criticalCor + " overKill" + this.overkill +
                "\nNP获取率:" + na + " hits" + hits + " cardBuff" + cardBuff);
	}

    //np的构造函数
    //从者，位置，卡色，首卡，暴击，蓝魔放，绿魔放,np获得量,overkill
    public CardItem(ServantItem servantItem,int position,String card_Type,String firstCardType,boolean ifCritical,
                    double aCardBuff,double qCardBuff,double npBuff,boolean ifOverkill){
        this.position = position;
        this.card_Type = card_Type;
        this.firstCardType = firstCardType;
        this.ifCritical = ifCritical;
        this.aCardBuff = aCardBuff;
        this.qCardBuff = qCardBuff;
        this.npBuff = npBuff;
        this.ifOverkill = ifOverkill;
        init(servantItem);
        Log.d("CARD","卡牌倍率" + this.atkTimes + " 位置加成" + this.positionBuff + " 首位加成" + this.firstCardBuff
                + " 阶职补正" + this.classCor  + " 暴击补正" + this.criticalCor + " 克制" + weakCor +
                " ex奖励" + this.exReward + " 卡牌buff" + cardBuff + " 固定伤害" + solidAtkBuff);
        Log.d("CARD","卡牌倍率" + this.npTimes + " 位置加成" + this.npPositionBuff + " 首位加成" + this.npFirstCardBuff
                + " NP Buff" + this.npBuff +  " 暴击补正" + this.criticalCor + " overKill" + this.overkill +
                "\nNP获取率:" + na + " hits" + hits + " cardBuff" + cardBuff);
    }

    private void init(ServantItem servantItem){
        if (servantItem != null) {
            id = servantItem.getId();
            name = servantItem.getName();
            nickname = servantItem.getNickname();
            class_type = servantItem.getClass_type();
            star = servantItem.getStar();
            arts_hit = servantItem.getArts_hit();
            buster_hit = servantItem.getBuster_hit();
            quick_hit = servantItem.getQuick_hit();
            ex_hit = servantItem.getEx_hit();
            quick_na = servantItem.getQuick_na();
            arts_na = servantItem.getArts_na();
            buster_na = servantItem.getBuster_na();
            ex_na = servantItem.getEx_na();
            trump_na = servantItem.getTrump_na();
            nd = servantItem.getNd();
            arts_buff = servantItem.getArts_buff();
            buster_buff = servantItem.getBuster_buff();
            quick_buff = servantItem.getQuick_buff();
            atk_buff = servantItem.getAtk_buff();
            special_buff = servantItem.getSpecial_buff();
            critical_buff = servantItem.getCritical_buff();
            solid_buff = servantItem.getSolid_buff();
        }
        cardPosition = this.position;
        cardType = card_Type;
        atkTimes();
        positionBuff();
        firstCardBuff();
        classCor();
        criticalCor();
        exReward();
        weakCor();
        overkillF();
    }

    private void atkTimes(){
        switch (card_Type) {
            case "b":
                atkTimes = 1.5;
                npTimes = 0;
                cardBuff = buster_buff + bCardBuff;
                na = buster_na;
                hits = buster_hit;
                break;
            case "a":
                atkTimes = 1.0;
                npTimes = 3.0;
                cardBuff = arts_buff + aCardBuff;
                na = arts_na;
                hits = arts_hit;
                break;
            case "q":
                atkTimes = 0.8;
                npTimes = 1.0;
                cardBuff = quick_buff + qCardBuff;
                na = quick_na;
                hits = quick_hit;
                break;
            case "ex":
                atkTimes = 1.0;
                npTimes = 1.0;
                cardBuff = 0;
                na = ex_na;
                hits = ex_hit;
        }
    }

    private void positionBuff(){
        switch (position) {
            case 1:
                positionBuff = 1.0;
                npPositionBuff = 1.0;
                break;
            case 2:
                positionBuff = 1.2;
                npPositionBuff = 1.5;
                break;
            case 3:
                positionBuff = 1.4;
                npPositionBuff = 2.0;
                break;
            case 4:
                positionBuff = 1.0;
                npPositionBuff = 1.0;
                break;
        }
    }

    private void firstCardBuff(){
        switch (firstCardType) {
            case "b":
                firstCardBuff = 0.5;
                npFirstCardBuff = 0;
                break;
            case "a":
                firstCardBuff = 0;
                npFirstCardBuff = 1.0;
                break;
            case "q":
                firstCardBuff = 0;
                npFirstCardBuff = 0;
                break;
        }
    }

    private void classCor(){
        switch (class_type) {
            case "Saber":
                classCor = 1.00;
                break;
            case "Archer":
                classCor = 0.95;
                break;
            case "Lancer":
                classCor = 1.05;
                break;
            case "Rider":
                classCor = 1.0;
                break;
            case "Caster":
                classCor = 0.9;
                break;
            case "Assassin":
                classCor = 0.9;
                break;
            case "Berserker":
                classCor = 1.1;
                break;
            case "Ruler":
                classCor = 1.1;
                break;
            case "Shielder":
                classCor = 1.0;
                break;
            case "Alterego":
                classCor = 1.0;
                break;
            case "Avenger":
                classCor = 1.1;
                break;
            case "Beast":
                classCor = 1.0;
                break;
            case "MoonCancer":
                classCor = 1.0;
                break;
        }
    }

    private void criticalCor(){
        if (ifCritical) {
            criticalCor = 2.0;
            criticalBuff = criticalBuff + critical_buff;
        }else{
            criticalCor = 1;
            criticalBuff = 0;
        }
    }

    private void exReward(){
        if (ifSameColor && position == 4) {
            exReward = 3.5;
        }else if(!ifSameColor && position == 4){
            exReward = 2.0;
        }else{
            exReward = 1.0;
        }
    }

    private void weakCor(){
        switch (weak_type) {
            case 1:
                weakCor = 1.0;
                break;
            case 2:
                if (class_type.equals("Berserker")) {
                    weakCor = 1.5;
                } else {
                    weakCor = 2.0;
                }
                break;
            case 3:
                weakCor = 0.5;
                break;
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
