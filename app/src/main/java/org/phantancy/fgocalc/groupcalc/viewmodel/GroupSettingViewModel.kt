package org.phantancy.fgocalc.groupcalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import org.phantancy.fgocalc.data.repository.CalcRepository
import org.phantancy.fgocalc.data.repository.NoblePhantasmRepository
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.entity.SvtExpEntity
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO

class GroupSettingViewModel(app: Application) : AndroidViewModel(app) {
    private var npRepository: NoblePhantasmRepository
    private var calcRepository: CalcRepository

    var servant : ServantEntity = ServantEntity()
    var memberVO: GroupMemberVO = GroupMemberVO()

    //经验列表
    var svtExpEntities: List<SvtExpEntity> = ArrayList()
    init {
        npRepository = NoblePhantasmRepository(app)
        calcRepository = CalcRepository(app)
    }

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
}