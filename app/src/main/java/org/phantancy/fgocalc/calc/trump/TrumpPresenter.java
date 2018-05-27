package org.phantancy.fgocalc.calc.trump;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ConditionTrump;
import org.phantancy.fgocalc.item.ServantItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by HATTER on 2017/11/7.
 */

public class TrumpPresenter implements TrumpContract.Presenter {
    @NonNull
    private TrumpContract.View mView;
    @NonNull
    private Context ctx;
    private String result = "";
    private final String TAG = getClass().getSimpleName();

    public TrumpPresenter(@NonNull TrumpContract.View mView, @NonNull Context ctx) {
        this.mView = mView;
        this.ctx = ctx;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
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
                ran = Math.random() * 0.2 + 0.9;
                break;
        }
        return ran;
    }

    @Override
    public void clear() {
        result = "";
    }

    @Override
    public ConditionTrump getConditionTrump(int atk, int hpTotal, int hpLeft, String trumpColor,
                                  int weakType, double teamCor, double randomCor, double trumpTimes,
                                  ServantItem servantItem, BuffsItem buffsItem) {
        String class_type;
        int solid_atk;
        double arts_buff,
                buster_buff,
                quick_buff,
                atk_buff,
//                special_buff,
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
                trumpTimes = trumpTimes + ((buffsItem.getExtraTimes() / 100) * (1 - ((double)hpLeft / (double) hpTotal)));
            }
            //131 a双子
            if (servantItem.getId() == 131) {
                //弓双子额外倍率固定600%
                trumpTimes = trumpTimes + (6 * (1 - ((double)hpLeft / (double) hpTotal)));
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
    public void getReady(ConditionTrump conTrump) {
        calcTrump(conTrump);
    }

    private void calcTrump(ConditionTrump conT){
        int overallAttack;
        double attack = 0;
        if (conT.getTrumpTimes() == 0) {
            attack = 0;
        }else{
            attack = conT.getAtk() * conT.getAtkCor() * (conT.getTrumpTimes() * conT.getCardTimes() * (1 + conT.getCardBuff()))
                    * conT.getClassCor() * conT.getWeakCor() * conT.getTeamCor() * conT.getRandomCor() *
                    (1 + conT.getAtkBuff() + conT.getEnemyDefence()) * (1 + conT.getSpecialBuff() - conT.getSpecialDefence() + conT.getTrumpPowerBuff() - conT.getTrumpDown())
                    * conT.getTrumpBuff() + (conT.getSolidBuff() - conT.getSolidDefence());
        }
        int attackInt = (int) Math.floor(attack);
        overallAttack = attackInt;
        String[] con = getConditions(conT);
        String hpStatus = "";
        if (conT.getServantItem().getId() == 66 || conT.getServantItem().getId() == 131) {
            hpStatus = new StringBuilder().append(" 总hp："+ conT.getHpTotal() + " 剩余hp：" + conT.getHpLeft()).toString();
        }
        if (TextUtils.isEmpty(result)) {
            ServantItem sItem = conT.getServantItem();
            result = new StringBuilder().append(sItem.getName()).append(" " + sItem.getClass_type() + "\n")//从者名称+职阶
                    .append("总atk：").append(conT.getAtk()).append(hpStatus + "\n")//atk情况
                    .append(con[0]).append("," + con[1]).append("," + con[2] + "\n")//条件
                    .append(getExtraBuffs(conT) + "\n")//buff
                    .append("宝具伤害----->").append(overallAttack).toString();
        } else {
            result = new StringBuilder().append(result)
                    .append(ctx.getString(R.string.fgocalc_divider))
                    .append("总atk：").append(conT.getAtk()).append(hpStatus + "\n")//atk情况
                    .append(con[0]).append("," + con[1]).append("," + con[2] + "\n")//条件
                    .append(getExtraBuffs(conT) + "\n")//buff
                    .append("宝具伤害----->").append(overallAttack).toString();
        }
        mView.setResult(result);
    }

    //获取环境条件情况
    private String[] getConditions(ConditionTrump conT){
        String[] a = new String[3];
        switch (conT.getWeakType()) {
            case 1:
                a[0] = "职阶无克";
                break;
            case 2:
                a[0] = "职阶克制";
                break;
            case 3:
                a[0] = "职阶被克";
                break;
        }
        if (conT.getTeamCor() == 1.0) {
            a[1] = "阵营无克";
        }else if (conT.getTeamCor() == 1.1) {
            a[1] = "阵营克制";
        }else if (conT.getTeamCor() == 0.9) {
            a[1] = "阵营被克";
        }
        if (conT.getRandomCor() == getRan(Constant.TYPE_MIN)) {
            a[2] = "最小乱数";
        }else if (conT.getRandomCor() == getRan(Constant.TYPE_MAX)) {
            a[2] = "最大乱数";
        }else if (conT.getRandomCor() == getRan(Constant.TYPE_AVERAGE)) {
            a[2] = "平均乱数";
        }else{
            a[2] = "随机乱数";
        }
        return a;
    }

    //获取额外buff情况
    private String getExtraBuffs(ConditionTrump conT){
        BuffsItem item = conT.getBuffsItem();
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
