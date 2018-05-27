package org.phantancy.fgocalc.calc.np;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.CommandCard;
import org.phantancy.fgocalc.item.ConditionNp;
import org.phantancy.fgocalc.item.ServantItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by HATTER on 2017/11/7.
 */

public class NpPresenter implements NpContract.Presenter {
    @NonNull
    private NpContract.View mView;
    @NonNull
    private Context ctx;
    private String result = "";
    private int overAllNp;

    public NpPresenter(@NonNull NpContract.View mView, @NonNull Context ctx) {
        this.mView = mView;
        this.ctx = ctx;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public double getRan(int type) {
        double ran = 0.8;
        switch (type) {
            case Constant.TYPE_MIN:
                ran = 0.8;
                break;
            case Constant.TYPE_MAX:
                ran = 1.23;
                break;
            case Constant.TYPE_AVERAGE:
                ran = 1.015;
                break;
            case Constant.TYPE_RANDOM:
                ran = Math.random() * 0.52 + 0.8;
                break;
        }
        return ran;
    }

    @Override
    public ConditionNp getCondition(String cardType1, String cardType2, String cardType3,
                                    boolean ifCr1, boolean ifCr2, boolean ifCr3,
                                    boolean ifok1, boolean ifok2, boolean ifok3,
                                    double randomCor, ServantItem servantItem, BuffsItem buffsItem,
                                    int enemyAmount) {
        if (buffsItem == null) {
            buffsItem = new BuffsItem();
        }
        ConditionNp conNp = new ConditionNp();
        conNp.setCardType1(cardType1);
        conNp.setCardType2(cardType2);
        conNp.setCardType3(cardType3);
        conNp.setIfCr1(ifCr1);
        conNp.setIfCr2(ifCr2);
        conNp.setIfCr3(ifCr3);
        conNp.setIfok1(ifok1);
        conNp.setIfok2(ifok2);
        conNp.setIfok3(ifok3);
        conNp.setRandomCor(randomCor);
        conNp.setServantItem(servantItem);
        conNp.setBuffsItem(buffsItem);
        conNp.setEnemyAmount(enemyAmount);
        return conNp;
    }

    @Override
    public void getReady(ConditionNp conNp) {
        CommandCard c1 = new CommandCard(1,conNp);
        CommandCard c2 = new CommandCard(2,conNp);
        CommandCard c3 = new CommandCard(3,conNp);
        CommandCard c4 = new CommandCard(4,conNp);
        calcNp(c1,conNp);
        calcNp(c2,conNp);
        calcNp(c3,conNp);
        calcNp(c4,conNp);
        mView.setResult(result);
    }

    @Override
    public void clean() {
        result = "";
    }

    /**
     * 每Hit主动NP获得量×攻击Hit数×［卡牌NP倍率×位置加成×（1+卡牌Buff）＋首位加成］×
     * （1+NP Buff）×暴击补正×Overkill补正×敌补正
     */
    private void calcNp(CommandCard c,ConditionNp conNp){
        double np = 0;
        if (c.cardType.equals("np") && conNp != null) {
            ServantItem item = conNp.getServantItem();
            double npTimes = 0;
            switch (item.getTrump_color()) {
                case "b":
                    npTimes = 0;
                    break;
                case "a":
                    npTimes = 3;
                    break;
                case "q":
                    npTimes = 1;
                    break;
            }
            np = item.getTrump_na() * 100 * item.getNp_hit() * (npTimes * (1 + c.cardBuff)) *
                    (1 + c.npBuff)  * c.overkill * c.randomCor * conNp.getEnemyAmount();
        }else{
            np = c.na * 100 * c.hits * (c.npTimes * c.npPositionBuff * (1 + c.cardBuff) + c.npFirstCardBuff) *
                    (1 + c.npBuff) * c.criticalCor * c.overkill * c.randomCor;
        }
        int npInt = (int) Math.rint(np);
        if (c.cardType.equals("a") && npInt == 0) {
            npInt = 1;
        }
        if (result.length() < 1) {
            ServantItem sItem = conNp.getServantItem();
            result = new StringBuilder().append(sItem.getName()).append(" " + sItem.getClass_type() + "\n")//从者名称+职阶
                    .append(getConditions(conNp) + " 宝具打击" + conNp.getEnemyAmount() + "个敌人\n")//条件
                    .append(getExtraBuffs(conNp) + "\n")//buff
                    .append(c.cardType).append("卡在").append(c.cardPosition).append("号位")
                    .append(c.ifCritical == false ? "" : " 暴击")
                    .append(c.ifOverkill ==false ? "" : " overkill")
                    .append("的np获取量为").append(npInt).append("%").toString();
//            result = c.cType + "卡在" + c.cPosition + " 号位的np获取量为" + npInt;
            overAllNp = npInt;
        } else {
            if (c.cardPosition == 1) {
                result = new StringBuilder().append(result)
                        .append(getConditions(conNp) + " 宝具打击" + conNp.getEnemyAmount() + "个敌人\n")//条件
                        .append(getExtraBuffs(conNp) + "\n")//buff
                        .append(c.cardType).append("卡在").append(c.cardPosition).append("号位")
                        .append(c.ifCritical == false ? "" : " 暴击")
                        .append(c.ifOverkill ==false ? "" : " overkill")
                        .append("的np获取量为").append(npInt).append("%").toString();
                overAllNp += npInt;
            }else{
                result = new StringBuilder().append(result).append("\n")
                        .append(c.cardType).append("卡在").append(c.cardPosition).append("号位")
                        .append(c.ifCritical == false ? "" : " 暴击")
                        .append(c.ifOverkill ==false ? "" : " overkill")
                        .append("的np获取量为").append(npInt).append("%").toString();
                overAllNp += npInt;
            }
            if (c.cardPosition == 4) {
                result = new StringBuilder().append(result).append("\n合计----->").append(overAllNp).append("%").append(ctx.getString(R.string.fgocalc_divider)).toString();
                overAllNp = 0;
            }
//            result = result + "\n" + c.cType + "卡在" + c.cPosition + " 号位的np获取量为" + npInt;
//            overAllNp += npInt;
        }
    }

    //获取环境条件情况
    private String getConditions(ConditionNp conNp){
        String a = "";
        if (conNp.getRandomCor() == getRan(Constant.TYPE_MIN)) {
            a = "最小随机数";
        }else if (conNp.getRandomCor() == getRan(Constant.TYPE_MAX)) {
            a = "最大随机数";
        }else if (conNp.getRandomCor() == getRan(Constant.TYPE_AVERAGE)) {
            a = "平均随机数";
        }else{
            a = "随机乱数";
        }
        return a;
    }

    //获取额外buff情况
    private String getExtraBuffs(ConditionNp conNp){
        BuffsItem item = conNp.getBuffsItem();
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
