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
        //回显数据
        //保存数据

        //阶职相性
        binding.viewAffinity.setOnClickListener {
            val picker = OptionPicker(mActy)
            picker.setData(ConditionData.affinityMap.keys.toList())
            picker.setOnOptionPickedListener { position, item ->
                binding.viewAffinity.setContent(item as String)
            }
            picker.show()
        }
        //阵营相性
        binding.viewAttribute.setOnClickListener {
            val picker = OptionPicker(mActy)
            picker.setData(ConditionData.attributeMap.keys.toList())
            picker.setOnOptionPickedListener { position, item ->
                binding.viewAttribute.setContent(item as String)
            }
            picker.show()
        }
        //选择宝具 获取宝具列表再初始化
        vm.getNpEntities().observe(viewLifecycleOwner, Observer { npList ->
            //宝具选择
            binding.viewNpSelect.setOnClickListener {
                initNpSelectDialog(npList)
            }
        })
        //等级
        //芙芙atk
        //礼装atk
        //总atk
        //总hp
        //剩余hp
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
            val content = "${npDes} ${lv}"
            binding.viewNpSelect.setContent(content)
            //数据
            val npPosition = picker.firstWheelView.currentPosition
            var npRate = 0.0
            when (lv) {
                "一宝" -> npRate = npList[npPosition].npLv1
                "二宝" -> npRate = npList[npPosition].npLv2
                "三宝" -> npRate = npList[npPosition].npLv3
                "四宝" -> npRate = npList[npPosition].npLv4
                "五宝" -> npRate = npList[npPosition].npLv5
            }
            Log.i(TAG, "宝具：${npPosition} 倍率：${npRate}")
        }
        picker.show()
    }

    private fun initSingleSelectionDialog(selections: List<String>, itemView: ListItemView){
        val picker = OptionPicker(mActy)
        picker.setData(selections)
        picker.setOnOptionPickedListener { position, item ->
            itemView.setContent(item as String)
        }
        picker.show()
    }
}