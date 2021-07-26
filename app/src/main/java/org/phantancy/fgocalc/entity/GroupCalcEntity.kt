package org.phantancy.fgocalc.entity

import org.phantancy.fgocalc.common.Constant

data class GroupCalcEntity(
        var cardType1: String = Constant.CARD_QUICK,
        var cardType2: String = Constant.CARD_QUICK,
        var cardType3: String = Constant.CARD_QUICK,
        var cardType4: String = Constant.CARD_EX,
        var isCritical1: Boolean = false,
        var isCritical2: Boolean = false,
        var isCritical3: Boolean = false,
        var isOverkill1: Boolean = false,
        var isOverkill2: Boolean = false,
        var isOverkill3: Boolean = false,
        var isOverkill4: Boolean = false,
        /**
         * 需要判断的数据
         */
        var firstCardType: String = Constant.CARD_QUICK,
        var isSameColor: Boolean = false,
        var isBusterChain: Boolean = false
)