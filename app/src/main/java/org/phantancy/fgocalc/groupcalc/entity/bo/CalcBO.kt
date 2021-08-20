package org.phantancy.fgocalc.groupcalc.entity.bo

import androidx.collection.SimpleArrayMap
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import java.io.Serializable

data class CalcBO(
        //职阶克制
        var affinityMod: Double = 2.0,
        //阵营克制
        var attributeMod: Double = 1.0,
        //选择宝具
        var npEntity: NoblePhantasmEntity,
        //宝具伤害倍率
        var npDmgMultiplier: Double = 0.0,
        //总atk
        var atk: Double = 0.0,
        //总hp
        var hp: Double = 0.0,
        //剩余hp
        var hpLeft: Double = 0.0,
        var buffMap: SimpleArrayMap<String, Double>
) : Serializable {
}