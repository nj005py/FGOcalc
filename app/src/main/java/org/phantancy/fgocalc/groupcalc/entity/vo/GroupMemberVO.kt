package org.phantancy.fgocalc.groupcalc.entity.vo

import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO

//编队计算：编队成员
data class GroupMemberVO(
        var svtEntity: ServantEntity,
        var cards: ArrayList<CardBO> = arrayListOf<CardBO>()
) {
}