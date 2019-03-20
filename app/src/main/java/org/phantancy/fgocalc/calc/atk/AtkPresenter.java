package org.phantancy.fgocalc.calc.atk;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.calc.CalcActy;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.database.DBManager;
import org.phantancy.fgocalc.item.AtkHpItem;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.CommandCard;
import org.phantancy.fgocalc.item.ConditionAtk;
import org.phantancy.fgocalc.item.ConditionTrump;
import org.phantancy.fgocalc.item.CurveItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.BaseUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HATTER on 2017/11/6.
 */

public class AtkPresenter implements AtkContract.Presenter {
    @NonNull
    private final AtkContract.View mView;
    private String result = "";
    private int overallAttack;
    private Context ctx;
    private String TAG = getClass().getSimpleName();

    public AtkPresenter(@NonNull AtkContract.View mView,Context ctx) {
        this.mView = mView;
        this.ctx = ctx;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    //乱数补正
    public double getRan(int type) {
        double ran = 0.9;
        switch (type) {
            case Constant.TYPE_MIN:
                ran = 0.9;
                break;
            case Constant.TYPE_MAX:
                ran = 1.1;
                break;
            case Constant.TYPE_AVERAGE:
                ran = 1.0;
                break;
            case Constant.TYPE_RANDOM:
                ran = 0.01;//占位表示随机数
            break;
        }
        return ran;
    }

    @Override
    public AtkHpItem getAtkHpByLv(ServantItem sItem,int lv,List<CurveItem> curveList) {
        if (lv > 0 && curveList != null) {
            int curve = curveList.get(lv).getCurve();

            int atkBase = sItem.getAtk_base();
            int atkDefault = sItem.getDefault_atk();
            int hpBase = sItem.getHp_base();
            int hpDefault = sItem.getDefault_hp();

            int atk = (int)(atkBase + ((float)atkDefault - (float) atkBase) / 1000 * curve);
            int hp = (int)(hpBase + ((float)hpDefault - (float)hpBase) / 1000 * curve);

            return new AtkHpItem(atk,hp);
        }else{
            return new AtkHpItem(0,0);
        }
    }


    @Override
    public ConditionAtk getCondition(int atk, String cardType1, String cardType2, String cardType3,
                                     boolean ifEx, boolean ifCr1, boolean ifCr2, boolean ifCr3,
                                     int weakType, double teamCor, double randomCor,
                                     ServantItem servantItem, BuffsItem buffsItem,String npLv) {
        boolean ifsameColor;
        double busterChain = 0;
        //同色比较，增加宝具卡的颜色判断
        String color1,color2,color3;
        color1 = getCardColor(cardType1,servantItem);
        color2 = getCardColor(cardType2,servantItem);
        color3 = getCardColor(cardType3,servantItem);
        if (color1.equals(color2) && color2.equals(color3)) {
            ifsameColor = true;
            if (color1.equals("b")) {
                busterChain = 0.2;
            } else {
                busterChain = 0;
            }
        } else {
            ifsameColor = false;
        }
        if (buffsItem == null) {
            buffsItem = new BuffsItem();
        }
        if (randomCor == 0.01) {
            randomCor = Math.random() * 0.2 + 0.9;
        }
        ConditionAtk conAtk = new ConditionAtk();
        conAtk.setAtk(atk);
        conAtk.setCardType1(cardType1);
        conAtk.setCardType2(cardType2);
        conAtk.setCardType3(cardType3);
        conAtk.setIfCr1(ifCr1);
        conAtk.setIfCr2(ifCr2);
        conAtk.setIfCr3(ifCr3);
        conAtk.setIfEx(ifEx);
        conAtk.setIfsameColor(ifsameColor);
        conAtk.setWeakType(weakType);
        conAtk.setTeamCor(teamCor);
        conAtk.setRandomCor(randomCor);
        conAtk.setBusterChain(busterChain);
        conAtk.setBuffsItem(buffsItem);
        conAtk.setServantItem(servantItem);
        conAtk.setNpLv(npLv);
        return conAtk;
    }

    @Override
    public ConditionTrump getConditionTrump(int atk, int hpTotal, int hpLeft, String trumpColor,
                                            int weakType, double teamCor, double randomCor, double trumpTimes,
                                            ServantItem servantItem, BuffsItem buffsItem,String npLv) {
        String class_type;
        int solid_atk;
        double arts_buff,
                buster_buff,
                quick_buff,
                atk_buff,
                special_buff,
                critical_buff;
        ConditionTrump c = new ConditionTrump();
        if (buffsItem == null) {
            buffsItem = new BuffsItem();
        }
        class_type = servantItem.getClass_type();
        arts_buff = servantItem.getArts_buff();
        buster_buff = servantItem.getBuster_buff();
        quick_buff = servantItem.getQuick_buff();
        atk_buff = servantItem.getAtk_buff();
//        special_buff = servantItem.getSpecial_buff();
        critical_buff = servantItem.getCritical_buff();
        solid_atk = servantItem.getSolid_buff();
        c.setAtk(atk);
        c.setWeakType(weakType);
        c.setClassCor(classCor(class_type));
        c.setWeakCor(weakCorForAtk(weakType,class_type));
        c.setTeamCor(teamCor);
        c.setRandomCor(randomCor);
        c.setServantItem(servantItem);
        c.setBuffsItem(buffsItem);
        c.setNpLv(npLv);
        //无宝具特攻也要写1，不能为0
        if (buffsItem.getTrumpSpecialUp() == 0) {
            c.setTrumpBuff(1);
        } else {
            c.setTrumpBuff(buffsItem.getTrumpSpecialUp() / 100);
        }
        c.setSolidBuff(buffsItem.getSolidAtk() + solid_atk);
        c.setTrumpPowerBuff(buffsItem.getTrumpUp() / 100);
        c.setAtkBuff(buffsItem.getAtkUp() / 100 + atk_buff);
        c.setSpecialBuff(buffsItem.getSpecialUp() / 100);
        //如果减防高于100%当100%计算
        double enemyDefence = 0;
        if ((buffsItem.getEnemyDefence() / 100) > 1) {
            enemyDefence = 1;
        }else{
            enemyDefence = buffsItem.getEnemyDefence() / 100;
        }
        c.setEnemyDefence(enemyDefence);
        c.setTrumpDown(buffsItem.getTrumpDown() / 100);
        switch (trumpColor) {
            case "b":
                c.setCardBuff(buffsItem.getBusterUp() / 100 + buster_buff);
                c.setCardTimes(1.5);
                break;
            case "a":
                c.setCardBuff(buffsItem.getArtsUp() / 100 + arts_buff);
                c.setCardTimes(1.0);
                break;
            case "q":
                c.setCardBuff(buffsItem.getQuickUp() / 100 + quick_buff);
                c.setCardTimes(0.8);
                break;
        }
        //宝具倍率，双子需要特殊处理
        if (servantItem.getId() == 66 || servantItem.getId() == 131){
            if (servantItem.getId() == 66) {
                trumpTimes = trumpTimes + (buffsItem.getExtraTimes() / 100) * (1 - ((double)hpLeft / (double)hpTotal));
            }
            //131 a双子
            if (servantItem.getId() == 131) {
                //弓双子额外倍率固定600%
                trumpTimes = trumpTimes + 6 * (1 - ((double)hpLeft / (double)hpTotal));
            }
            c.setHpLeft(hpLeft);
            c.setHpTotal(hpTotal);
            c.setTrumpTimes(trumpTimes);
        }else{
            //允许附加倍率吧
            if (buffsItem.getExtraTimes() != 0) {
                trumpTimes = trumpTimes + (buffsItem.getExtraTimes() / 100) ;
            }
            c.setTrumpTimes(trumpTimes);
        }
        Log.d(TAG,"trump times:" + trumpTimes);
        return c;
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

    private double weakCorForAtk(int weak_type,String class_type){
        double weakCor = 1.0;
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
        return weakCor;
    }

    //职介补正
    private double classCor(String classType) {
        String classCache = classType.toLowerCase();
        double classCor = 0;
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
        return classCor;
    }

    @Override
    public void getReady(ConditionAtk conAtk,ConditionTrump conT) {
        CommandCard c1 = new CommandCard(1,conAtk,conT);
        CommandCard c2 = new CommandCard(2,conAtk,conT);
        CommandCard c3 = new CommandCard(3,conAtk,conT);
        CommandCard c4 = new CommandCard(4,conAtk,conT);
        calcAtk(c1,conAtk);
        calcAtk(c2,conAtk);
        calcAtk(c3,conAtk);
        calcAtk(c4,conAtk);
//        mView.setResult(result);
        mView.setResult(Html.fromHtml(result));
    }

    private void calcAtk(CommandCard c,ConditionAtk conAtk) {
        double attack = 0;
        String hpStatus = "";
        String npLv = " " + conAtk.getNpLv() + "<br>";
        //减防上限是100%，超过就当是100%
        double enemyDefence = 0;
        //卡是宝具卡，条件不为null时计算宝具伤害
        if (c.cardType.equals("np") && c.conT != null) {
            ConditionTrump conT = c.conT;
            enemyDefence = conT.getEnemyDefence();
            if (1 < enemyDefence) {
                enemyDefence = 1;
            }
            if (conT.getServantItem().getId() == 66 || conT.getServantItem().getId() == 131) {
                hpStatus = new StringBuilder().append(" 总hp："+ conT.getHpTotal() + " 剩余hp：" + conT.getHpLeft() + "<br>").toString();
            }
            if (conT.getTrumpTimes() == 0) {
                attack = 0;
            }else{
                attack = conT.getAtk() * conT.getAtkCor() * (conT.getTrumpTimes() * conT.getCardTimes() * (1 + conT.getCardBuff()))
                        * conT.getClassCor() * conT.getWeakCor() * conT.getTeamCor() * conT.getRandomCor() *
                        (1 + conT.getAtkBuff() + enemyDefence) * (1 + conT.getSpecialBuff() - conT.getSpecialDefence() + conT.getTrumpPowerBuff() - conT.getTrumpDown())
                        * conT.getTrumpBuff() + (conT.getSolidBuff() - conT.getSolidDefence());
            }
        }else{
            enemyDefence = c.enemyDefence;
            if (1 < enemyDefence) {
                enemyDefence = 1;
            }
            attack = c.atk * c.atkCor * (c.atkTimes * c.positionBuff * (1 + c.cardBuff) + c.firstCardBuff) *
                    c.classCor * c.weakCor * c.teamCor * c.randomCor * (1 + c.atkBuff + enemyDefence) *
                    (1 + c.specialBuff - c.specialDefence + c.criticalBuff) * c.criticalCor * c.exReward
                    + (c.solidBuff - c.solidDefence) + c.atk * c.busterChain;
        }
        int attackInt = (int) Math.floor(attack);
        String[] con = getConditions(conAtk);
        if (result.length() < 1) {
            ServantItem sItem = conAtk.getServantItem();
            result = new StringBuilder()
                    .append(sItem.getName() + " " + sItem.getClass_type() + "<br>" + "总atk：")//名称
                    .append(conAtk.getAtk())
                    .append(npLv)
                    .append(hpStatus
                            + con[0] + ","
                            + con[1] + ","
                            + con[2] + "<br>"
                            + getExtraBuffs(conAtk) + "<br>")//atk情况
                    .append(BaseUtils.getCardTypeWithColour(c.cardType))
                    .append(c.cardPosition)
                    .append("号位")
                    .append(c.ifCritical == false ? "" : "暴击")
                    .append("的伤害为")
                    .append(attackInt).toString();
            overallAttack = attackInt;
        } else {
            if (c.cardPosition == 1) {
                result = new StringBuilder()
                        .append(result)
                        .append(con[0])
                        .append("," + con[1]).append("," + con[2] + "<br>")//条件
                        .append("总atk：")
                        .append(conAtk.getAtk())
                        .append(npLv)
                        .append(hpStatus)//atk情况
                        .append(getExtraBuffs(conAtk) + "<br>")//buff
                        .append(BaseUtils.getCardTypeWithColour(c.cardType))
                        .append(c.cardPosition)
                        .append("号位")
                        .append(c.ifCritical == false ? "" : "暴击")
                        .append("的伤害为")
                        .append(attackInt).toString();
                overallAttack += attackInt;
            }else{
                result = new StringBuilder().append(result).append("<br>")
                        .append(BaseUtils.getCardTypeWithColour(c.cardType))
                        .append(c.cardPosition).append("号位").append(c.ifCritical == false ? "" : "暴击")
                        .append("的伤害为").append(attackInt).toString();
                overallAttack += attackInt;
            }
            if (c.cardPosition == 4) {
                result = new StringBuilder().append(result).append("<br>合计----->").append(overallAttack)
                        .append("<p>" + ctx.getString(R.string.fgocalc_divider) + "<p>").toString();
                overallAttack = 0;
            }
        }
    }

    @Override
    public void clean() {
        result = "";
    }

    //获取环境条件情况
    private String[] getConditions(ConditionAtk conAtk){
        String[] a = new String[3];
        switch (conAtk.getWeakType()) {
            case 1:
                a[0] = "职阶无克";
                break;
            case 2:
                a[0] = "职阶克制";
                break;
            case 3:
                a[0] = "职阶被克";
                break;
            case 4:
                a[0] = "职阶克制狂";
                break;
        }
        if (conAtk.getTeamCor() == 1.0) {
            a[1] = "阵营无克";
        }else if (conAtk.getTeamCor() == 1.1) {
            a[1] = "阵营克制";
        }else if (conAtk.getTeamCor() == 0.9) {
            a[1] = "阵营被克";
        }
        if (conAtk.getRandomCor() == getRan(Constant.TYPE_MIN)) {
            a[2] = "最小乱数";
        }else if (conAtk.getRandomCor() == getRan(Constant.TYPE_MAX)) {
            a[2] = "最大乱数";
        }else if (conAtk.getRandomCor() == getRan(Constant.TYPE_AVERAGE)) {
            a[2] = "平均乱数";
        }else{
            a[2] = "随机乱数";
        }
        return a;
    }

    //获取额外buff情况
    private String getExtraBuffs(ConditionAtk conAtk){
        BuffsItem item = conAtk.getBuffsItem();
        if (item == null) {
            return "无额外buff";
        }else {
            String re = "";
            String[] buffs = ctx.getResources().getStringArray(R.array.buffs);
            for (int i = 0;i < buffs.length;i ++){
                Class cls = item.getClass();
                String[] buff = buffs[i].split(",");
                // 选择要包裹的代码块，然后按下ctrl + alt + t ，快速生成try catch等
                try {
                    if (buff[2].equals("d")) {
                        Method method = cls.getDeclaredMethod("get" + buff[3]);
                        double value = (double)method.invoke(item);
                        if (value != 0) {
                            re += new StringBuilder().append(" " + buff[1] + ":").append(value).append("%").toString();
                        }
                    }else{
                        Method method = cls.getDeclaredMethod("get" + buff[3]);
                        int value = (int)method.invoke(item);
                        if (value != 0) {
                            re += new StringBuilder().append(" " + buff[1] + ":").append(value).toString();
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return TextUtils.isEmpty(re) ? "无额外buff" : re;
        }
    }
}
