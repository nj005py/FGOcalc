package org.phantancy.fgocalc.groupcalc.entity.vo

import android.os.Parcel
import android.os.Parcelable
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupMemberSettingBO

//编队计算：编队成员
class GroupMemberVO() : Parcelable {
    var svtEntity: ServantEntity = ServantEntity()//从者数据
    set(value) {
        field = value
        settingVO.lv = svtEntity.rewardLv
    }
    var cards: ArrayList<CardBO> = arrayListOf<CardBO>()//从者配卡
    var settingVO: GroupMemberSettingVO = GroupMemberSettingVO()//从者条件 UI数据
    var settingBO: GroupMemberSettingBO = GroupMemberSettingBO()//从者条件 逻辑数据
    constructor(parcel: Parcel):this() {
        svtEntity = parcel.readParcelable(ServantEntity::class.java.classLoader)!!
        parcel.readTypedList(cards, CardBO.CREATOR)
        settingVO = parcel.readParcelable(GroupMemberSettingVO::class.java.classLoader)!!
        settingBO = parcel.readParcelable(GroupMemberSettingBO::class.java.classLoader)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(svtEntity, flags)
        parcel.writeTypedList(cards)
        parcel.writeParcelable(settingVO,flags)
        parcel.writeParcelable(settingBO,flags)
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