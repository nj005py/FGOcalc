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
import org.phantancy.fgocalc.data.ServantAvatar
import org.phantancy.fgocalc.entity.CardPickEntity
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.entity.ServantEntity
import kotlin.coroutines.coroutineContext

class GroupCalcViewModel(app: Application) : AndroidViewModel(app) {
    private val npRepository: NoblePhantasmRepository = NoblePhantasmRepository(app)

    private val _svtGroup = MutableLiveData<ArrayList<ServantEntity>>()
    val svtGroup: LiveData<ArrayList<ServantEntity>> = _svtGroup

    val servants = ArrayList<ServantEntity>()

    fun addServant(svt: ServantEntity, svtPosition: Int) {
        if (servants?.size < 3) {
            servants.add(svt)
        } else {
            servants[svtPosition] = svt;
        }
        _svtGroup.value = servants
//        parseServantsCards(servants)
    }

    fun removeServant(svt: ServantEntity) {
        servants?.remove(svt)
        _svtGroup.value = servants
    }

    private val _cardPicks = MutableLiveData<ArrayList<CardPickEntity>>()
    val cardPicks: LiveData<ArrayList<CardPickEntity>> = _cardPicks

    /**
     * 卡片，包含归属从者，卡片位置id，编队时贴上从者头像区分卡片
     */
    //解析从者列表
    fun parseServantsCards(svts: ArrayList<ServantEntity>) {
        viewModelScope.launch {
            val list = ArrayList<CardPickEntity>()
            //归属从者
            var svtSource = 0
            //位置id
            var id = 0
            for (svt in svts) {
                list.addAll(parsePickCards(svt, svtSource,id))
                id += 6
                svtSource++
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
    suspend fun parsePickCards(svt: ServantEntity, svtSource: Int, start: Int): ArrayList<CardPickEntity> {
        val x: String = svt.cards
        val list = ArrayList<CardPickEntity>()
        var id = start
        //解析配卡
        for (card in x.toCharArray()) {
            CardLogic.parseGroupCardPickEntity(id, card,svtSource,ServantAvatar.getServantAvatar(svt.id))?.let {
                list.add(it)
                id++
            }
        }
        //宝具卡
        val npEntityList = queryNPEntitiesList(svt.id);
        npEntityList?.let {
            CardLogic.parseGroupCardPickNp(id, it[0]?.npColor,svtSource,ServantAvatar.getServantAvatar(svt.id))?.let {
                list.add(it)
                id++
            }
        }
        return list
//        _cardPicks.setValue(list as ArrayList<CardPickEntity>?)
    }

    /**
     * pos 从者位置
     * npEntity 宝具信息
     */
    fun updateServantCards(pos: Int, npEntity: NoblePhantasmEntity) {
        var count = 0;
        val list = ArrayList<CardPickEntity>()
        var id = 0
        viewModelScope.launch {
            for (i in servants) {
                if (count == pos) {
                    //宝具卡色从条件里取
                    list.addAll(updatePickCards(pos, npEntity, id))
                } else {
                    //宝具卡色从数据库里取
                    list.addAll(parsePickCards(i, id))
                }
                count++
                id++
            }
            _cardPicks.value = list
        }
    }

    fun updatePickCards(pos: Int, npEntity: NoblePhantasmEntity, id: Int): ArrayList<CardPickEntity> {
        return ArrayList<CardPickEntity>().apply {
            val svt = servants[pos]
            for (y in svt.cards.toCharArray()) {
                CardLogic.parseCardPickEntity(id, y)?.let { add(it) }
            }
            CardLogic.parseCardPickNp(id, npEntity.npColor)?.let { add(it) }
        }
    }

    //数据库查询宝具信息
    fun getNPEntities(svtId: Int): LiveData<List<NoblePhantasmEntity>> {
        return npRepository.getNoblePhantasmEntities(svtId)
    }

    private val _npEntities = MutableLiveData<List<NoblePhantasmEntity>>()
    val npEntities: LiveData<List<NoblePhantasmEntity>> = _npEntities

    private suspend fun queryNPEntitiesList(id: Int): List<NoblePhantasmEntity> {
        return coroutineScope {
            val npList = async(Dispatchers.IO) {
                npRepository.getNoblePhantasmEntitiesList(id)
            }
            npList.await()
        }
    }

}