package org.phantancy.fgocalc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.collection.SimpleArrayMap;

import org.phantancy.fgocalc.common.Constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//用于计算的数据
public class CalcEntity implements Parcelable {
    //0: 默认，1: 读取
    private int source = 0;
    /**
     * 条件
     */
    //职阶克制
    private double affinityMod = 2.0;
    //阵营克制
    private double attributeMod = 1.0;
    //选择宝具
    private NoblePhantasmEntity npEntity;
    //宝具伤害倍率
    private double npDmgMultiplier = 0.0;
    //总atk
    private double atk = 0;
    //总hp
    private double hp = 0;
    //剩余hp
    private double hpLeft = 0;


    //ui数据
    //选卡类型，
    private String cardType1 = Constant.CARD_QUICK;
    private String cardType2 = Constant.CARD_QUICK;
    private String cardType3 = Constant.CARD_QUICK;
    private String cardType4 = Constant.CARD_EX;

    //1-3位置是否暴击 依据计算页
    private boolean isCritical1 = false;
    private boolean isCritical2 = false;
    private boolean isCritical3 = false;

    //1-4位置是否过量伤害 依据计算页
    private boolean isOverkill1 = false;
    private boolean isOverkill2 = false;
    private boolean isOverkill3 = false;
    private boolean isOverkill4 = false;

    //敌人
    private int enemyCount = 1;
    //np敌补正
    private double[] enemysNpMod;
    //打星敌补正
    private double[] enemysStarMod;

    //是否保存条件
    private boolean isSavedCondition = false;
    //是否保存buff
    private boolean isSavedBuff = false;

    //数据库获取
    private ServantEntity svt;
    /**
     * 加工数据
     */
    private String firstCardType = Constant.CARD_QUICK;
    private boolean isSameColor = false;
    private boolean isBusterChain = false;

    public CalcEntity() {
    }

    protected CalcEntity(Parcel in) {
        source = in.readInt();
        affinityMod = in.readDouble();
        attributeMod = in.readDouble();
        npEntity = in.readParcelable(NoblePhantasmEntity.class.getClassLoader());
        npDmgMultiplier = in.readDouble();
        atk = in.readDouble();
        hp = in.readDouble();
        hpLeft = in.readDouble();
        cardType1 = in.readString();
        cardType2 = in.readString();
        cardType3 = in.readString();
        cardType4 = in.readString();
        isCritical1 = in.readByte() != 0;
        isCritical2 = in.readByte() != 0;
        isCritical3 = in.readByte() != 0;
        isOverkill1 = in.readByte() != 0;
        isOverkill2 = in.readByte() != 0;
        isOverkill3 = in.readByte() != 0;
        isOverkill4 = in.readByte() != 0;
        enemyCount = in.readInt();
        enemysNpMod = in.createDoubleArray();
        enemysStarMod = in.createDoubleArray();
        isSavedCondition = in.readByte() != 0;
        isSavedBuff = in.readByte() != 0;
        svt = in.readParcelable(ServantEntity.class.getClassLoader());
        firstCardType = in.readString();
        isSameColor = in.readByte() != 0;
        isBusterChain = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(source);
        dest.writeDouble(affinityMod);
        dest.writeDouble(attributeMod);
        dest.writeParcelable(npEntity, flags);
        dest.writeDouble(npDmgMultiplier);
        dest.writeDouble(atk);
        dest.writeDouble(hp);
        dest.writeDouble(hpLeft);
        dest.writeString(cardType1);
        dest.writeString(cardType2);
        dest.writeString(cardType3);
        dest.writeString(cardType4);
        dest.writeByte((byte) (isCritical1 ? 1 : 0));
        dest.writeByte((byte) (isCritical2 ? 1 : 0));
        dest.writeByte((byte) (isCritical3 ? 1 : 0));
        dest.writeByte((byte) (isOverkill1 ? 1 : 0));
        dest.writeByte((byte) (isOverkill2 ? 1 : 0));
        dest.writeByte((byte) (isOverkill3 ? 1 : 0));
        dest.writeByte((byte) (isOverkill4 ? 1 : 0));
        dest.writeInt(enemyCount);
        dest.writeDoubleArray(enemysNpMod);
        dest.writeDoubleArray(enemysStarMod);
        dest.writeByte((byte) (isSavedCondition ? 1 : 0));
        dest.writeByte((byte) (isSavedBuff ? 1 : 0));
        dest.writeParcelable(svt, flags);
        dest.writeString(firstCardType);
        dest.writeByte((byte) (isSameColor ? 1 : 0));
        dest.writeByte((byte) (isBusterChain ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CalcEntity> CREATOR = new Creator<CalcEntity>() {
        @Override
        public CalcEntity createFromParcel(Parcel in) {
            return new CalcEntity(in);
        }

        @Override
        public CalcEntity[] newArray(int size) {
            return new CalcEntity[size];
        }
    };

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public double[] getEnemysStarMod() {
        return enemysStarMod;
    }

    public void setEnemysStarMod(double[] enemysStarMod) {
        this.enemysStarMod = enemysStarMod;
    }

    //输入buff数据
    private SimpleArrayMap<String,Double> buffMap;

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public double getNpDmgMultiplier() {
        return npDmgMultiplier;
    }

    public void setNpDmgMultiplier(double npDmgMultiplier) {
        this.npDmgMultiplier = npDmgMultiplier;
    }

    public NoblePhantasmEntity getNpEntity() {
        return npEntity;
    }

    public void setNpEntity(NoblePhantasmEntity npEntity) {
        this.npEntity = npEntity;
    }

    public boolean isSavedCondition() {
        return isSavedCondition;
    }

    public void setSavedCondition(boolean savedCondition) {
        isSavedCondition = savedCondition;
    }

    public boolean isSavedBuff() {
        return isSavedBuff;
    }

    public void setSavedBuff(boolean savedBuff) {
        isSavedBuff = savedBuff;
    }

    public SimpleArrayMap<String, Double> getBuffMap() {
        if (buffMap == null) {
            buffMap = new SimpleArrayMap<>();
        }
        return buffMap;
    }

    public void setBuffMap(SimpleArrayMap<String, Double> buffMap) {
        this.buffMap = buffMap;
    }

    public boolean isOverkill4() {
        return isOverkill4;
    }

    public void setOverkill4(boolean overkill4) {
        isOverkill4 = overkill4;
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

    public String getCardType4() {
        return cardType4;
    }

    public void setCardType4(String cardType4) {
        this.cardType4 = cardType4;
    }

    public boolean isCritical1() {
        return isCritical1;
    }

    public void setCritical1(boolean critical1) {
        isCritical1 = critical1;
    }

    public boolean isCritical2() {
        return isCritical2;
    }

    public void setCritical2(boolean critical2) {
        isCritical2 = critical2;
    }

    public boolean isCritical3() {
        return isCritical3;
    }

    public void setCritical3(boolean critical3) {
        isCritical3 = critical3;
    }

    public boolean isOverkill1() {
        return isOverkill1;
    }

    public void setOverkill1(boolean overkill1) {
        isOverkill1 = overkill1;
    }

    public boolean isOverkill2() {
        return isOverkill2;
    }

    public void setOverkill2(boolean overkill2) {
        isOverkill2 = overkill2;
    }

    public boolean isOverkill3() {
        return isOverkill3;
    }

    public void setOverkill3(boolean overkill3) {
        isOverkill3 = overkill3;
    }

    public double getAtk() {
        return atk;
    }

    public void setAtk(double atk) {
        this.atk = atk;
    }


    public double getAffinityMod() {
        return affinityMod;
    }

    public void setAffinityMod(double affinityMod) {
        this.affinityMod = affinityMod;
    }

    public double getAttributeMod() {
        return attributeMod;
    }

    public void setAttributeMod(double attributeMod) {
        this.attributeMod = attributeMod;
    }

    public ServantEntity getSvt() {
        return svt;
    }

    public void setSvt(ServantEntity svt) {
        this.svt = svt;
    }

    public String getFirstCardType() {
        return firstCardType;
    }

    public void setFirstCardType(String firstCardType) {
        this.firstCardType = firstCardType;
    }

    public boolean isSameColor() {
        return isSameColor;
    }

    public void setSameColor(boolean sameColor) {
        isSameColor = sameColor;
    }

    public boolean isBusterChain() {
        return isBusterChain;
    }

    public void setBusterChain(boolean busterChain) {
        isBusterChain = busterChain;
    }

    public double[] getEnemysNpMod() {
        return enemysNpMod;
    }

    public void setEnemysNpMod(double[] enemysNpMod) {
        this.enemysNpMod = enemysNpMod;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getHpLeft() {
        return hpLeft;
    }

    public void setHpLeft(double hpLeft) {
        this.hpLeft = hpLeft;
    }
}
