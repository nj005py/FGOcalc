package org.phantancy.fgocalc.entity

import android.os.Parcel
import android.os.Parcelable
import org.phantancy.fgocalc.common.Constant.CARD_QUICK

//指令卡
data class CardObject(
        //卡色
        var type: String = CARD_QUICK,
        //从者id
        var svtId: Int = 0,
        //属于某从者
        var belongId: Int = 0,
        //第几张卡
        var position: Int = 0,
        //野兽足迹
        var extraAtk: Double = 0.0,
        //纹章buff
        var buffMap: Map<String, String>? = null
):Parcelable {
    //ui是否显示
    var isVisible = true

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()!!
        belongId = parcel.readInt()
        position = parcel.readInt()
        extraAtk = parcel.readDouble()
        buffMap = LinkedHashMap()
        parcel.readMap(buffMap ,javaClass.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeInt(belongId)
        parcel.writeInt(position)
        parcel.writeDouble(extraAtk)
        parcel.writeMap(buffMap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardObject> {
        override fun createFromParcel(parcel: Parcel): CardObject {
            return CardObject(parcel)
        }

        override fun newArray(size: Int): Array<CardObject?> {
            return arrayOfNulls(size)
        }
    }

}