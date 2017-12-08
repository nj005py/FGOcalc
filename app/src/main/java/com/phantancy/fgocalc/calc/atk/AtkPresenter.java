package com.phantancy.fgocalc.calc.atk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.common.Constant;
import com.phantancy.fgocalc.item.BuffItem;
import com.phantancy.fgocalc.item.BuffsItem;
import com.phantancy.fgocalc.item.CommandCard;
import com.phantancy.fgocalc.item.ConditionAtk;
import com.phantancy.fgocalc.item.ServantItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by HATTER on 2017/11/6.
 */

public class AtkPresenter implements AtkContract.Presenter {
    @NonNull
    private final AtkContract.View mView;
    private String result = "";
    private int overallAttack;
    private Context ctx;

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
    public ConditionAtk getCondition(int atk, String cardType1, String cardType2, String cardType3,
                                     boolean ifEx, boolean ifCr1, boolean ifCr2, boolean ifCr3,
                                     int weakType, double teamCor, double randomCor,
                                     ServantItem servantItem, BuffsItem buffsItem) {
        boolean ifsameColor;
        double busterChain = 0;
        if (cardType1.equals(cardType2) && cardType2.equals(cardType3)) {
            ifsameColor = true;
            if (cardType1.equals("b")) {
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
        return conAtk;
    }

    @Override
    public void getReady(ConditionAtk conAtk) {
        CommandCard c1 = new CommandCard(1,conAtk);
        CommandCard c2 = new CommandCard(2,conAtk);
        CommandCard c3 = new CommandCard(3,conAtk);
        CommandCard c4 = new CommandCard(4,conAtk);
        calcAtk(c1,conAtk);
        calcAtk(c2,conAtk);
        calcAtk(c3,conAtk);
        calcAtk(c4,conAtk);
        mView.setResult(result);
    }

    private void calcAtk(CommandCard c,ConditionAtk conAtk) {
        double attack = c.atk * c.atkCor * (c.atkTimes * c.positionBuff * (1 + c.cardBuff) + c.firstCardBuff) *
                c.classCor * c.weakCor * c.teamCor * c.randomCor * (1 + c.atkBuff + c.enemyDefence) *
                (1 + c.specialBuff - c.specialDefence + c.criticalBuff) * c.criticalCor * c.exReward
                + (c.solidBuff - c.solidDefence) + c.atk * c.busterChain;
        int attackInt = (int) Math.floor(attack);
        String[] con = getConditions(conAtk);
        if (result.length() < 1) {
            ServantItem sItem = conAtk.getServantItem();
            result = new StringBuilder().append(sItem.getName()).append(" " + sItem.getClass_type() + "\n")//从者名称+职阶
                    .append(con[0]).append("," + con[1]).append("," + con[2] + "\n")//条件
                    .append(getExtraBuffs(conAtk) + "\n")//buff
                    .append(c.cardType).append("卡在").append(c.cardPosition).append("号位").append(c.ifCritical == false ? "" : "暴击")
                    .append("的伤害为").append(attackInt).toString();
//            result = getExtraBuffs(conAtk) + "\n" + c.cardType + "卡在" + c.cardPosition + "号位的伤害为" + attackInt;
            overallAttack = attackInt;
        } else {
            if (c.cardPosition == 1) {
                result = new StringBuilder().append(result)
                        .append(con[0]).append("," + con[1]).append("," + con[2] + "\n")//条件
                        .append(getExtraBuffs(conAtk) + "\n")//buff
                        .append(c.cardType).append("卡在").append(c.cardPosition).append("号位").append(c.ifCritical == false ? "" : "暴击")
                        .append("的伤害为").append(attackInt).toString();
//                result += getExtraBuffs(conAtk) + "\n" + c.cardType + "卡在" + c.cardPosition + "号位的伤害为" + attackInt;
                overallAttack += attackInt;
            }else{
                result = new StringBuilder().append(result).append("\n").append(c.cardType)
                        .append("卡在").append(c.cardPosition).append("号位").append(c.ifCritical == false ? "" : "暴击")
                        .append("的伤害为").append(attackInt).toString();
//                result = result + "\n" + c.cardType + "卡在" + c.cardPosition + "号位的伤害为" + attackInt;
                overallAttack += attackInt;
            }
            if (c.cardPosition == 4) {
                result = new StringBuilder().append(result).append("\n合计----->").append(overallAttack).append("\n").toString();
//                result = result + "\n" + "合计----->" + overallAttack + "\n";
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
