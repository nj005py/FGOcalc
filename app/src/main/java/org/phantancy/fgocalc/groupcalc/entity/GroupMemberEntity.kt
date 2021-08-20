package org.phantancy.fgocalc.groupcalc.entity

import org.phantancy.fgocalc.entity.ServantEntity

//编队计算：编队成员
data class GroupMemberEntity(
        var svtEntity: ServantEntity,
        var cards: ArrayList<CardObject> = arrayListOf<CardObject>()
) {
}