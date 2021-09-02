package org.phantancy.fgocalc.groupcalc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.phantancy.fgocalc.data.repository.CalcRepository
import org.phantancy.fgocalc.data.repository.NoblePhantasmRepository
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.entity.SvtExpEntity
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO

class GroupSettingViewModel(app: Application) : AndroidViewModel(app) {
    private var npRepository: NoblePhantasmRepository = NoblePhantasmRepository(app)
    private var calcRepository: CalcRepository = CalcRepository(app)

    var servant : ServantEntity = ServantEntity()
    var memberVO: GroupMemberVO = GroupMemberVO()

    //经验列表
    var svtExpEntities: List<SvtExpEntity> = ArrayList()

    //获取宝具列表
    fun getNpEntities(): LiveData<List<NoblePhantasmEntity>> {
        return npRepository.getNoblePhantasmEntities(servant.id)
    }

    //选宝具，宝具等级，解析内容
    fun parseNpBuff(it: NoblePhantasmEntity, lv: String) {
        //buff随宝具
        var lvBuff: String = ""
        when (lv) {
            "一宝" -> lvBuff = it.ocBuffLv1
            "二宝" -> lvBuff = it.ocBuffLv2
            "三宝" -> lvBuff = it.ocBuffLv3
            "四宝" -> lvBuff = it.ocBuffLv4
            "五宝" -> lvBuff = it.ocBuffLv5
        }
    }

    //从者等级成长值
    fun getSvtExpEntities(): LiveData<List<SvtExpEntity>>{
        return calcRepository.getSvtExpList(servant.id)
    }

    //合计条件atk
    fun sumAtk(): Int {
        //等级atk+礼装atk+芙芙atk
        val res: Int = memberVO.settingVO.atkLv + memberVO.settingVO.essenceAtk + memberVO.settingVO.fouAtk
        memberVO.settingVO.atkTotal = res
        memberVO.settingBO.atk = res.toDouble()
        return res
    }

    //芙芙atk变化
    fun onFouAtkChanged(fou: Int): Int {
        memberVO.settingVO.fouAtk = fou
        return sumAtk()
    }

    //礼装atk变化
    fun onEssenceAtkChanged(essence: Int): Int {
        memberVO.settingVO.essenceAtk = essence
        return sumAtk()
    }

    //等级变化，atk变化
    fun onAtkLvChanged(lv: Int): Int {
        memberVO.settingVO.atkTotal = getAtkLv(servant, lv, svtExpEntities)
        return sumAtk()
    }

    //等级变化，hp变化
    fun onHpLvChanged(lv: Int): Int {
        memberVO.settingVO.hpTotal = getHpLv(servant, lv, svtExpEntities)
        return memberVO.settingVO.hpTotal
    }

    //依据等级获取atk
    fun getAtkLv(sItem: ServantEntity, lv: Int, curveList: List<SvtExpEntity>?): Int {
        if (lv > 0 && curveList != null) {
            val curve = curveList[lv].curve
            val atkBase = sItem.atkBase
            val atkDefault = sItem.atkDefault
            memberVO.settingVO.atkLv = (atkBase + (atkDefault.toFloat() - atkBase.toFloat()) / 1000 * curve).toInt()
            return memberVO.settingVO.atkLv
        }
        return 0
    }

    //依据等级获取hp
    fun getHpLv(sItem: ServantEntity, lv: Int, curveList: List<SvtExpEntity>?): Int {
        if (lv > 0 && curveList != null) {
            val curve = curveList[lv].curve
            val hpBase = sItem.hpBase
            val hpDefault = sItem.hpDefault
            memberVO.settingVO.hpLv = (hpBase + (hpDefault.toFloat() - hpBase.toFloat()) / 1000 * curve).toInt()
            return memberVO.settingVO.hpLv
        }
        return 0
    }
}