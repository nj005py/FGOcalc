package org.phantancy.fgocalc.logic

import android.util.Log
import org.phantancy.fgocalc.common.Formula
import org.phantancy.fgocalc.common.ParamsUtil
import org.phantancy.fgocalc.data.BuffData
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupCalcBO
import org.phantancy.fgocalc.entity.ResultDmg
import org.phantancy.fgocalc.entity.ResultGroupDmg
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupMemberSettingBO
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupResultNp
import java.text.MessageFormat

/**
 * 每个从者独立信息
 * 职阶相性
 * 阵营相性
 * 选择宝具
 * 宝具lv
 * 芙芙atk
 * 礼装atk
 * 等级
 * atk
 * 总hp
 * 剩余hp
 * 各种buff
 *
 * 多个从者共享信息
 * 敌人数量
 * 敌方1
 * 敌方2
 * 敌方3
 */
class CalcLogic {
    val TAG = "CalcLogic"

    //计算多张卡的伤害
    fun cardsDmg(random: Double, groupCalcBO: GroupCalcBO, isBraveChain: Boolean):ResultDmg {
        //每张卡伤害结果
        /**
         * 每张卡都有cardEntity与对应的servant
         */
        //todo 结果携带对应从者
        val groupDmg = ResultGroupDmg()
        val resList = arrayListOf<Double>()
        for ((position, card) in groupCalcBO.chosenCards.withIndex()) {
            //todo 计算伤害
            var res: Double = dmgCalc(position, random, groupCalcBO)
            res = Math.floor(res)
            resList.add(res)
        }
        var sum = 0.0
        for (res in resList){
            sum += res
        }
        resList.add(sum)
        var des = ""
        for ((index,res) in resList.withIndex()){
            des += "c${index + 1}: ${ParamsUtil.dmgResFormat(res)}\n"
        }
        var resultDmg = ResultDmg().apply {
            c1 = ParamsUtil.dmgResFormat(resList[0])
            c2 = ParamsUtil.dmgResFormat(resList[1])
            c3 = ParamsUtil.dmgResFormat(resList[2])
            if (isBraveChain) {
                c4 = ParamsUtil.dmgResFormat(resList[3])
            } else {
                c4 = ""
            }
            this.sum = ParamsUtil.dmgResFormat(sum)
            this.des = des
        }
        return resultDmg
    }

    /**
     * 伤害计算准备，计算单卡伤害
     *
     * @param cardType
     * @param position
     * @param random
     * @return
     */
    private fun dmgCalc(position: Int, random: Double, groupCalcBO: GroupCalcBO): Double {
        //todo 判断卡片类型，宝具卡或普攻卡
        val curCardType = groupCalcBO.chosenCards[position].type
        return if (ParamsUtil.isNp(curCardType))
            npDmg(position, random, groupCalcBO)
        else attackDmg(position, random, groupCalcBO)
    }

    private fun attackDmg(position: Int, random: Double, groupCalcBO: GroupCalcBO): Double {
        /**
         * 准备条件
         */
        //todo 计算
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]

        /**
         * 单独卡计算的部分
         */
        //atk
        val atk: Double = setting.atk
        //卡牌伤害倍率
        val cardDmgMultiplier = ParamsUtil.getCardDmgMultiplier(card.type)
        //位置补正
        val positionMod = ParamsUtil.getDmgPositionMod(position + 1)
        //判断宝具卡前还是后，按需取buff
        var effectiveBuff = getEffectiveBuff(position, groupCalcBO)
        //首卡加成
        val firstCardMod = ParamsUtil.getDmgFirstCardMod(groupCalcBO.firstCardType)
        //职阶系数
        val classAtkMod = ParamsUtil.getClassAtkMod(servant.classType)
        //职阶克制
        val affinityMod: Double = setting.affinityMod
        //阵营克制
        val attributeMod: Double = setting.attributeMod
        //攻击buff
        var atkBuff: Double = getAtkBuff(position, groupCalcBO)
        //防御buff
        val defBuff = 0.0
        //特攻
        val specialBuff: Double = safeGetBuffMap(BuffData.SPECIAL_UP, setting)
        //特防
        val specialDefBuff = 0.0

        /**
         * 暴击buff
         */
        //判断暴击
        val isCritical = ParamsUtil.isCritical(position + 1, groupCalcBO.isCritical1,
                groupCalcBO.isCritical2,
                groupCalcBO.isCritical3)
        var criticalBuff = ParamsUtil.getCriticalBuff(isCritical, card.type,
                safeGetBuffMap(BuffData.CRITICAL_UP, setting),
                0.0,
                safeGetBuffMap(BuffData.CRITICAL_QUICK_UP, setting),
                safeGetBuffMap(BuffData.CRITICAL_ARTS_UP, setting),
                safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP, setting))
        criticalBuff += servant.criticalBuffN
        //宝具卡后
        for (index in 0 until position) {
            if (ParamsUtil.isNp(groupCalcBO.chosenCards[index].type)) {
                val indexSetting = groupCalcBO.chosenSetting[index]
                criticalBuff += ParamsUtil.getCriticalBuff(isCritical, card.type,
                        safeGetBuffMap(BuffData.CRITICAL_UP_AF, indexSetting), 0.0,
                        safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_AF, indexSetting),
                        safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_AF, indexSetting),
                        safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_AF, indexSetting))
                //前面有宝具卡则加伤害后buff
                if (groupCalcBO.chosenCards[index].svtPosition == card.svtPosition) {
                    //与前面宝具卡是同个从者，则加伤害前buff
                    criticalBuff += ParamsUtil.getCriticalBuff(isCritical, card.type,
                            safeGetBuffMap(BuffData.CRITICAL_UP_BE, indexSetting), 0.0,
                            safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_BE, indexSetting),
                            safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_BE, indexSetting),
                            safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_BE, indexSetting))
                }
            }
        }
        //暴击补正
        val criticalMod = ParamsUtil.getDmgCriticalMod(isCritical)
        //ex卡补正
        val exDmgBouns = ParamsUtil.getExDmgBouns(card.type, groupCalcBO.isSameColor)
        //固伤
        val selfDmgBuff: Double = safeGetBuffMap(BuffData.SELF_DAMAGE_UP, setting)
        //固防
        val selfDmgDefBuff = 0.0
        //红链
        val busterChainMod = ParamsUtil.mergeBusterChainMod(card.type, groupCalcBO.isBusterChain)
        return Formula.damgeFormula(atk, cardDmgMultiplier, positionMod, effectiveBuff, firstCardMod, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff, criticalBuff, criticalMod,
                exDmgBouns, selfDmgBuff, selfDmgDefBuff, busterChainMod)
    }

    //宝具伤害
    private fun npDmg(position: Int, random: Double, groupCalcBO: GroupCalcBO): Double {
        //todo 宝具
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        //atk
        val atk: Double = setting.atk
        //宝具倍率
        val npDmgMultiplier: Double = getNpMultiplier(servant.id, setting.npDmgMultiplier,
                safeGetBuffMap(BuffData.NP_MULTIPLIER_UP_BE, setting), setting.hpLeft, setting.hp)
        //卡牌伤害倍率
        val cardDmgMultiplier = ParamsUtil.getCardDmgMultiplier(card.type)
        // 卡牌buff(魔放)
        var effectiveBuff = getEffectiveBuffForNp(position, groupCalcBO)
        //职阶系数
        val classAtkMod = ParamsUtil.getClassAtkMod(servant.classType)
        //职阶克制
        val affinityMod: Double = setting.affinityMod
        //阵营克制
        val attributeMod: Double = setting.attributeMod
        //攻击buff
        var atkBuff: Double = getAtkBuffForNp(position, groupCalcBO)
        //防御buff
        val defBuff = 0.0
        //特攻
        val specialBuff = safeGetBuffMap(BuffData.SPECIAL_UP, setting)
        //特防
        val specialDefBuff = 0.0
        //宝具威力
        val npPowerBuff = safeGetBuffMap(BuffData.NP_POWER_UP, setting)
        //宝具特攻
        var npSpecialBuff = safeGetBuffMap(BuffData.NP_SPECICAL_UP_BE, setting)
        //宝具特攻不能为0
        if (npSpecialBuff == 0.0) {
            npSpecialBuff = 1.0
        }
        //固伤
        val selfDmgBuff = safeGetBuffMap(BuffData.SELF_DAMAGE_UP, setting)
        //固防
        val selfDmgDefBuff = 0.0
        Log.d(TAG, MessageFormat.format("{0} {1} {2} {3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16} ", atk, npDmgMultiplier, cardDmgMultiplier, effectiveBuff, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff,
                npPowerBuff, npSpecialBuff, selfDmgBuff, selfDmgDefBuff))
        return Formula.npDamageFormula(atk, npDmgMultiplier, cardDmgMultiplier, effectiveBuff, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff,
                npPowerBuff, npSpecialBuff, selfDmgBuff, selfDmgDefBuff)
    }

    //宝具倍率处理
    fun getNpMultiplier(svtId: Int, npDmgMultiplier: Double, npMultiplierUp: Double, hpLeft: Double, hp: Double): Double {
        var npDmgMultiplier = npDmgMultiplier
        npDmgMultiplier = if (svtId == 66 || svtId == 161) {
            //骑双子,土方岁三
            npDmgMultiplier + npMultiplierUp * (1 - hpLeft / hp)
        } else if (svtId == 131) {
            //弓双子
            npDmgMultiplier + 6 * (1 - hpLeft / hp)
        } else {
            npDmgMultiplier + npMultiplierUp
        }
        return npDmgMultiplier
    }

    /**
     * np计算
     * 伤害计算只计算打第一个敌人
     * np、打星，光炮需要计算打所有敌人
     */
    //计算多张卡的np
    fun cardsNp(groupCalcBO: GroupCalcBO, isBraveChain: Boolean): GroupResultNp {
        var resList = arrayListOf<DoubleArray>()
        for ((position, card) in groupCalcBO.chosenCards.withIndex()) {
            //todo 计算np
            //原始数据
            var res: DoubleArray = npGenCalc(position,groupCalcBO)
            resList.add(res)
        }
        var strRes = arrayListOf<String>()
        for ((index,res) in resList.withIndex()){
            //展示数据
            strRes.add(parseNpRes(res, groupCalcBO.chosenCards[index].type,groupCalcBO.enemyCount))
        }
        var resultNp = GroupResultNp().apply {
            c1 = strRes[0]
            c2 = strRes[1]
            c3 = strRes[2]
            c1Data = oneCardTotalNpRes(resList[0],groupCalcBO.chosenCards[0].type,groupCalcBO.enemyCount)
            c2Data = oneCardTotalNpRes(resList[1],groupCalcBO.chosenCards[1].type,groupCalcBO.enemyCount)
            c3Data = oneCardTotalNpRes(resList[2],groupCalcBO.chosenCards[2].type,groupCalcBO.enemyCount)
            if (isBraveChain) {
                c4 = strRes[3]
                c4Data = oneCardTotalNpRes(resList[3],groupCalcBO.chosenCards[3].type,groupCalcBO.enemyCount)
            } else{
                c4 = ""
            }
        }
        return resultNp
    }

    //解析np计算结果
    private fun parseNpRes(res: DoubleArray, cardType: String,enemyCount:Int): String {
        val builder = StringBuilder()
        if (ParamsUtil.isNp(cardType)) {
            val sum = ParamsUtil.npGenResFormat(oneCardTotalNpRes(res, cardType, enemyCount))
            builder.append(sum)
                    .append(" (")
            for (i in 0 until enemyCount) {
                builder.append(ParamsUtil.npGenResFormat(res[i]))
                if (i < enemyCount - 1) {
                    builder.append(", ")
                }
            }
            builder.append(")")
        } else {
            builder.append(ParamsUtil.npGenResFormat(res[0]))
        }
        return builder.toString()
    }

    //单卡总共打出的np
    private fun oneCardTotalNpRes(res: DoubleArray, cardType: String, enemyCount:Int): Double {
        return if (ParamsUtil.isNp(cardType)) {
            var sum = 0.0
            for (i in 0 until enemyCount) {
                sum += res[i]
            }
            sum
        } else {
            res[0]
        }
    }

    private fun npGenCalc(position: Int, groupCalcBO: GroupCalcBO): DoubleArray {
        val curCardType = groupCalcBO.chosenCards[position].type
        return if (ParamsUtil.isNp(curCardType)) npNpGenDelegate(position,groupCalcBO)
        else npGen(position, groupCalcBO)
    }

    //普攻np 肯定只打1个敌人
    fun npGen(position: Int, groupCalcBO: GroupCalcBO): DoubleArray {
        /**
         * 准备条件
         */
        //todo 计算
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        //np获取率
        val na = ParamsUtil.getNa(card.type, servant.quickNa, servant.artsNa, servant.busterNa, servant.exNa, servant.npHit.toDouble())
        //hit数
        val hits = ParamsUtil.getHits(card.type, servant.quickHit, servant.artsHit, servant.busterHit, servant.exHit, servant.npHit).toDouble()
        //卡牌np倍率
        val cardNpMultiplier = ParamsUtil.getCardNpMultiplier(card.type)
        //位置加成
        val positionMod = ParamsUtil.getNpPositionMod(position)
        //魔放
        var effectiveBuff = getEffectiveBuff(position, groupCalcBO)
        //首卡加成
        val firstCardMod = ParamsUtil.getNpFirstCardMod(groupCalcBO.firstCardType)
        //黄金率
        var npBuff = getNpcUp(position, groupCalcBO)
        //暴击补正
        //判断暴击
        val isCritical = ParamsUtil.isCritical(position, groupCalcBO.isCritical1,
                groupCalcBO.isCritical2,
                groupCalcBO.isCritical3)
        val criticalMod = ParamsUtil.getNpCriticalMod(isCritical, card.type)
        //overkill补正
        val isOverkill = ParamsUtil.isOverkill(position, groupCalcBO.isOverkill1, groupCalcBO.isOverkill2,
                groupCalcBO.isOverkill3, groupCalcBO.isOverkill4)
        val overkillMod = ParamsUtil.getNpOverkillMod(isOverkill)
        val res = DoubleArray(3)
        //只能是第1个敌人
        val enemyNpMod = groupCalcBO.enemysNpMod[0]
        res[0] = Formula.npGenerationFormula(na, hits, cardNpMultiplier, positionMod, effectiveBuff, firstCardMod,
                npBuff, criticalMod, overkillMod, enemyNpMod)
        return res
    }

    //宝具np 打多个敌人
    private fun npNpGenDelegate(position: Int,groupCalcBO: GroupCalcBO): DoubleArray {
        val servant = groupCalcBO.chosenServants[position]
        val res = DoubleArray(6)
        //辅助宝具不用算直接为0
        if (servant.npType == "support") {
            res[0] = 0.0
            return res
        }
        //单体宝具只算第一个敌人
        if (servant.npType == "one") {
            res[0] = npNpGen(position, groupCalcBO, groupCalcBO.enemysNpMod[0])
        }
        //光炮宝具算整个敌人列表
        if (servant.npType == "all") {
            for (i in 0 until groupCalcBO.enemyCount) {
                //计算
                res[i] = npNpGen(position, groupCalcBO, groupCalcBO.enemysNpMod[i])
            }
            return res
        }
        return res
    }

    //宝具np 打1个敌人，宝具的敌补正需要外部指定
    private fun npNpGen(position: Int, groupCalcBO: GroupCalcBO, enemyNpMod: Double): Double {
        //todo 计算
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        //np获取率
        val na = ParamsUtil.getNa(card.type, servant.quickNa, servant.artsNa, servant.busterNa, servant.exNa, servant.npNa)
        //hit数
        val hits = ParamsUtil.getHits(card.type, servant.quickHit, servant.artsHit, servant.busterHit, servant.exHit, servant.npHit).toDouble()
        //卡牌np倍率
        val cardNpMultiplier = ParamsUtil.getCardNpMultiplier(card.type)
        //魔放
        var effectiveBuff = getEffectiveBuffForNp(position, groupCalcBO)
        //黄金率
        var npBuff = getNpcUpForNp(position, groupCalcBO)
        //overkill补正
        val isOverkill = ParamsUtil.isOverkill(position+1, groupCalcBO.isOverkill1, groupCalcBO.isOverkill2,
                groupCalcBO.isOverkill3, groupCalcBO.isOverkill4)
        val overkillMod = ParamsUtil.getNpOverkillMod(isOverkill)
        return Formula.npNpGenerationFormula(na, hits, cardNpMultiplier, effectiveBuff, npBuff, overkillMod, enemyNpMod)
    }

    /**
     * 公共方法
     */

    //安全取buff
    fun safeGetBuffMap(key: String, calcEntity: GroupMemberSettingBO): Double {
        return if (calcEntity.buffMap.get(key) == null) 0.0
        else calcEntity.buffMap.get(key)!!
    }
    /**
     * 宝具前，平A需要考虑:全buff+被动buff
     * 宝具，平A不考虑
     * 宝具后，平A需要考虑:全buff+被动buff+伤害前buff+伤害后buff=宝具前+伤害前buff+伤害后buff
     */
    /**
     * 宝具1 宝具2 平A3，都有前置buff，伤害，后置buff
     * 宝具1：全buff+被动buff+宝具1前置
     * 宝具2：全buff+被动buff+宝具1后置+宝具2前置
     * 平a3：全buff+被动buff+宝具1后置+宝具2后置
     *
     * 判断要不要加宝具后buff
     * 前置buff需要对比从者是否同一个，svtPosition判断
     * 1:不需要
     * 2：1为宝具，则加宝具后buff
     * 3：1为宝具，则加宝具后buff，2为宝具，则加宝具后buff
     * ex：前面最多1个宝具
     */
    //魔放处理：平A
    fun getEffectiveBuff(position: Int, groupCalcBO: GroupCalcBO): Double {
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        var quickBuff = 0.0
        var artsBuff = 0.0
        var busterBuff = 0.0
        var effectiveBuff = 0.0
        if (!ParamsUtil.isEx(card.type)) {
            quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP, setting)
            artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP, setting)
            busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP, setting)
            for (index in 0 until position) {
                if (ParamsUtil.isNp(groupCalcBO.chosenCards[index].type)) {
                    val indexSetting = groupCalcBO.chosenSetting[index]
                    //前面有宝具卡则加伤害后buff
                    quickBuff += safeGetBuffMap(BuffData.QUICK_UP_AF, indexSetting)
                    artsBuff += safeGetBuffMap(BuffData.ARTS_UP_AF, indexSetting)
                    busterBuff += safeGetBuffMap(BuffData.BUSTER_UP_AF, indexSetting)
                    if (groupCalcBO.chosenCards[index].svtPosition == card.svtPosition) {
                        //与前面宝具卡是同个从者，则加伤害前buff
                        quickBuff += safeGetBuffMap(BuffData.QUICK_UP_BE, indexSetting)
                        artsBuff += safeGetBuffMap(BuffData.ARTS_UP_BE, indexSetting)
                        busterBuff += safeGetBuffMap(BuffData.BUSTER_UP_BE, indexSetting)
                    }
                }
            }
            //最终用于计算的魔放结果
            effectiveBuff = ParamsUtil.getEffectiveBuff(card.type, quickBuff, artsBuff, busterBuff)
        }
        return effectiveBuff
    }

    //魔放处理：宝具
    fun getEffectiveBuffForNp(position: Int, groupCalcBO: GroupCalcBO): Double {
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        var quickBuff = 0.0
        var artsBuff = 0.0
        var busterBuff = 0.0
        var effectiveBuff = 0.0
        if (!ParamsUtil.isEx(card.type)) {
            quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP, setting)
            artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP, setting)
            busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP, setting)
            for (index in 0 until position) {
                if (ParamsUtil.isNp(groupCalcBO.chosenCards[index].type)) {
                    val indexSetting = groupCalcBO.chosenSetting[index]
                    //前面有宝具卡则加伤害后buff
                    quickBuff += safeGetBuffMap(BuffData.QUICK_UP_AF, indexSetting)
                    artsBuff += safeGetBuffMap(BuffData.ARTS_UP_AF, indexSetting)
                    busterBuff += safeGetBuffMap(BuffData.BUSTER_UP_AF, indexSetting)
                    if (groupCalcBO.chosenCards[index].svtPosition == card.svtPosition) {
                        //与前面宝具卡是同个从者，则加伤害前buff
                        quickBuff += safeGetBuffMap(BuffData.QUICK_UP_BE, indexSetting)
                        artsBuff += safeGetBuffMap(BuffData.ARTS_UP_BE, indexSetting)
                        busterBuff += safeGetBuffMap(BuffData.BUSTER_UP_BE, indexSetting)
                    }
                }
            }
            //宝具卡多加个自身前置buff
            quickBuff += safeGetBuffMap(BuffData.QUICK_UP_BE, setting)
            artsBuff += safeGetBuffMap(BuffData.ARTS_UP_BE, setting)
            busterBuff += safeGetBuffMap(BuffData.BUSTER_UP_BE, setting)
            //最终用于计算的魔放结果
            effectiveBuff = ParamsUtil.getEffectiveBuff(card.type, quickBuff, artsBuff, busterBuff)
        }
        return effectiveBuff
    }

    //攻击buff
    fun getAtkBuff(position: Int, groupCalcBO: GroupCalcBO): Double {
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        var atkBuff: Double = safeGetBuffMap(BuffData.ATK_UP, setting)
        for (index in 0 until position) {
            if (ParamsUtil.isNp(groupCalcBO.chosenCards[index].type)) {
                val indexSetting = groupCalcBO.chosenSetting[index]
                atkBuff += safeGetBuffMap(BuffData.ATK_UP_AF, indexSetting)
                //前面有宝具卡则加伤害后buff
                if (groupCalcBO.chosenCards[index].svtPosition == card.svtPosition) {
                    //与前面宝具卡是同个从者，则加伤害前buff
                    atkBuff += safeGetBuffMap(BuffData.ATK_UP_BE, indexSetting)
                }
            }
        }
        return atkBuff
    }

    //攻击buff：宝具
    fun getAtkBuffForNp(position: Int, groupCalcBO: GroupCalcBO): Double {
        val setting = groupCalcBO.chosenSetting[position]
        var atkBuff = getAtkBuff(position, groupCalcBO)
        atkBuff += safeGetBuffMap(BuffData.ATK_UP_BE, setting)
        return atkBuff
    }

    //黄金率：平A
    fun getNpcUp(position: Int, groupCalcBO: GroupCalcBO): Double {
        val card = groupCalcBO.chosenCards[position]
        val servant = groupCalcBO.chosenServants[position]
        val setting = groupCalcBO.chosenSetting[position]
        var npBuff = safeGetBuffMap(BuffData.NPC_UP, setting)
        for (index in 0 until position) {
            if (ParamsUtil.isNp(groupCalcBO.chosenCards[index].type)) {
                val indexSetting = groupCalcBO.chosenSetting[index]
                npBuff += safeGetBuffMap(BuffData.NPC_UP_AF, indexSetting)
                //前面有宝具卡则加伤害后buff
                if (groupCalcBO.chosenCards[index].svtPosition == card.svtPosition) {
                    //与前面宝具卡是同个从者，则加伤害前buff
                    npBuff += safeGetBuffMap(BuffData.NPC_UP_BE, indexSetting)
                }
            }
        }
        return npBuff
    }

    //黄金率：宝具
    fun getNpcUpForNp(position: Int, groupCalcBO: GroupCalcBO): Double {
        val setting = groupCalcBO.chosenSetting[position]
        var npBuff = getNpcUp(position, groupCalcBO)
        npBuff += safeGetBuffMap(BuffData.NPC_UP_BE, setting)
        return npBuff
    }
}