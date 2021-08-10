package org.phantancy.fgocalc.entity
//编队计算：编队成员
data class GroupMemberEntity(
        var svtEntity: ServantEntity,
        var cards: ArrayList<CardObject> = arrayListOf<CardObject>()
) {
}