package org.phantancy.fgocalc.groupcalc.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.gzuliyujiang.wheelpicker.LinkagePicker
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelpicker.contract.LinkageProvider
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import org.phantancy.fgocalc.data.ConditionData
import org.phantancy.fgocalc.databinding.FragmentGroupMemberSettingBinding
import org.phantancy.fgocalc.entity.NoblePhantasmEntity
import org.phantancy.fgocalc.fragment.LazyFragment
import org.phantancy.fgocalc.groupcalc.viewmodel.GroupSettingViewModel
import org.phantancy.fgocalc.view.ListItemView

/**
 * 编队从者条件
 */
class GroupMemberSettingFragment : LazyFragment() {
    private lateinit var binding: FragmentGroupMemberSettingBinding
    private lateinit var vm: GroupSettingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupMemberSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun init() {
        super.init()
        vm = ViewModelProvider(mActy).get(GroupSettingViewModel::class.java)
        initView()
        //回显数据
        initData()
        //保存数据
    }

    private fun initData() {
        vm.memberVO?.let {
            //阶职相性
            if (it.settingVO.affinity.isNotEmpty()) {
                binding.viewAffinity.setContent(it.settingVO.affinity)
            } else {
                //默认值
                val key = ConditionData.getAffinityKeys()[0]
                val value = ConditionData.getAffinityValues()[0]
                binding.viewAffinity.setContent(key)
                it.settingVO.affinity = key
                it.settingBO.affinityMod = value
            }
            //阵营相性
            if (it.settingVO.attribute.isNotEmpty()) {
                binding.viewAttribute.setContent(it.settingVO.attribute)
            } else {
                val key = ConditionData.getAttributeKeys()[0]
                val value = ConditionData.getAffinityValues()[0]
                binding.viewAttribute.setContent(key)
                it.settingVO.attribute = key
                it.settingBO.attributeMod = value
            }
            //选择宝具 获取宝具列表再初始化

            //等级
            //芙芙atk
            //礼装atk
            //总atk
            if (it.settingVO.atkTotal != 0) {
                binding.viewAtkTotal.setContent("${it.settingVO.atkTotal}")
            } else {
                vm.servant?.let {
                    binding.viewAtkTotal.setContent("${it.atkDefault}")
                    vm.memberVO.settingVO.atkTotal = it.atkDefault
                    vm.memberVO.settingBO.atk = it.atkDefault.toDouble()
                }
            }
            //总hp
            //剩余hp
            if (it.settingVO.hpTotal != 0) {
                binding.viewHpTotal.setContent("${it.settingVO.hpTotal}")
            }
            if (it.settingVO.hpLeft != 0){
                binding.viewHpLeft.setContent("${it.settingVO.hpLeft}")
            }
            vm.servant?.let {
                //atk hp
                binding.viewHpTotal.setContent("${it.hpDefault}")
                binding.viewHpLeft.setContent("${it.hpDefault}")
            }
        }

    }

    private fun initView() {
        //阶职相性
        binding.viewAffinity.setOnClickListener {
            val picker = OptionPicker(mActy)
            picker.setData(ConditionData.affinityMap.keys.toList())
            picker.setOnOptionPickedListener { position, item ->
                binding.viewAffinity.setContent(item as String)
                vm.memberVO.settingVO.affinity = item as String
                vm.memberVO.settingBO.affinityMod = ConditionData.affinityMap[item]!!
            }
            picker.show()
        }
        //阵营相性
        binding.viewAttribute.setOnClickListener {
            val picker = OptionPicker(mActy)
            picker.setData(ConditionData.attributeMap.keys.toList())
            picker.setOnOptionPickedListener { position, item ->
                binding.viewAttribute.setContent(item as String)
                vm.memberVO?.settingVO?.attribute = item
                vm.memberVO?.settingBO?.attributeMod = ConditionData.attributeMap[item]!!
            }
            picker.show()
        }
        //选择宝具 获取宝具列表再初始化
        vm.getNpEntities().observe(viewLifecycleOwner, Observer { npList ->
            //回显
            vm.memberVO?.let {
                if (it.settingVO.npDes.isNotEmpty()) {
                    binding.viewNpSelect.setContent(it.settingVO.npDes)
                } else {
                    //默认宝具1，lv1
                    val np = npList[0]
                    val content = genNpContent(np.npDes,"一宝")
                    binding.viewNpSelect.setContent(content)
                    vm.memberVO.settingVO.npDes = content
                    vm.memberVO.settingBO.npEntity = np
                    vm.memberVO.settingBO.npDmgMultiplier = np.npLv1
                }
            }

            //宝具选择
            binding.viewNpSelect.setOnClickListener {
                initNpSelectDialog(npList)
            }
        })
        //等级
        vm.getSvtExpEntities().observe(viewLifecycleOwner, Observer { expList ->
            //进度条最大值
//            binding.famSbLvSvt.setProgress(vm.memberVO.svtEntity.rewardLv.toFloat())
            //回显
            vm.memberVO?.let {
                if (it.settingVO.fouAtk != 0) {
                    binding.viewFouAtk.setContent("${it.settingVO.fouAtk}")
                }
                if (it.settingVO.essenceAtk != 0) {
                    binding.viewEssenceAtk.setContent("${it.settingVO.essenceAtk}")
                }
                binding.famSbLvSvt.setProgress(it.settingVO.lv)
            }
            vm.svtExpEntities = expList
            binding.famSbLvSvt.setOnSeekChangeListener(object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams) {
                    vm.memberVO?.settingVO?.lv = seekParams.progressFloat
                    binding.viewAtkTotal.setContent("${vm.onAtkLvChanged(seekParams.progress)}")
                    binding.viewHpTotal.setContent("${vm.onHpLvChanged(seekParams.progress)}")
                }

                override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
                override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {}
            })
            //芙芙atk
            binding.viewFouAtk.setOnClickListener {
                val picker = OptionPicker(mActy)
                picker.setData(ConditionData.fouAtkMap.keys.toList())
                picker.setOnOptionPickedListener { position, item ->
                    binding.viewFouAtk.setContent(item as String)
                    val res = vm.onFouAtkChanged(ConditionData.getFouAtkValues()[position])
                    binding.viewAtkTotal.setContent("$res")
                }
                picker.show()
            }
            //礼装atk
            binding.viewEssenceAtk.setOnClickListener {
                val picker = OptionPicker(mActy)
                picker.setData(ConditionData.essenceAtkMap.keys.toList())
                picker.setOnOptionPickedListener { position, item ->
                    binding.viewEssenceAtk.setContent(item as String)
                    val res = vm.onEssenceAtkChanged(ConditionData.getEssenceAtkValues()[position])
                    binding.viewAtkTotal.setContent("$res")
                }
                picker.show()
            }
            //总atk
            //总hp
            //剩余hp
        })

    }

    //宝具选择弹窗
    private fun initNpSelectDialog(npList: List<NoblePhantasmEntity>) {
        //获取宝具列表
        val picker = LinkagePicker(mActy)
        picker.setData(object : LinkageProvider {
            override fun firstLevelVisible(): Boolean {
                return true
            }

            override fun thirdLevelVisible(): Boolean {
                return false
            }

            override fun provideFirstData(): List<String> {
                //宝具描述列表
                val list = ArrayList<String>()
                for (np in npList) {
                    list.add(np.npDes)
                }
                return list
            }

            override fun linkageSecondData(firstIndex: Int): List<String> {
                return ConditionData.npLvKeys.asList()
            }

            override fun findFirstIndex(firstValue: Any?): Int {
                if (firstValue == null) {
                    return LinkageProvider.INDEX_NO_FOUND
                }
                for ((index, np) in npList.withIndex()) {
                    if (np.id == (firstValue as NoblePhantasmEntity).id) {
                        return index
                    }
                }
                return LinkageProvider.INDEX_NO_FOUND
            }

            override fun findSecondIndex(firstIndex: Int, secondValue: Any?): Int {
                if (secondValue == null) {
                    return LinkageProvider.INDEX_NO_FOUND
                }
                for ((index, lv) in ConditionData.npLvKeys.withIndex()) {
                    if (lv.equals(secondValue)) {
                        return index
                    }
                }
                return LinkageProvider.INDEX_NO_FOUND
            }

            override fun findThirdIndex(firstIndex: Int, secondIndex: Int, thirdValue: Any?): Int {
                return 0
            }

            override fun linkageThirdData(firstIndex: Int, secondIndex: Int): List<Any> {
                return ArrayList()
            }
        })
        picker.setOnLinkagePickedListener { npDes, lv, _ ->
            //显示
            val content = genNpContent(npDes as String,lv as String)
            binding.viewNpSelect.setContent(content)
            vm.memberVO.settingVO.npDes = content
            //数据
            val npPosition = picker.firstWheelView.currentPosition
            var npDmgMultiplier = 0.0
            var np = npList[npPosition]
            when (lv) {
                "一宝" -> npDmgMultiplier = np.npLv1
                "二宝" -> npDmgMultiplier = np.npLv2
                "三宝" -> npDmgMultiplier = np.npLv3
                "四宝" -> npDmgMultiplier = np.npLv4
                "五宝" -> npDmgMultiplier = np.npLv5
            }
            vm.memberVO.settingBO.npEntity = np
            vm.memberVO.settingBO.npDmgMultiplier = npDmgMultiplier
            //更换宝具卡
            val npCardPosition = vm.memberVO.cards.size - 1
            val npCardBO = vm.memberVO.cards[npCardPosition]
            npCardBO.type = np.npColor
            vm.memberVO.cards[npCardPosition] = npCardBO
            Log.i(TAG, "宝具：${npPosition} 倍率：${npDmgMultiplier}")
        }
        picker.show()
    }

    private fun initSingleSelectionDialog(selections: List<String>, itemView: ListItemView) {
        val picker = OptionPicker(mActy)
        picker.setData(selections)
        picker.setOnOptionPickedListener { position, item ->
            itemView.setContent(item as String)
        }
        picker.show()
    }

    private fun genNpContent(npDes:String, lv: String): String{
        return "$npDes ${lv}"
    }
}