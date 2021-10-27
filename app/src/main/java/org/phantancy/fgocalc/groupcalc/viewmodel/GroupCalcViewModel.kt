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
import org.phantancy.fgocalc.common.Constant
import org.phantancy.fgocalc.common.Constant.CARD_EX
import org.phantancy.fgocalc.common.ParamsUtil
import org.phantancy.fgocalc.data.ServantAvatarData
import org.phantancy.fgocalc.data.repository.NoblePhantasmRepository
import org.phantancy.fgocalc.entity.*
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupCalcBO
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupResultNp
import org.phantancy.fgocalc.groupcalc.entity.bo.GroupResultStar
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupCalcVO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupEnemyVO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO
import org.phantancy.fgocalc.logic.CalcLogic
import org.phantancy.fgocalc.logic.CardLogic
import org.phantancy.fgocalc.util.ToastUtils

class GroupCalcViewModel(app: Application) : AndroidViewModel(app) {
    private val TAG = "GroupCalcViewModel"
    private val npRepository: NoblePhantasmRepository = NoblePhantasmRepository(app)

    private val _memberGroup = MutableLiveData<ArrayList<GroupMemberVO>>()
    val memberGroup: LiveData<ArrayList<GroupMemberVO>> = _memberGroup

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

    private suspend fun parseServantCards(svt: ServantEntity, svtPosition: Int): ArrayList<CardBO> {
        val cards: String = svt.cards
        val cardBoList = ArrayList<CardBO>()
        var position = 0
        //解析配卡
        for (card in cards.toCharArray()) {
            CardLogic.parseGroupCardBO(svt.id, svtPosition, position, card)?.let {
                cardBoList.add(it)
                position++
            }
        }
        //宝具卡
        val npEntityList = queryNPEntitiesList(svt.id);
        npEntityList?.let {
            CardLogic.parseGroupCardBONp(svt.id, svtPosition, position, it[0]?.npColor)?.let {
                cardBoList.add(it)
                position++
            }
        }
        return cardBoList
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
//        calcEntites[pos] = calcEntity
//        viewModelScope.launch {
//            for (svt in servants) {
//                if (count == pos) {
//                    //宝具卡色从条件里取
//                    list.addAll(updatePickCards(pos, calcEntity.npEntity, svtSource, id))
//                } else {
//                    //宝具卡色从数据库里取
//                    list.addAll(parsePickCards(svt, svtSource, id))
//                }
//                count++
//                id += 6
//                svtSource++
//            }
//            _cardPicks.value = list
//        }
    }

//    private fun updatePickCards(pos: Int, npEntity: NoblePhantasmEntity, svtSource: Int, start: Int): ArrayList<CardPickEntity> {
//        return ArrayList<CardPickEntity>().apply {
//            val svt = servants[pos]
//            var id = start
//            for (card in svt.cards.toCharArray()) {
//                CardLogic.parseGroupCardPickEntity(id, card, svtSource, ServantAvatarData.getServantAvatar(svt.id))?.let { add(it) }
//                id++
//            }
//            CardLogic.parseGroupCardPickNp(id, npEntity.npColor, svtSource, ServantAvatarData.getServantAvatar(svt.id))?.let { add(it) }
//        }
//    }

    private suspend fun queryNPEntitiesList(id: Int): List<NoblePhantasmEntity> {
        return coroutineScope {
            val npList = async(Dispatchers.IO) {
                npRepository.getNoblePhantasmEntitiesList(id)
            }
            npList.await()
        }
    }


    //添加编队成员
    fun addGroupMember(vo: GroupMemberVO) {
        viewModelScope.launch {
            if (_memberGroup.value == null) {
                vo.svtEntity?.let {
                    vo.cards = parseServantCards(vo.svtEntity, 0)
                }
                _memberGroup.value = arrayListOf<GroupMemberVO>(vo)
            } else {
                vo.svtEntity?.let {
                    vo.cards = parseServantCards(vo.svtEntity, _memberGroup.value!!.size)
                }
                _memberGroup.value = _memberGroup.value.run {
                    _memberGroup.value!!.add(vo)
                    this
                }
            }

        }
    }

    //移除编队成员
    fun removeMember(vo: GroupMemberVO, list: ArrayList<GroupMemberVO>) {
        list?.let {
            list.remove(vo)
            //从者位置重排列
            //遍历成员
            for ((memberPosition, member) in list.withIndex()) {
                //遍历成员卡
                member.cards?.let {
                    for (card in it) {
                        card.svtPosition = memberPosition
                        card.isVisible = true
                    }
                }
                //成员位置+1
            }
            _memberGroup.value = list
        }
    }

    //更新编队成员
    fun updateMember(vo: GroupMemberVO, list: ArrayList<GroupMemberVO>, memberPosition: Int) {
        list?.let {
            //更新成员
            list[memberPosition] = vo
            for ((index, member) in list.withIndex()) {
                //遍历成员卡
                member.cards?.let {
                    for (card in it) {
                        card.svtPosition = index
                        card.isVisible = true
                    }
                }
            }
        }
        _memberGroup.value = list
    }

    //清理选卡
    fun cleanChosenCards() {

    }

    //结果列表
    val mResultList = MutableLiveData<List<ResultEntity>>()
    val resultList: LiveData<List<ResultEntity>> = mResultList

    //清理结果
    fun cleanResult() {
//        parseServantsCards(servants)
    }

    //点击计算
    fun clickCalc(members: ArrayList<GroupMemberVO>, groupCalcVO: GroupCalcVO,enemyVO: GroupEnemyVO,
                  chosenCards: List<CardBO>, isBraveChain: Boolean) {
        /**
         * 选择的卡
         * 一些公共条件：三连增益、染色、宝具卡位置
         */
        if (chosenCards.size == 3) {

            //计算数据
            val groupCalcBO = GroupCalcBO()
            //overkill 暴击
            groupCalcBO.isOverkill1 = groupCalcVO.isOverkill1
            groupCalcBO.isOverkill2 = groupCalcVO.isOverkill2
            groupCalcBO.isOverkill3 = groupCalcVO.isOverkill3
            groupCalcBO.isOverkill4 = groupCalcVO.isOverkill4
            groupCalcBO.isCritical1 = groupCalcVO.isCritical1
            groupCalcBO.isCritical2 = groupCalcVO.isCritical2
            groupCalcBO.isCritical3 = groupCalcVO.isCritical3
            groupCalcBO.chosenCards.addAll(chosenCards)
            //敌方
            groupCalcBO.enemyCount = enemyVO.enemyCount
            groupCalcBO.enemysNpMod = enemyVO.enemysNpMod
            groupCalcBO.enemysStarMod = enemyVO.enemysStarMod
            val card1 = chosenCards[0]
            if (isBraveChain) {
                val card4 = CardBO().apply {
                    type = CARD_EX
                    svtId = card1.svtId
                    svtPosition = card1.svtPosition
                }
                groupCalcBO.chosenCards.add(card4)
            }
            //分析卡片
            for (card in groupCalcBO.chosenCards) {
                groupCalcBO.chosenSetting.add(members[card.svtPosition].settingBO)
                groupCalcBO.chosenServants.add(members[card.svtPosition].svtEntity)
            }
            //校验详细设置
            for ((index, setting) in groupCalcBO.chosenSetting.withIndex()) {
                if (setting.atk == 0.0) {
                    ToastUtils.showToast("请设置${groupCalcBO.chosenServants[index].name}详细设置")
                    return
                }
            }
            //是否同色
            groupCalcBO.isSameColor = ParamsUtil.isCardsSameColor(groupCalcBO.chosenCards[0].type,
                    groupCalcBO.chosenCards[1].type, groupCalcBO.chosenCards[2].type)
            //是否红链
            groupCalcBO.isBusterChain = ParamsUtil.isCardsBusterChain(groupCalcBO.chosenCards[0].type,
                    groupCalcBO.chosenCards[1].type, groupCalcBO.chosenCards[2].type)
            //首卡颜色
            groupCalcBO.firstCardType = ParamsUtil.getCardColor(card1.type)
            val calcLogic = CalcLogic()
            //计算伤害
            //伤害随机
            val dmgRandomMax = 1.1
            val dmgRandomMin = 0.9
            val dmgRandomAvg = 1.0
            val max = calcLogic.cardsDmg(dmgRandomMax, groupCalcBO, isBraveChain)
            val min = calcLogic.cardsDmg(dmgRandomMin, groupCalcBO, isBraveChain)
            val avg = calcLogic.cardsDmg(dmgRandomAvg, groupCalcBO, isBraveChain)
            //np计算
            val np = calcLogic.cardsNp(groupCalcBO, isBraveChain)
            //打星计算
            val star = calcLogic.cardsStar(groupCalcBO, isBraveChain)
            handleResult(members, min, max, avg, groupCalcBO, isBraveChain, np,star)
        }

    }

    fun handleResult(members: ArrayList<GroupMemberVO>, min: ResultDmg, max: ResultDmg,
                     avg: ResultDmg, groupCalcBO: GroupCalcBO, isBraveChain: Boolean,
                     np: GroupResultNp, star: GroupResultStar) {
        val resList: ArrayList<ResultEntity> = ArrayList()
        val res1 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcBO.chosenCards[0].type, min.c1, max.c1, avg.c1, np.c1, star.c1,
                "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[0].id))
        val res2 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcBO.chosenCards[1].type, min.c2, max.c2, avg.c2, np.c2, star.c2,
                "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[1].id))
        val res3 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcBO.chosenCards[2].type, min.c3, max.c3, avg.c3, np.c3, star.c3,
                "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[2].id))
        resList.add(res1)
        resList.add(res2)
        resList.add(res3)
        //有ex卡
        if (isBraveChain) {
            val res4 = ResultEntity(ResultEntity.TYPE_CARD,
                    Constant.CARD_EX, min.c4, max.c4, avg.c4, np.c4, star.c4,
                    "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[3].id))
            resList.add(res4)
        }
        //从者总结
        members?.let {
            for ((index, member) in it.withIndex()) {
                member.svtEntity?.let {
                    val sumMax = statisticDmg(member.cards[0].svtPosition, max, groupCalcBO.chosenCards)
                    val sumMin = statisticDmg(member.cards[0].svtPosition, max, groupCalcBO.chosenCards)
                    val sumAvg = statisticDmg(member.cards[0].svtPosition, max, groupCalcBO.chosenCards)
                    val sumNp = statisticNp(member.cards[0].svtPosition,np,groupCalcBO.chosenCards)
                    val sumStar = statisticStar(member.cards[0].svtPosition,star,groupCalcBO.chosenCards)
                    val sum = "${member.svtEntity.name}总结 \n${sumMin}-${sumMax} ${sumAvg} \n" +
                            "np获取${ParamsUtil.npGenResFormat(sumNp)}\n" +
                            "打星获取${ParamsUtil.starDropResFormat(sumStar)}"
                    resList.add(ResultEntity(type = ResultEntity.TYEP_SUM, sum = sum, avatar = ServantAvatarData.getServantAvatar(it.id)))
                }
            }
        }
        mResultList.value = resList
    }

    //总结伤害
    fun statisticDmg(svtPosition: Int, res: ResultDmg, chosenCards: List<CardBO>): Int {
        val list = ArrayList<Int>()
        list.add(res.c1.toInt())
        list.add(res.c2.toInt())
        list.add(res.c3.toInt())
        if (!res.c4.isNullOrEmpty()) {
            list.add(res.c4.toInt())
        }
        var sum = 0
        for ((index, card) in chosenCards.withIndex()) {
            if (card.svtPosition == svtPosition) {
                sum += list[index]
            }
        }
        return sum
    }

    //总结np
    fun statisticNp(svtPosition: Int, res: GroupResultNp, chosenCards: List<CardBO>): Double {
        val list = ArrayList<Double>()
        list.add(res.c1Data)
        list.add(res.c2Data)
        list.add(res.c3Data)
        if (!res.c4.isNullOrEmpty()) {
            list.add(res.c4Data)
        }
        var sum = 0.0
        for ((index, card) in chosenCards.withIndex()) {
            if (card.svtPosition == svtPosition) {
                sum += list[index]
            }
        }
        return sum
    }

    //总结打星
    fun statisticStar(svtPosition: Int, res: GroupResultStar, chosenCards: List<CardBO>): Double {
        val list = ArrayList<Double>()
        list.add(res.c1Data)
        list.add(res.c2Data)
        list.add(res.c3Data)
        if (!res.c4.isNullOrEmpty()) {
            list.add(res.c4Data)
        }
        var sum = 0.0
        for ((index, card) in chosenCards.withIndex()) {
            if (card.svtPosition == svtPosition) {
                sum += list[index]
            }
        }
        return sum
    }
    /**
     * c1 c2 c3
     * 1a 2a 1a
     * 总结svt1:
     * 1min = c1+c3
     * 1max = c1+c3
     * 1min-1max
     * 总结svt2:
     * 2min = c2
     * 2max = c2
     * 2min-2max
     */
}