package org.phantancy.fgocalc.groupcalc.entity.bo

import android.os.Parcel
import android.os.Parcelable
import androidx.collection.SimpleArrayMap
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.util.readSimpleArrayMap
import org.phantancy.fgocalc.util.writeSimpleArrayMap
import java.io.Serializable

data class CalcBO(
        //职阶克制
        var affinityMod: Double = 2.0,
        //阵营克制
        var attributeMod: Double = 1.0,
        //选择宝具
        var npEntity: NoblePhantasmEntity?,
        //宝具伤害倍率
        var npDmgMultiplier: Double = 0.0,
        //总atk
        var atk: Double = 0.0,
        //总hp
        var hp: Double = 0.0,
        //剩余hp
        var hpLeft: Double = 0.0,
        var buffMap: SimpleArrayMap<String, Double> = SimpleArrayMap()
)  : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readParcelable(NoblePhantasmEntity::class.java.classLoader),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble()
            ) {
        readSimpleArrayMap(parcel,buffMap)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(affinityMod)
        parcel.writeDouble(attributeMod)
        parcel.writeParcelable(npEntity, flags)
        parcel.writeDouble(npDmgMultiplier)
        parcel.writeDouble(atk)
        parcel.writeDouble(hp)
        parcel.writeDouble(hpLeft)
        writeSimpleArrayMap(parcel,buffMap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalcBO> {
        override fun createFromParcel(parcel: Parcel): CalcBO {
            return CalcBO(parcel)
        }

        override fun newArray(size: Int): Array<CalcBO?> {
            return arrayOfNulls(size)
        }
    }

}


