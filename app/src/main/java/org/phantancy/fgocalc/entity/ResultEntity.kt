package org.phantancy.fgocalc.entity

import org.phantancy.fgocalc.common.Constant

data class ResultEntity(
        val type: Int = TYPE_CARD,
        val cardType: String = Constant.CARD_EX,
        var dmgMin: String? = "0",//最小伤害
        var dmgMax: String? = "0",//最大伤害
        var dmgAvg: String? = "0",//平均伤害
        var np: String? = "0",
        var star: String? = "0",
        var sum: String? = "0",//总结
        var avatar:Int? = 0//头像
) {
    companion object {
        val TYPE_CARD = 0x0;
        val TYEP_SUM = 0X1;
    }
}