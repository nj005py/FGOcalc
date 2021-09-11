package org.phantancy.fgocalc.groupcalc.viewmodel

import android.app.Application
import android.widget.Toast
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
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupCalcVO
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
            for ((memberPosition, member) in list.withIndex()){
                //遍历成员卡
                member.cards?.let {
                    for (card in it){
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
    fun updateMember(vo: GroupMemberVO, list: ArrayList<GroupMemberVO>, memberPosition: Int){
        list?.let {
            //更新成员
            list[memberPosition] = vo
            for ((index, member) in list.withIndex()){
                //遍历成员卡
                member.cards?.let {
                    for (card in it){
                        card.svtPosition = index
                        card.isVisible = true
                    }
                }
            }
        }
        _memberGroup.value = list
    }

    //清理选卡
    fun cleanChosenCards(){

    }

    //结果列表
    val mResultList = MutableLiveData<List<ResultEntity>>()
    val resultList: LiveData<List<ResultEntity>> = mResultList

    //清理结果
    fun cleanResult() {
//        parseServantsCards(servants)
    }

    //点击计算
    fun clickCalc(members: ArrayList<GroupMemberVO>, groupCalcVO:GroupCalcVO,
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
            for (card in groupCalcBO.chosenCards){
                groupCalcBO.chosenSetting.add(members[card.svtPosition].settingBO)
                groupCalcBO.chosenServants.add(members[card.svtPosition].svtEntity)
            }
            //校验详细设置
            for ((index,setting) in groupCalcBO.chosenSetting.withIndex()){
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
            val max = calcLogic.cardsDmg(dmgRandomMax,groupCalcBO, isBraveChain)
            val min = calcLogic.cardsDmg(dmgRandomMin,groupCalcBO, isBraveChain)
            handleResult(min,max,groupCalcBO)
        }

    }
    //伤害计算

    fun handleResult(min: ResultDmg, max: ResultDmg, groupCalcBO: GroupCalcBO) {
        val res1 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcBO.chosenCards[0].type, min.c1, max.c1, "np", "star",
                "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[0].id))
        val res2 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcBO.chosenCards[1].type, min.c2, max.c2, "np", "star",
                "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[1].id))
        val res3 = ResultEntity(ResultEntity.TYPE_CARD,
                groupCalcBO.chosenCards[2].type, min.c3, max.c3, "np", "star",
                "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[2].id))
        var res4 = ResultEntity(ResultEntity.TYPE_CARD,
                Constant.CARD_QUICK, "", "", "np", "star",
                "", ServantAvatarData.getServantAvatar(4))
        if (groupCalcBO.chosenCards.size == 4) {
           res4  = ResultEntity(ResultEntity.TYPE_CARD,
                    groupCalcBO.chosenCards[3].type, min.c4, max.c4, "np", "star",
                    "", ServantAvatarData.getServantAvatar(groupCalcBO.chosenServants[3].id))
        }

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
                "", "", "", "", "", sumBuilder.toString(), ServantAvatarData.getServantAvatar(2));
        val list: ArrayList<ResultEntity> = ArrayList()
        list.add(res1)
        list.add(res2)
        list.add(res3)
        list.add(res4)
        list.add(resSum)
        mResultList.value = list
    }
}