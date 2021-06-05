package org.phantancy.fgocalc.entity

data class ResultEntity(
        val type: Int,
        val cardType: String,
        var dmgMin: String,
        var dmgMax: String,
        var np: String,
        var star: String,
        var sum: String = "x"
) {
    companion object {
        val TYPE_CARD = 0x0;
        val TYEP_SUM = 0X1;
    }
}