package org.phantancy.fgocalc.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.phantancy.fgocalc.data.NoblePhantasmRepository
import org.phantancy.fgocalc.data.ServantAvatar
import org.phantancy.fgocalc.entity.CalcEntity
import org.phantancy.fgocalc.entity.CardPickEntity
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.logic.CalcLogic
import org.phantancy.fgocalc.logic.CardLogic

class GroupCalcViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "GroupCalcViewModel"
    private val npRepository: NoblePhantasmRepository = NoblePhantasmRepository(app)

    private val _svtGroup = MutableLiveData<ArrayList<ServantEntity>>()
    val svtGroup: LiveData<ArrayList<ServantEntity>> = _svtGroup

    private val servants = ArrayList<ServantEntity>()
    private val calcEntites = ArrayList<CalcEntity>()

    fun addServant(svt: ServantEntity, svtPosition: Int) {
        if (servants?.size < 3) {
            servants.add(svt)
        } else {
            servants[svtPosition] = svt;
        }
        _svtGroup.value = servants
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
                list.addAll(parsePickCards(svt, svtSource, id))
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
    private suspend fun parsePickCards(svt: ServantEntity, svtSource: Int, start: Int): ArrayList<CardPickEntity> {
        val x: String = svt.cards
        val list = ArrayList<CardPickEntity>()
        var id = start
        //解析配卡
        for (card in x.toCharArray()) {
            CardLogic.parseGroupCardPickEntity(id, card, svtSource, ServantAvatar.getServantAvatar(svt.id))?.let {
                list.add(it)
                id++
            }
        }
        //宝具卡
        val npEntityList = queryNPEntitiesList(svt.id);
        npEntityList?.let {
            CardLogic.parseGroupCardPickNp(id, it[0]?.npColor, svtSource, ServantAvatar.getServantAvatar(svt.id))?.let {
                list.add(it)
                id++
            }
        }
        return list
    }

    /**
     * pos 从者位置
     * npEntity 宝具信息
     *
     */
    fun updateServantCards(pos: Int, npEntity: NoblePhantasmEntity) {
        var count = 0;
        val list = ArrayList<CardPickEntity>()
        var id = 0
        var svtSource = 0
        viewModelScope.launch {
            for (svt in servants) {
                if (count == pos) {
                    //宝具卡色从条件里取
                    list.addAll(updatePickCards(pos, npEntity,svtSource, id))
                } else {
                    //宝具卡色从数据库里取
                    list.addAll(parsePickCards(svt, svtSource, id))
                }
                count++
                id += 6
                svtSource++
            }
            _cardPicks.value = list
        }
    }

    private fun updatePickCards(pos: Int, npEntity: NoblePhantasmEntity, svtSource: Int, start: Int): ArrayList<CardPickEntity> {
        return ArrayList<CardPickEntity>().apply {
            val svt = servants[pos]
            var id = start
            for (card in svt.cards.toCharArray()) {
                CardLogic.parseGroupCardPickEntity(id, card, svtSource, ServantAvatar.getServantAvatar(svt.id))?.let { add(it) }
                id++
            }
            CardLogic.parseGroupCardPickNp(id, npEntity.npColor, svtSource, ServantAvatar.getServantAvatar(svt.id))?.let { add(it) }
        }
    }

    private suspend fun queryNPEntitiesList(id: Int): List<NoblePhantasmEntity> {
        return coroutineScope {
            val npList = async(Dispatchers.IO) {
                npRepository.getNoblePhantasmEntitiesList(id)
            }
            npList.await()
        }
    }


    private val pickedCards = ArrayList<CardPickEntity>()
    //点击计算
    fun clickCalc(pickedCards: ArrayList<CardPickEntity>){
        pickedCards.clear()
        this.pickedCards.addAll(pickedCards)

        if (pickedCards.size == 3) {
            //计算伤害
            //伤害随机
            val dmgRandomMax = 1.1
            val dmgRandomMin = 0.9
            val calcLogic = CalcLogic()
            //res
//            val max = calcLogic.fourCardsDmg(dmgRandomMax)
//            val min = calcLogic.fourCardsDmg(dmgRandomMin)
//            Log.d(TAG,"max: $max min: $min")
        }
    }
    //伤害计算
}