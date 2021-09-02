package org.phantancy.fgocalc.groupcalc.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
        //保存数据
    }

    private fun initView() {
        //缓存atktotal hptotal hpleft，会受lv fou esscen影响
        var atkTotalCache = 0
        var hpTotalCache = 0
        var hpLeftCache = 0
        vm.memberVO?.settingVO?.let {
            atkTotalCache = it.atkTotal
            hpTotalCache = it.hpTotal
            hpLeftCache = it.hpLeft
        }

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

        }
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
                    val content = genNpContent(np.npDes, "一宝")
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
        //等级 涉及atk hp的回显都要在这里设置，atkLv非常重要
        vm.getSvtExpEntities().observe(viewLifecycleOwner, Observer { expList ->
            //进度条最大值
//            binding.famSbLvSvt.setProgress(vm.memberVO.svtEntity.rewardLv.toFloat())
            vm.svtExpEntities = expList
            //回显
            vm.memberVO?.let {
                if (it.settingVO.fouAtk != 0) {
                    binding.viewFouAtk.setContent("${it.settingVO.fouAtk}")
                } else {
                    //默认1000芙芙
                    binding.viewFouAtk.setContent("${1000}")
                    vm.memberVO.settingVO.fouAtk = 1000
                    //处理总atk
                    vm.servant?.let {
                        val atk = it.atkDefault + 1000
                        binding.viewAtkTotal.setContent("${atk}")
                        vm.memberVO.settingVO.atkTotal = atk
                        vm.memberVO.settingBO.atk = atk.toDouble()
                    }
                }
                if (it.settingVO.essenceAtk != 0) {
                    binding.viewEssenceAtk.setContent("${it.settingVO.essenceAtk}")
                } else {
                    binding.viewEssenceAtk.setContent("${0}")
                    vm.memberVO.settingVO.essenceAtk = 0
                }
                binding.famSbLvSvt.setProgress(it.settingVO.lv.toFloat())
                vm.getAtkLv(vm.servant, it.settingVO.lv, expList)
                vm.getHpLv(vm.servant, it.settingVO.lv, expList)
            }

            //总atk
            if (atkTotalCache != 0) {
                vm.memberVO.settingVO.atkTotal = atkTotalCache
                vm.memberVO.settingBO.atk = atkTotalCache.toDouble()
                binding.viewAtkTotal.setContent("${atkTotalCache}")
            } else {
                //见芙芙处理
            }
            //总hp
            if (hpTotalCache != 0) {
                vm.memberVO.settingVO.hpTotal = hpTotalCache
                vm.memberVO.settingBO.hp = hpTotalCache.toDouble()
                binding.viewHpTotal.setContent("${hpTotalCache}")
            } else {
                binding.viewHpTotal.setContent("${vm.servant.hpDefault}")
                vm.memberVO.settingVO.hpTotal = vm.servant.hpDefault
                vm.memberVO.settingBO.hp = vm.servant.hpDefault.toDouble()
            }
            //剩余hp
            if (hpLeftCache != 0) {
                vm.memberVO.settingVO.hpLeft = hpLeftCache
                vm.memberVO.settingBO.hpLeft = hpLeftCache.toDouble()
                binding.viewHpLeft.setContent("${hpLeftCache}")
            } else {
                binding.viewHpLeft.setContent("${vm.servant.hpDefault}")
                vm.memberVO.settingVO.hpLeft = vm.servant.hpDefault
                vm.memberVO.settingBO.hpLeft = vm.servant.hpDefault.toDouble()
            }
            binding.famSbLvSvt.setOnSeekChangeListener(object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams) {
                    vm.memberVO?.settingVO?.lv = seekParams.progress
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
            //监听atk
            binding.viewAtkTotal.addWatcher(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(s)) {
                        vm.memberVO.settingVO.atkTotal = s.toString().toInt()
                        vm.memberVO.settingBO.atk = s.toString().toDouble()
                    }

                }

            })
            //监听hp
            binding.viewHpTotal.addWatcher(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(s)) {
                        vm.memberVO.settingVO.hpTotal = s.toString().toInt()
                        vm.memberVO.settingBO.hp = s.toString().toDouble()
                    }
                }
            })
            //监听剩余hp
            binding.viewHpLeft.addWatcher(object :TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!TextUtils.isEmpty(s)) {
                        vm.memberVO.settingVO.hpLeft = s.toString().toInt()
                        vm.memberVO.settingBO.hpLeft = s.toString().toDouble()
                    }
                }
            })

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
            val content = genNpContent(npDes as String, lv as String)
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

    private fun genNpContent(npDes: String, lv: String): String {
        return "$npDes ${lv}"
    }
}