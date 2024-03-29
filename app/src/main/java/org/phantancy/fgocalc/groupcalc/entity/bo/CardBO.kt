package org.phantancy.fgocalc.groupcalc.entity.bo

import android.os.Parcel
import android.os.Parcelable
import androidx.collection.SimpleArrayMap
import org.phantancy.fgocalc.common.Constant.CARD_QUICK
import org.phantancy.fgocalc.util.readSimpleArrayMap
import org.phantancy.fgocalc.util.writeSimpleArrayMap

//指令卡
data class CardBO(
        //卡色
        var type: String = CARD_QUICK,
        //从者id
        var svtId: Int = 0,
        //属于某从者
        var svtPosition: Int = 0,
        //从者配卡中第几张卡
        var position: Int = 0,
        //野兽足迹
        var extraAtk: Double = 0.0,
        //纹章buff
        var buffMap: SimpleArrayMap<String,Double> = SimpleArrayMap()
):Parcelable {
    //ui是否显示
    var isVisible = true

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()!!
        svtId = parcel.readInt()
        svtPosition = parcel.readInt()
        position = parcel.readInt()
        extraAtk = parcel.readDouble()
        readSimpleArrayMap(parcel,buffMap)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeInt(svtId)
        parcel.writeInt(svtPosition)
        parcel.writeInt(position)
        parcel.writeDouble(extraAtk)
        writeSimpleArrayMap(parcel,buffMap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardBO> {
        override fun createFromParcel(parcel: Parcel): CardBO {
            return CardBO(parcel)
        }

        override fun newArray(size: Int): Array<CardBO?> {
            return arrayOfNulls(size)
        }
    }

}