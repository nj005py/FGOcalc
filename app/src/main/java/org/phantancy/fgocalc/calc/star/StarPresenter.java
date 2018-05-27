package org.phantancy.fgocalc.calc.star;

import android.content.Context;
import android.text.TextUtils;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.CommandCard;
import org.phantancy.fgocalc.item.ConditionStar;
import org.phantancy.fgocalc.item.ServantItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

/**
 * Created by HATTER on 2017/11/7.
 */

public class StarPresenter implements StarContract.Presenter {
    private StarContract.View mView;
    private Context ctx;
    private String result = "";
    private double overallMax,overallMin,overAllAverage;

    public StarPresenter(StarContract.View mView, Context ctx) {
        this.mView = mView;
        this.ctx = ctx;
        mView.setPresenter(this);
    }

    @Override
    public void clear() {
        result = "";
    }

    @Override
    public double getRan(int type) {
        double ran = -0.1;
        switch (type) {
            case Constant.TYPE_MIN:
                ran = -0.1;
                break;
            case Constant.TYPE_MAX:
                ran = 0.2;
                break;
            case Constant.TYPE_AVERAGE:
                ran = 0.05;
                break;
            case Constant.TYPE_RANDOM:
                ran = Math.random() * 0.3 + (-0.1);
                break;
        }
        return ran;
    }

    @Override
    public void start() {

    }

    @Override
    public ConditionStar getCondition(String cardType1, String cardType2, String cardType3,
                                      boolean ifCr1, boolean ifCr2, boolean ifCr3,
                                      boolean ifok1, boolean ifok2, boolean ifok3,
                                      double randomCor, ServantItem servantItem, BuffsItem buffsItem,
                                      int enemyAmount) {
        if (buffsItem == null) {
            buffsItem = new BuffsItem();
        }
        ConditionStar conS = new ConditionStar();
        conS.setCardType1(cardType1);
        conS.setCardType2(cardType2);
        conS.setCardType3(cardType3);
        conS.setIfCr1(ifCr1);
        conS.setIfCr2(ifCr2);
        conS.setIfCr3(ifCr3);
        conS.setIfok1(ifok1);
        conS.setIfok2(ifok2);
        conS.setIfok3(ifok3);
        conS.setRandomCor(randomCor);
        conS.setServantItem(servantItem);
        conS.setBuffsItem(buffsItem);
        conS.setEnemyAmount(enemyAmount);
        return conS;
    }

    @Override
    public void getReady(ConditionStar conS) {
        CommandCard c1 = new CommandCard(1,conS);
        CommandCard c2 = new CommandCard(2,conS);
        CommandCard c3 = new CommandCard(3,conS);
        CommandCard c4 = new CommandCard(4,conS);
        calcStar(c1,conS);
        calcStar(c2,conS);
        calcStar(c3,conS);
        calcStar(c4,conS);
        mView.setResult(result);
    }

    //指令卡 [星星发生率+卡牌补正×(1+卡牌Buff)+首位加成+星星发生率Buff+暴击补正-敌方星星发生Buff-敌补正]×Overkill乘算补正+Overkill加算补正
    // 宝具 [星星发生率+卡牌星星发生率×(1+卡牌Buff)+星星发生率Buff-敌方星星发生Buff-敌补正]×Overkill乘算补正+Overkill加算补正
    private void calcStar(CommandCard c,ConditionStar conS){
        double starOccur = 0;
        if (c.cardType.equals("np") && conS != null) {
            ServantItem item = conS.getServantItem();
            double cardStarGeneration = 0;
            switch (item.getTrump_color()) {
                case "b":
                    cardStarGeneration = 0.1;
                    break;
                case "a":
                    cardStarGeneration = 0;
                    break;
                case "q":
                    cardStarGeneration = 0.8;
                    break;
            }
            starOccur = ((c.starOccur + cardStarGeneration * (1 + c.cardBuff) + c.starOccurBuff - c.enemyStarBuff - c.randomCor)
                    * c.starOverkillMuti + c.starOverkillPlus) * conS.getEnemyAmount();
        }else{
            starOccur = (c.starOccur + c.cardStarCor * (1 + c.cardBuff) + c.starFirstCardBuff + c.starOccurBuff - c.enemyStarBuff - c.randomCor)
                    * c.starOverkillMuti + c.starOverkillPlus;
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        String[] starInfo = getStarNum(starOccur,c);
        double max = Double.valueOf(starInfo[1]);
        double min = Double.valueOf(starInfo[2]);
        double average = Double.valueOf(starInfo[3]);
        if (result.length() < 1) {
            ServantItem sItem = conS.getServantItem();
            result = new StringBuilder().append(sItem.getName()).append(" " + sItem.getClass_type() + "\n")//从者名称+职阶
                    .append(getConditions(conS) + " 宝具打击" + conS.getEnemyAmount() + "个敌人\n")//条件
                    .append(getExtraBuffs(conS) + "\n")//buff
                    .append(c.cardType).append("卡在").append(c.cardPosition).append("号位")
                    .append(c.ifCritical == false ? "" : " 暴击")
                    .append(c.ifOverkill ==false ? "" : " overkill")
                    .append(starInfo[0]).toString();
            overallMax += max;
            overallMin += min;
            overAllAverage += average;
        }else{
            if (c.cardPosition == 1) {
                result = new StringBuilder().append(result)
                        .append(getConditions(conS) + " 宝具打击" + conS.getEnemyAmount() + "个敌人\n")//条件
                        .append(getExtraBuffs(conS) + "\n")//buff
                        .append(c.cardType).append("卡在").append(c.cardPosition).append("号位")
                        .append(c.ifCritical == false ? "" : " 暴击")
                        .append(c.ifOverkill ==false ? "" : " overkill")
                        .append(starInfo[0]).toString();
                overallMax += max;
                overallMin += min;
                overAllAverage += average;
            }else{
                result = new StringBuilder().append(result).append("\n")
                        .append(c.cardType).append("卡在").append(c.cardPosition).append("号位")
                        .append(c.ifCritical == false ? "" : " 暴击")
                        .append(c.ifOverkill ==false ? "" : " overkill")
                        .append(starInfo[0]).toString();
                overallMax += max;
                overallMin += min;
                overAllAverage += average;
            }
            if (c.cardPosition == 4) {
                result = new StringBuilder().append(result).append("\n合计---->可获得").append(overallMin).append("-")
                        .append(overallMax).append("颗星 ")
                        .append("平均").append(df.format(overAllAverage)).append("颗星").append(ctx.getString(R.string.fgocalc_divider)).toString();
                overallMax = 0;
                overallMin = 0;
                overAllAverage = 0;
            }
        }
    }

    //获取环境条件情况
    private String getConditions(ConditionStar conS){
        String a = "";
        if (conS.getRandomCor() == getRan(Constant.TYPE_MIN)) {
            a = "最小随机乱数";
        }else if (conS.getRandomCor() == getRan(Constant.TYPE_MAX)) {
            a = "最大随机数";
        }else if (conS.getRandomCor() == getRan(Constant.TYPE_AVERAGE)) {
            a = "平均随机数";
        }else{
            a = "随机乱数";
        }
        return a;
    }

    //获取额外buff情况
    private String getExtraBuffs(ConditionStar conS){
        BuffsItem item = conS.getBuffsItem();
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

    private String[] getStarNum(double starOccur,CommandCard c){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.##");
        String max;
        String min;
        String average;
        String[] result = new String[4];
        int hits = 0;
        if (c.cardType.equals("np")) {
            hits = c.getConS().getServantItem().getNp_hit();
        }else{
            hits = c.hits;
        }
        if (starOccur > 0) {
            if (starOccur > 0 && starOccur < 1) {
                max = String.valueOf(hits);
                min = "0";
                average = df.format(starOccur * hits);
                sb.append("可获得0-").append(hits).append("颗星 ")
                        .append("平均").append(average).append("颗星 ");
                result[0] = sb.toString();
                result[1] = max;
                result[2] = min;
                result[3] = average;
            }else if (starOccur > 1) {
                max = df.format(Math.ceil(starOccur) * hits);
                min = df.format(Math.floor(starOccur) * hits);
                average = df.format(starOccur * hits);
                sb.append("可获得").append(Math.floor(starOccur) * hits).append("-")
                        .append(Math.ceil(starOccur) * hits).append("颗星 ")
                        .append("平均").append(average).append("颗星 ");
                result[0] = sb.toString();
                result[1] = max;
                result[2] = min;
                result[3] = average;
            }
        }else{
            result[0] = "可获得0颗星";
            result[1] = "0";
            result[2] = "0";
            result[3] = "0";
        }
        return result;
    }
}
