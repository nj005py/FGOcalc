package org.phantancy.fgocalc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.phantancy.fgocalc.common.CardLogic
import org.phantancy.fgocalc.entity.CardPickEntity
import org.phantancy.fgocalc.entity.ServantEntity

class GroupCalcViewModel : ViewModel() {
    private val _svtGroup = MutableLiveData<ArrayList<ServantEntity>>()
    val svtGroup:LiveData<ArrayList<ServantEntity>> = _svtGroup

    val servants = ArrayList<ServantEntity>()
    fun addServant(svt: ServantEntity){
//        _svtGroup.value?.let {
//            var list:ArrayList<ServantEntity> = it
//            list?.add(svt)
//            _svtGroup.value = list
//        }
        servants.add(svt)
        parseServantsCards(servants)
    }

    private val _cardPicks = MutableLiveData<ArrayList<CardPickEntity>>()
    val cardPicks:LiveData<ArrayList<CardPickEntity>> = _cardPicks

    fun parsePickCards(svt: ServantEntity):ArrayList<CardPickEntity> {
        val x: String = svt.cards
        var id = 0
        val list = ArrayList<CardPickEntity>()
        //平A
        for (y in x.toCharArray()) {
            CardLogic.parseCardPickEntity(id, y)?.let { list.add(it) }
            id++
        }
        return list
        //宝具
//        if (calcEntity.getNpEntity() != null) {
//            CardLogic.parseCardPickNp(id, calcEntity.getNpEntity().npColor)?.let { list.add(it) }
//        }
//        _cardPicks.setValue(list as ArrayList<CardPickEntity>?)
    }

    fun parseServantsCards(svts: ArrayList<ServantEntity>) {
        val list = ArrayList<CardPickEntity>()
        for (svt in svts){
            list.addAll(parsePickCards(svt))
        }
        _cardPicks.value = list
    }
}