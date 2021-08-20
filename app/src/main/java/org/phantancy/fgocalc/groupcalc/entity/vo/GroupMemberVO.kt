package org.phantancy.fgocalc.groupcalc.entity.vo

import android.os.Parcel
import android.os.Parcelable
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO

//编队计算：编队成员
class GroupMemberVO() : Parcelable {
    var svtEntity: ServantEntity = ServantEntity()
    var cards: ArrayList<CardBO> = arrayListOf<CardBO>()

    constructor(parcel: Parcel):this() {
        svtEntity = parcel.readParcelable(ServantEntity::class.java.classLoader)!!
        parcel.readTypedList(cards, CardBO.CREATOR)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(svtEntity, flags)
        parcel.writeTypedList(cards)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupMemberVO> {
        override fun createFromParcel(parcel: Parcel): GroupMemberVO {
            return GroupMemberVO(parcel)
        }

        override fun newArray(size: Int): Array<GroupMemberVO?> {
            return arrayOfNulls(size)
        }
    }

}