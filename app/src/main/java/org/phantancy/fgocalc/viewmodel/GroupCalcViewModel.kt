package org.phantancy.fgocalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.phantancy.fgocalc.common.CardLogic
import org.phantancy.fgocalc.data.NoblePhantasmRepository
import org.phantancy.fgocalc.entity.CardPickEntity
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.entity.ServantEntity
import kotlin.coroutines.coroutineContext

class GroupCalcViewModel(app: Application) : AndroidViewModel(app) {
    private val npRepository: NoblePhantasmRepository = NoblePhantasmRepository(app)

    private val _svtGroup = MutableLiveData<ArrayList<ServantEntity>>()
    val svtGroup: LiveData<ArrayList<ServantEntity>> = _svtGroup

    val servants = ArrayList<ServantEntity>()

    fun addServant(svt: ServantEntity,svtPosition: Int) {
        if (servants?.size < 3) {
            servants.add(svt)
        } else {
            servants[svtPosition] = svt;
        }
        _svtGroup.value = servants
//        parseServantsCards(servants)
    }

    fun removeServant(svt: ServantEntity){
        servants?.remove(svt)
        _svtGroup.value = servants
    }

    private val _cardPicks = MutableLiveData<ArrayList<CardPickEntity>>()
    val cardPicks: LiveData<ArrayList<CardPickEntity>> = _cardPicks

    //解析从者列表
    fun parseServantsCards(svts: ArrayList<ServantEntity>) {
        viewModelScope.launch {
            val list = ArrayList<CardPickEntity>()
            for (svt in svts) {
                list.addAll(parsePickCards(svt))
            }
            _cardPicks.value = list
        }
    }

    //解析单个从者信息
    /**
     * 解析从者卡片
     * 1 获得svt item
     * 2 解析配卡
     * 3 查询宝具数据
     * 4 配卡+第一张宝具卡组成卡片列表
     * 5 更新livedata
     */
    suspend fun parsePickCards(svt: ServantEntity): ArrayList<CardPickEntity> {
        val x: String = svt.cards
        var id = 0
        val list = ArrayList<CardPickEntity>()
        //解析配卡
        for (y in x.toCharArray()) {
            CardLogic.parseCardPickEntity(id, y)?.let { list.add(it) }
            id++
        }
        //宝具卡
        val npEntityList = queryNPEntitiesList(svt.id);
        npEntityList?.let {
            CardLogic.parseCardPickNp(id, it[0]?.npColor)?.let { list.add(it) }
        }
        return list
//        _cardPicks.setValue(list as ArrayList<CardPickEntity>?)
    }



    //数据库查询宝具信息
    fun getNPEntities(svtId: Int): LiveData<List<NoblePhantasmEntity>> {
        return npRepository.getNoblePhantasmEntities(svtId)
    }

    private val _npEntities = MutableLiveData<List<NoblePhantasmEntity>>()
    val npEntities:LiveData<List<NoblePhantasmEntity>> = _npEntities

    private suspend fun queryNPEntitiesList(id: Int): List<NoblePhantasmEntity>{
        return coroutineScope  {
            val npList = async(Dispatchers.IO) {
                npRepository.getNoblePhantasmEntitiesList(id)
            }
            npList.await()
        }
    }

}