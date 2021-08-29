package org.phantancy.fgocalc.groupcalc.entity.vo

import com.github.gzuliyujiang.wheelpicker.OptionPicker
import org.phantancy.fgocalc.data.ConditionData

//阶职相性
//阵营相性
//选择宝具 获取宝具列表再初始化
//等级
//芙芙atk
//礼装atk
//总atk
//总hp
//剩余hp
class GroupMemberSettingVO {
    //阶职相性
    var affinity = ""
    //阵营相性
    var attribute = ""
    //选择宝具 哪个宝具？几宝？
    var npDes = ""
    //等级
    var lv = 1f
    //芙芙atk
    var fouAtk = 0
    //礼装atk
    var essenceAtk = 0
    //总atk
    var atkTotal = 0
    //总hp
    var hpTotal = 0
    //剩余hp
    var hpLeft = 0
}