package org.phantancy.fgocalc.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "servants")
public class ServantEntity implements Parcelable {
    /**
     * 基础信息
     */
    @Ignore
    public String avatarUrl;
    @Ignore
    public int avatarRes = -1;
    //从者id
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    //名称 中文
    @ColumnInfo(name = "name")
    public String name;
    //昵称
    @ColumnInfo(name = "nickname")
    public String nickname;
    //职阶
    @ColumnInfo(name = "class_type")
    public String classType;
    //星
    @ColumnInfo(name = "star")
    public int star;
    //阵营（天地人星兽）
    @ColumnInfo(name = "attribute")
    public String attribute;
    //特性
    @ColumnInfo(name = "traits")
    public String traits;
    //属性
    @ColumnInfo(name = "alignments")
    public String alignments;
    //最高等级
    @ColumnInfo(name = "reward_lv")
    @Nullable
    public int rewardLv;
    //成长类型
    @ColumnInfo(name = "exp_type")
    @Nullable
    public int expType;
    //产星率
    @ColumnInfo(name = "star_generation")
    @Nullable
    public double starGeneration;

    /**
     * atk hp信息
     */
    //默认hp（满级）
    
    @ColumnInfo(name = "hp_default")
    public int hpDefault;
    //默认atk（满级）
    @ColumnInfo(name = "atk_default")
    public int atkDefault;
    //基础hp（1级）
    @ColumnInfo(name = "hp_base")
    public int hpBase;
    //基础atk（1级）
    @ColumnInfo(name = "atk_base")
    public int atkBase;
    //hp_max 100级满芙芙
    @ColumnInfo(name = "hp_max")
    public int hpMax;
    //atk_max 100级满芙芙
    @ColumnInfo(name = "atk_max")
    public int atkMax;
    //class_mod职阶系数
    @ColumnInfo(name = "class_mod")
    public double classMod;
    //hp_default_mod
    @ColumnInfo(name = "hp_default_mod")
    public double hpDefaultMod;
    //atk_default_mod
    @ColumnInfo(name = "atk_default_mod")
    public double atkDefaultMod;

    /**
     * 指令卡
     * 按绿蓝红ex宝具顺序写
     * 配卡
     */
    //配卡
    @ColumnInfo(name = "cards")
    public String cards;
    /**
     * hit数
     */
    //绿卡hit数
    @ColumnInfo(name = "quick_hit")
    public int quickHit;
    //蓝卡hit数
    @ColumnInfo(name = "arts_hit")
    public int artsHit;
    //红卡hit数
    @ColumnInfo(name = "buster_hit")
    public int busterHit;
    //ex卡hit数
    @ColumnInfo(name = "ex_hit")
    public int exHit;
    //宝具hit数
    @ColumnInfo(name = "np_hit")
    public int npHit;
    /**
     * np获取
     */
    //绿卡主动np获取
    @ColumnInfo(name = "quick_na")
    public double quickNa;
    //蓝卡主动np获取
    @ColumnInfo(name = "arts_na")
    public double artsNa;
    //红卡主动np获取
    @ColumnInfo(name = "buster_na")
    public double busterNa;
    //ex卡主动np获取
    @ColumnInfo(name = "ex_na")
    public double exNa;
    //宝具卡主动np获取
    @ColumnInfo(name = "np_na")
    public double npNa;
    //被动np获取
    @ColumnInfo(name = "nd")
    public double nd;

    /**
     * 宝具倍率
     * 宝具信息补充
     */
    //1宝倍率
    @ColumnInfo(name = "np_lv1")
    public double npLv1;
    //2宝倍率
    @ColumnInfo(name = "np_lv2")
    public double npLv2;
    //3宝倍率
    @ColumnInfo(name = "np_lv3")
    public double np_lv3;
    //4宝倍率
    @ColumnInfo(name = "np_lv4")
    public double npLv4;
    //5宝倍率
    @ColumnInfo(name = "np_lv5")
    public double npLv5;

    //1宝倍率（宝具本）
    @ColumnInfo(name = "np_lv1_updated")
    public double npLv1Updated;
    //2宝倍率（宝具本）
    @ColumnInfo(name = "np_lv2_updated")
    public double npLv2Updated;
    //3宝倍率（宝具本）
    @ColumnInfo(name = "np_lv3_updated")
    public double npLv3Updated;
    //4宝倍率（宝具本）
    @ColumnInfo(name = "np_lv4_updated")
    public double npLv4Updated;
    //5宝倍率（宝具本）
    @ColumnInfo(name = "np_lv5_updated")
    public double npLv5Updated;

    //有无宝具本
    @ColumnInfo(name = "np_upgraded")
    public int npUpgraded;
    //宝具卡色
    @ColumnInfo(name = "np_color")
    public String npColor;
    //宝具类型（单体，群体，辅助）
    @ColumnInfo(name = "np_type")
    public String npType;

    /**
     * 被动buff
     */
    //被动绿魔放
    @ColumnInfo(name = "quick_buff")
    public double quickBuffN;
    //被动蓝魔放
    @ColumnInfo(name = "arts_buff")
    public double artsBuffN;
    //被动红魔放
    @ColumnInfo(name = "buster_buff")
    public double busterBuffN;
    //特攻buff
    @ColumnInfo(name = "special_buff")
    public double specialBuffN;
    //被动暴击buff
    @ColumnInfo(name = "critical_buff")
    public double criticalBuffN;
    //被动固定伤害buff
    @ColumnInfo(name = "self_damage")
    public double selfDamageN;
    //被动产星率
    @ColumnInfo(name = "star_generation_extra")
    public double starGenerationN;

    public ServantEntity() {
    }

    protected ServantEntity(Parcel in) {
        avatarUrl = in.readString();
        avatarRes = in.readInt();
        id = in.readInt();
        name = in.readString();
        nickname = in.readString();
        classType = in.readString();
        star = in.readInt();
        attribute = in.readString();
        traits = in.readString();
        alignments = in.readString();
        rewardLv = in.readInt();
        expType = in.readInt();
        starGeneration = in.readDouble();
        hpDefault = in.readInt();
        atkDefault = in.readInt();
        hpBase = in.readInt();
        atkBase = in.readInt();
        hpMax = in.readInt();
        atkMax = in.readInt();
        classMod = in.readDouble();
        hpDefaultMod = in.readDouble();
        atkDefaultMod = in.readDouble();
        cards = in.readString();
        quickHit = in.readInt();
        artsHit = in.readInt();
        busterHit = in.readInt();
        exHit = in.readInt();
        npHit = in.readInt();
        quickNa = in.readDouble();
        artsNa = in.readDouble();
        busterNa = in.readDouble();
        exNa = in.readDouble();
        npNa = in.readDouble();
        nd = in.readDouble();
        npLv1 = in.readDouble();
        npLv2 = in.readDouble();
        np_lv3 = in.readDouble();
        npLv4 = in.readDouble();
        npLv5 = in.readDouble();
        npLv1Updated = in.readDouble();
        npLv2Updated = in.readDouble();
        npLv3Updated = in.readDouble();
        npLv4Updated = in.readDouble();
        npLv5Updated = in.readDouble();
        npUpgraded = in.readInt();
        npColor = in.readString();
        npType = in.readString();
        quickBuffN = in.readDouble();
        artsBuffN = in.readDouble();
        busterBuffN = in.readDouble();
        specialBuffN = in.readDouble();
        criticalBuffN = in.readDouble();
        selfDamageN = in.readDouble();
        starGenerationN = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatarUrl);
        dest.writeInt(avatarRes);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeString(classType);
        dest.writeInt(star);
        dest.writeString(attribute);
        dest.writeString(traits);
        dest.writeString(alignments);
        dest.writeInt(rewardLv);
        dest.writeInt(expType);
        dest.writeDouble(starGeneration);
        dest.writeInt(hpDefault);
        dest.writeInt(atkDefault);
        dest.writeInt(hpBase);
        dest.writeInt(atkBase);
        dest.writeInt(hpMax);
        dest.writeInt(atkMax);
        dest.writeDouble(classMod);
        dest.writeDouble(hpDefaultMod);
        dest.writeDouble(atkDefaultMod);
        dest.writeString(cards);
        dest.writeInt(quickHit);
        dest.writeInt(artsHit);
        dest.writeInt(busterHit);
        dest.writeInt(exHit);
        dest.writeInt(npHit);
        dest.writeDouble(quickNa);
        dest.writeDouble(artsNa);
        dest.writeDouble(busterNa);
        dest.writeDouble(exNa);
        dest.writeDouble(npNa);
        dest.writeDouble(nd);
        dest.writeDouble(npLv1);
        dest.writeDouble(npLv2);
        dest.writeDouble(np_lv3);
        dest.writeDouble(npLv4);
        dest.writeDouble(npLv5);
        dest.writeDouble(npLv1Updated);
        dest.writeDouble(npLv2Updated);
        dest.writeDouble(npLv3Updated);
        dest.writeDouble(npLv4Updated);
        dest.writeDouble(npLv5Updated);
        dest.writeInt(npUpgraded);
        dest.writeString(npColor);
        dest.writeString(npType);
        dest.writeDouble(quickBuffN);
        dest.writeDouble(artsBuffN);
        dest.writeDouble(busterBuffN);
        dest.writeDouble(specialBuffN);
        dest.writeDouble(criticalBuffN);
        dest.writeDouble(selfDamageN);
        dest.writeDouble(starGenerationN);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServantEntity> CREATOR = new Creator<ServantEntity>() {
        @Override
        public ServantEntity createFromParcel(Parcel in) {
            return new ServantEntity(in);
        }

        @Override
        public ServantEntity[] newArray(int size) {
            return new ServantEntity[size];
        }
    };
}
