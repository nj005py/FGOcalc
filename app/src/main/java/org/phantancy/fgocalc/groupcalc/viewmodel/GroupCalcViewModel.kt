package org.phantancy.fgocalc.groupcalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.phantancy.fgocalc.data.repository.NoblePhantasmRepository
import org.phantancy.fgocalc.data.ServantAvatarData
import org.phantancy.fgocalc.entity.*
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupCalcBO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO
import org.phantancy.fgocalc.logic.CalcLogic
import org.phantancy.fgocalc.logic.CardLogic

class GroupCalcViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "GroupCalcViewModel"
    private val npRepository: NoblePhantasmRepository = NoblePhantasmRepository(app)

    private val _svtGroup = MutableLiveData<ArrayList<ServantEntity>>()
    val svtGroup: LiveData<ArrayList<ServantEntity>> = _svtGroup

    private val servants = ArrayList<ServantEntity>()
    val calcEntites = ArrayList<CalcEntity>()

    val groupCalcEntity = GroupCalcBO()

    private val _memberGroup = MutableLiveData<ArrayList<GroupMemberVO>>()
    val memberGroup: LiveData<ArrayList<GroupMemberVO>> = _memberGroup

    init {
        calcEntites.apply {
            add(CalcEntity())
            add(CalcEntity())
            add(CalcEntity())
        }
    }

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
            CardLogic.parseGroupCardPickEntity(id, card, svtSource, ServantAvatarData.getServantAvatar(svt.id))?.let {
                list.add(it)
                id++
            }
        }
        //宝具卡
        val npEntityList = queryNPEntitiesList(svt.id);
        npEntityList?.let {
            CardLogic.parseGroupCardPickNp(id, it[0]?.npColor, svtSource, ServantAvatarData.getServantAvatar(svt.id))?.let {
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
    fun updateServantCards(pos: Int, calcEntity: CalcEntity) {
        var count = 0;
        val list = ArrayList<CardPickEntity>()
        var id = 0
        var svtSource = 0
        calcEntites[pos] = calcEntity
        viewModelScope.launch {
            for (svt in servants) {
                if (count == pos) {
                    //宝具卡色从条件里取
                    list.addAll(updatePickCards(pos, calcEntity.npEntity, svtSource, id))
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
                CardLogic.parseGroupCardPickEntity(id, card, svtSource, ServantAvatarData.getServantAvatar(svt.id))?.let { add(it) }
                id++
            }
            CardLogic.parseGroupCardPickNp(id, npEntity.npColor, svtSource, ServantAvatarData.getServantAvatar(svt.id))?.let { add(it) }
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

    //添加编队成员
    fun addGroupMember(vo: GroupMemberVO) {
        if (_memberGroup.value == null) {
            val list = arrayListOf<GroupMemberVO>(vo)
            _memberGroup.value = list
        } else {
            _memberGroup.value = _memberGroup.value.run {
                _memberGroup.value!!.add(vo)
                this
            }
        }
    }

    //移除编队成员
    fun removeMember(vo: GroupMemberVO) {
        _memberGroup.value = _memberGroup.value?.run {
            _memberGroup.value!!.remove(vo)
            this
        }
    }

    //结果列表
    val mResultList = MutableLiveData<List<ResultEntity>>()
    val resultList: LiveData<List<ResultEntity>> = mResultList

    //清理结果
    fun cleanResult() {
        parseServantsCards(servants)
    }

    //点击计算
    fun clickCalc(pickedCards: ArrayList<CardPickEntity>, isBraveChain: Boolean) {
        this.pickedCards.clear()
        this.pickedCards.addAll(pickedCards)

        if (pickedCards.size == 3) {
            //设置卡片
            groupCalcEntity.cardType1 = pickedCards[0].name
            groupCalcEntity.cardType2 = pickedCards[1].name
            groupCalcEntity.cardType3 = pickedCards[2].name
            //计算伤害
            //伤害随机
            val dmgRandomMax = 1.1
            val dmgRandomMin = 0.9
            val calcLogic = CalcLogic()
            //选中卡片对应的从者
            val pickedServants = listOf(servants[pickedCards[0].svtSource], servants[pickedCards[1].svtSource],
                    servants[pickedCards[2].svtSource])
            //res
            val max = calcLogic.fourCardsDmg(dmgRandomMax, calcEntites, groupCalcEntity, pickedServants, isBraveChain)
            val min = calcLogic.fourCardsDmg(dmgRandomMin, calcEntites, groupCalcEntity, pickedServants, isBraveChain)
//            Log.d(TAG,"max: $max min: $min")
            handleResult(min, max, pickedServants)

        }
    }
    //伤害计算

    fun handleResult(min: ResultDmg, max: ResultDmg, pickedServants: List<ServantEntity>) {
        val res1 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcEntity.cardType1, min.c1, max.c1, "np", "star",
                "", ServantAvatarData.getServantAvatar(pickedServants[0].id))
        val res2 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcEntity.cardType2, min.c2, max.c2, "np", "star",
                "", ServantAvatarData.getServantAvatar(pickedServants[1].id))
        val res3 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcEntity.cardType3, min.c3, max.c3, "np", "star",
                "", ServantAvatarData.getServantAvatar(pickedServants[2].id))
        val res4 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcEntity.cardType4, min.c4, max.c4, "np", "star",
                "", ServantAvatarData.getServantAvatar(pickedServants[0].id))
        val sumBuilder = StringBuilder()
        sumBuilder.append("伤害总计：")
                .append(min.sum)
                .append("-")
                .append(max.sum)
                .append("\n")
        sumBuilder.append("np总计：")
                .append("np")
                .append("\n")
        sumBuilder.append("打星总计：")
                .append("star.sum")
                .append("\n")
        val resSum = ResultEntity(ResultEntity.TYEP_SUM,
                "", "", "", "", "", sumBuilder.toString(), ServantAvatarData.getServantAvatar(pickedServants[0].id));
        val list: ArrayList<ResultEntity> = ArrayList()
        list.add(res1)
        list.add(res2)
        list.add(res3)
        list.add(res4)
        list.add(resSum)
        mResultList.value = list
    }
}