package org.phantancy.fgocalc.logic

import android.util.Log
import org.phantancy.fgocalc.common.Formula
import org.phantancy.fgocalc.common.ParamsUtil
import org.phantancy.fgocalc.data.BuffData
import org.phantancy.fgocalc.entity.CalcEntity
import org.phantancy.fgocalc.entity.GroupCalcEntity
import org.phantancy.fgocalc.entity.ResultDmg
import org.phantancy.fgocalc.entity.ServantEntity
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

    //计算4张卡的伤害
    fun fourCardsDmg(random: Double, calcEntities: List<CalcEntity>, groupCalcEntity: GroupCalcEntity,
                     servants: List<ServantEntity>, isBraveChain: Boolean): ResultDmg {
        /**
         * 需要3张卡判断的参数
         */
        //是否同色
        groupCalcEntity.isSameColor = ParamsUtil.isCardsSameColor(groupCalcEntity.cardType1,
                groupCalcEntity.cardType2, groupCalcEntity.cardType3)
        //是否红链
        groupCalcEntity.isBusterChain = ParamsUtil.isCardsBusterChain(groupCalcEntity.cardType1,
                groupCalcEntity.cardType2, groupCalcEntity.cardType3)
        //每张卡伤害结果
        /**
         * 每张卡都有cardEntity与对应的servant
         */
        var res1: Double = dmgCalc(groupCalcEntity.cardType1, 1, random, calcEntities[0], groupCalcEntity, servants[0])
        var res2: Double = dmgCalc(groupCalcEntity.cardType2, 2, random, calcEntities[1], groupCalcEntity, servants[1])
        var res3: Double = dmgCalc(groupCalcEntity.cardType3, 3, random, calcEntities[2], groupCalcEntity, servants[2])
        var res4: Double = 0.0
        res1 = Math.floor(res1)
        res2 = Math.floor(res2)
        res3 = Math.floor(res3)
        if (isBraveChain) {
            res4 = dmgCalc(groupCalcEntity.cardType4, 4, random, calcEntities[0], groupCalcEntity, servants[0])
            res4 = Math.floor(res4)
        }
        var sum = res1 + res2 + res3
        if (isBraveChain) {
            sum += res4
        }
        val des = MessageFormat.format("c1:{0}\nc2:{1}\nc3:{2}\nc4:{3}\nsum:{4}\n\n",
                ParamsUtil.dmgResFormat(res1),
                ParamsUtil.dmgResFormat(res2),
                ParamsUtil.dmgResFormat(res3),
                ParamsUtil.dmgResFormat(res4),
                ParamsUtil.dmgResFormat(sum))
        return ResultDmg(
                ParamsUtil.dmgResFormat(res1),
                ParamsUtil.dmgResFormat(res2),
                ParamsUtil.dmgResFormat(res3),
                ParamsUtil.dmgResFormat(res4),
                ParamsUtil.dmgResFormat(sum),
                des
        )
    }

    /**
     * 伤害计算准备，计算单卡伤害
     *
     * @param cardType
     * @param position
     * @param random
     * @return
     */
    private fun dmgCalc(cardType: String, position: Int, random: Double, calcEntity: CalcEntity,
                        groupCalcEntity: GroupCalcEntity, servant: ServantEntity): Double {
        //判断卡片类型，宝具卡或普攻卡
        return if (ParamsUtil.isNp(cardType))
            npDmg(cardType, random, calcEntity, servant)
        else dmg(cardType, position, random, calcEntity, groupCalcEntity, servant)
    }

    private fun dmg(cardType: String, position: Int, random: Double, calcEntity: CalcEntity,
                    groupCalcEntity: GroupCalcEntity, servant: ServantEntity): Double {
        /**
         * 准备条件
         */
        //首卡类型，看染色
        val cardType1: String = groupCalcEntity.cardType1
        //看看是不是同色卡链
        val isSameColor: Boolean = groupCalcEntity.isSameColor
        //看看是不是三红加固伤
        val isBusterChain: Boolean = groupCalcEntity.isBusterChain
        //宝具卡位置
        val npPosition = ParamsUtil.getNpPosition(groupCalcEntity.cardType1, groupCalcEntity.cardType2,
                groupCalcEntity.cardType3)

        /**
         * 单独卡计算的部分
         */
        //atk
        val atk: Double = calcEntity.getAtk()
        //卡牌伤害倍率
        val cardDmgMultiplier = ParamsUtil.getCardDmgMultiplier(cardType)
        //位置补正
        val positionMod = ParamsUtil.getDmgPositionMod(position)

        /**
         * 宝具前，平A需要考虑:全buff+被动buff
         * 宝具，平A不考虑
         * 宝具后，平A需要考虑:全buff+被动buff+伤害前buff+伤害后buff=宝具前+伤害前buff+伤害后buff
         */
        //判断宝具卡前还是后，按需取buff
        var quickBuff = 0.0
        var artsBuff = 0.0
        var busterBuff = 0.0
        var effectiveBuff = 0.0
        if (!ParamsUtil.isEx(cardType)) {
            quickBuff = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP, calcEntity)
            artsBuff = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP, calcEntity)
            busterBuff = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP, calcEntity)
            //宝具前buff
            if (position > npPosition) {
                //宝具后buff
                quickBuff += safeGetBuffMap(BuffData.QUICK_UP_BE, calcEntity) + safeGetBuffMap(BuffData.QUICK_UP_AF, calcEntity)
                artsBuff += safeGetBuffMap(BuffData.ARTS_UP_BE, calcEntity) + safeGetBuffMap(BuffData.ARTS_UP_AF, calcEntity)
                busterBuff += safeGetBuffMap(BuffData.BUSTER_UP_BE, calcEntity) + safeGetBuffMap(BuffData.BUSTER_UP_AF, calcEntity)
            }
            //最终用于计算的魔放结果
            effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff)
        }

        //首卡加成
        val firstCardMod = ParamsUtil.getDmgFirstCardMod(cardType1)
        //职阶系数
        val classAtkMod = ParamsUtil.getClassAtkMod(servant.classType)
        //职阶克制
        val affinityMod: Double = calcEntity.getAffinityMod()
        //阵营克制
        val attributeMod: Double = calcEntity.getAttributeMod()
        //攻击buff
        var atkBuff: Double = safeGetBuffMap(BuffData.ATK_UP, calcEntity)
        if (position > npPosition) {
            atkBuff += safeGetBuffMap(BuffData.ATK_UP_BE, calcEntity) + safeGetBuffMap(BuffData.ATK_UP_AF, calcEntity)
        }
        //防御buff
        val defBuff = 0.0
        //特攻
        val specialBuff: Double = safeGetBuffMap(BuffData.SPECIAL_UP, calcEntity)
        //特防
        val specialDefBuff = 0.0

        /**
         * 暴击buff
         */
        //判断暴击
        val isCritical = ParamsUtil.isCritical(position, groupCalcEntity.isCritical1,
                groupCalcEntity.isCritical2,
                groupCalcEntity.isCritical3)
        var criticalBuff = ParamsUtil.getCriticalBuff(isCritical, cardType,
                safeGetBuffMap(BuffData.CRITICAL_UP, calcEntity),
                0.0,
                safeGetBuffMap(BuffData.CRITICAL_QUICK_UP, calcEntity),
                safeGetBuffMap(BuffData.CRITICAL_ARTS_UP, calcEntity),
                safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP, calcEntity))
        criticalBuff += servant.criticalBuffN
        //宝具卡后
        if (position > npPosition) {
            criticalBuff += ParamsUtil.getCriticalBuff(isCritical, cardType,
                    safeGetBuffMap(BuffData.CRITICAL_UP_BE, calcEntity) + safeGetBuffMap(BuffData.CRITICAL_UP_AF, calcEntity), 0.0,
                    safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_BE, calcEntity) + safeGetBuffMap(BuffData.CRITICAL_QUICK_UP_AF, calcEntity),
                    safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_BE, calcEntity) + safeGetBuffMap(BuffData.CRITICAL_ARTS_UP_AF, calcEntity),
                    safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_BE, calcEntity) + safeGetBuffMap(BuffData.CRITICAL_BUSTER_UP_AF, calcEntity))
        }
        //暴击补正
        val criticalMod = ParamsUtil.getDmgCriticalMod(isCritical)
        //ex卡补正
        val exDmgBouns = ParamsUtil.getExDmgBouns(cardType, isSameColor)
        //固伤
        val selfDmgBuff: Double = safeGetBuffMap(BuffData.SELF_DAMAGE_UP, calcEntity)
        //固防
        val selfDmgDefBuff = 0.0
        //红链
        val busterChainMod = ParamsUtil.mergeBusterChainMod(cardType, isBusterChain)
        return Formula.damgeFormula(atk, cardDmgMultiplier, positionMod, effectiveBuff, firstCardMod, classAtkMod,
                affinityMod, attributeMod, random, atkBuff, defBuff, specialBuff, specialDefBuff, criticalBuff, criticalMod,
                exDmgBouns, selfDmgBuff, selfDmgDefBuff, busterChainMod)
    }

    //宝具伤害
    private fun npDmg(cardType: String, random: Double, calcEntity: CalcEntity, servant: ServantEntity): Double {
        /**
         * 需要3张卡判断的参数
         */
        /**
         * 单独卡计算的部分
         */
        //atk
        val atk: Double = calcEntity.getAtk()
        //宝具倍率
        val npDmgMultiplier: Double = getNpMultiplier(servant.id, calcEntity.getNpDmgMultiplier(),
                safeGetBuffMap(BuffData.NP_MULTIPLIER_UP_BE, calcEntity), calcEntity.getHpLeft(), calcEntity.getHp())
        //卡牌伤害倍率
        val cardDmgMultiplier = ParamsUtil.getCardDmgMultiplier(cardType)
        /**
         * 宝具buff:全buff+被动buff+伤害前buff
         */
        /**
         * 卡牌buff(魔放)
         */
        val quickBuff: Double = servant.quickBuffN + safeGetBuffMap(BuffData.QUICK_UP, calcEntity) + safeGetBuffMap(BuffData.QUICK_UP_BE, calcEntity)
        val artsBuff: Double = servant.artsBuffN + safeGetBuffMap(BuffData.ARTS_UP, calcEntity) + safeGetBuffMap(BuffData.ARTS_UP_BE, calcEntity)
        val busterBuff: Double = servant.busterBuffN + safeGetBuffMap(BuffData.BUSTER_UP, calcEntity) + safeGetBuffMap(BuffData.BUSTER_UP_BE, calcEntity)
        //最终用于计算的魔放结果
        val effectiveBuff = ParamsUtil.getEffectiveBuff(cardType, quickBuff, artsBuff, busterBuff)
        //职阶系数
        val classAtkMod = ParamsUtil.getClassAtkMod(servant.classType)
        //职阶克制
        val affinityMod: Double = calcEntity.getAffinityMod()
        //阵营克制
        val attributeMod: Double = calcEntity.getAttributeMod()
        //攻击buff
        val atkBuff = safeGetBuffMap(BuffData.ATK_UP, calcEntity) + safeGetBuffMap(BuffData.ATK_UP_BE, calcEntity)
        //防御buff
        val defBuff = 0.0
        //特攻
        val specialBuff = safeGetBuffMap(BuffData.SPECIAL_UP, calcEntity)
        //特防
        val specialDefBuff = 0.0
        //宝具威力
        val npPowerBuff = safeGetBuffMap(BuffData.NP_POWER_UP, calcEntity)
        //宝具特攻
        var npSpecialBuff = safeGetBuffMap(BuffData.NP_SPECICAL_UP_BE, calcEntity)
        //宝具特攻不能为0
        if (npSpecialBuff == 0.0) {
            npSpecialBuff = 1.0
        }
        //固伤
        val selfDmgBuff = safeGetBuffMap(BuffData.SELF_DAMAGE_UP, calcEntity)
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

    //安全取buff
    fun safeGetBuffMap(key: String, calcEntity: CalcEntity): Double {
        return if (calcEntity.buffMap.get(key) == null) 0.0
        else calcEntity.buffMap.get(key)!!
    }
}