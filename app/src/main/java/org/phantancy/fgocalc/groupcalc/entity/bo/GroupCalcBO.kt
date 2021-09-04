package org.phantancy.fgocalc.groupcalc.entity.bo

import org.phantancy.fgocalc.common.Constant
import org.phantancy.fgocalc.entity.ServantEntity

//计算用公共数据
data class GroupCalcBO(
        /**
         * 选的卡
         */
        var chosenCards: ArrayList<CardBO> = arrayListOf(),
        //选卡对应条件
        var chosenSetting: ArrayList<GroupMemberSettingBO> = arrayListOf(),
        //选卡对应从者
        var chosenServants : ArrayList<ServantEntity> = arrayListOf(),
        //暴击
        var isCritical1: Boolean = false,
        var isCritical2: Boolean = false,
        var isCritical3: Boolean = false,
        //过量伤害
        var isOverkill1: Boolean = false,
        var isOverkill2: Boolean = false,
        var isOverkill3: Boolean = false,
        var isOverkill4: Boolean = false,
        /**
         * 需要判断的数据
         */
        var firstCardType: String = Constant.CARD_QUICK,
        var isSameColor: Boolean = false,
        var isBusterChain: Boolean = false,
        //敌人
        var enemyCount: Int = 1,
        //np敌补正
        var enemysNpMod: ArrayList<Double> = arrayListOf(),
        //打星敌补正
        var enemysStarMod: ArrayList<Double> = arrayListOf()
)