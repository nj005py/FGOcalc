package org.phantancy.fgocalc.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * 计算条件页View Object
 * 保存UI数据信息
 */
data class CalcConditionVO(
        //职阶相性
        var affinityPosition: Int = 0,
        //阵营相性
        var attributePosition: Int = 0,
        //宝具选择
        var npSelectPosition: Int = 0,
        //宝具lv
        var npLvPosition: Int = 0,
        //芙芙atk
        var fouAtkPosition: Int = 0,
        //礼装atk
        var essenceAtkPosition: Int = 0,
        //等级
        var servantLv: Int = 0,
        //atk
        var atk: String? = "0",
        //总hp
        var hp: String? = "0",
        //剩余hp
        var hpLeft: String? = "0",
        //敌人数量
        var enemyCountPosition: Int = 0,
        //敌方1
        var enemyClass1Position: Int = 0,
        //敌方2
        var enemyClass2Position: Int = 0,
        //敌方3
        var enemyClass3Position: Int = 0
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(affinityPosition)
        parcel.writeInt(attributePosition)
        parcel.writeInt(npSelectPosition)
        parcel.writeInt(npLvPosition)
        parcel.writeInt(fouAtkPosition)
        parcel.writeInt(essenceAtkPosition)
        parcel.writeInt(servantLv)
        parcel.writeString(atk)
        parcel.writeString(hp)
        parcel.writeString(hpLeft)
        parcel.writeInt(enemyCountPosition)
        parcel.writeInt(enemyClass1Position)
        parcel.writeInt(enemyClass2Position)
        parcel.writeInt(enemyClass3Position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalcConditionVO> {
        override fun createFromParcel(parcel: Parcel): CalcConditionVO {
            return CalcConditionVO(parcel)
        }

        override fun newArray(size: Int): Array<CalcConditionVO?> {
            return arrayOfNulls(size)
        }
    }

}