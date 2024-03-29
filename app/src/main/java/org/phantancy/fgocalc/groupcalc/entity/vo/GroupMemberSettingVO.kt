package org.phantancy.fgocalc.groupcalc.entity.vo

import android.os.Parcel
import android.os.Parcelable
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import org.phantancy.fgocalc.data.ConditionData
import org.phantancy.fgocalc.entity.BuffInputEntity

//阶职相性
//阵营相性
//选择宝具 获取宝具列表再初始化
//等级
//芙芙atk
//礼装atk
//总atk
//总hp
//剩余hp
//条件buff UI数据
class GroupMemberSettingVO():Parcelable {
    //阶职相性
    var affinity = ""
    //阵营相性
    var attribute = ""
    //选择宝具 哪个宝具？几宝？
    var npDes = ""
    //等级
    var lv = 1
    //芙芙atk
    var fouAtk = 0
    //礼装atk
    var essenceAtk = 0
    //总atk
    var atkTotal = 0
    //总hp
    var hpTotal = 0
    //剩余hp
    var hpLeft = 0
    //等级atk
    var atkLv = 0
    //等级hp
    var hpLv = 0
    //buff UI list
    var uiBuffs = ArrayList<BuffInputEntity>()

    constructor(parcel: Parcel) : this() {
        affinity = parcel.readString().toString()
        attribute = parcel.readString().toString()
        npDes = parcel.readString().toString()
        lv = parcel.readInt()
        fouAtk = parcel.readInt()
        essenceAtk = parcel.readInt()
        atkTotal = parcel.readInt()
        hpTotal = parcel.readInt()
        hpLeft = parcel.readInt()
        atkLv = parcel.readInt()
        hpLv = parcel.readInt()
        uiBuffs = parcel.createTypedArrayList(BuffInputEntity.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(affinity)
        parcel.writeString(attribute)
        parcel.writeString(npDes)
        parcel.writeInt(lv)
        parcel.writeInt(fouAtk)
        parcel.writeInt(essenceAtk)
        parcel.writeInt(atkTotal)
        parcel.writeInt(hpTotal)
        parcel.writeInt(hpLeft)
        parcel.writeInt(atkLv)
        parcel.writeInt(hpLv)
        parcel.writeTypedList(uiBuffs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupMemberSettingVO> {
        override fun createFromParcel(parcel: Parcel): GroupMemberSettingVO {
            return GroupMemberSettingVO(parcel)
        }

        override fun newArray(size: Int): Array<GroupMemberSettingVO?> {
            return arrayOfNulls(size)
        }
    }

}