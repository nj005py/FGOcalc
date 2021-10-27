package org.phantancy.fgocalc.groupcalc.entity.vo

import android.os.Parcel
import android.os.Parcelable
import org.phantancy.fgocalc.util.readDoubleList
import org.phantancy.fgocalc.util.readIntList
import org.phantancy.fgocalc.util.writeDoubleList
import org.phantancy.fgocalc.util.writeIntList

data class GroupEnemyVO(
        //敌人
        var enemyCount: Int = 1,
        //np敌补正，默认saber
        var enemysNpMod: ArrayList<Double> = arrayListOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0),
        //打星敌补正，默认saber
        var enemysStarMod: ArrayList<Double> = arrayListOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        var enemysClassPosition: ArrayList<Int> = arrayListOf(0, 0, 0, 0, 0, 0)

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt()) {
        readDoubleList(parcel, enemysNpMod)
        readDoubleList(parcel, enemysStarMod)
        readIntList(parcel, enemysClassPosition)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(enemyCount)
        writeDoubleList(parcel, enemysNpMod)
        writeDoubleList(parcel, enemysStarMod)
        writeIntList(parcel, enemysClassPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupEnemyVO> {
        override fun createFromParcel(parcel: Parcel): GroupEnemyVO {
            return GroupEnemyVO(parcel)
        }

        override fun newArray(size: Int): Array<GroupEnemyVO?> {
            return arrayOfNulls(size)
        }
    }

}