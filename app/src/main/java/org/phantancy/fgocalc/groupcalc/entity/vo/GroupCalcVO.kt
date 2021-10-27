package org.phantancy.fgocalc.groupcalc.entity.vo

data class GroupCalcVO(
        //暴击
        var isCritical1: Boolean = false,
        var isCritical2: Boolean = false,
        var isCritical3: Boolean = false,
        //过量伤害
        var isOverkill1: Boolean = false,
        var isOverkill2: Boolean = false,
        var isOverkill3: Boolean = false,
        var isOverkill4: Boolean = false
)