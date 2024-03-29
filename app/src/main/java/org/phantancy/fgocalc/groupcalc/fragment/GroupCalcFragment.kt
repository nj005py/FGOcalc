package org.phantancy.fgocalc.groupcalc.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import org.phantancy.fgocalc.activity.BaseActy
import org.phantancy.fgocalc.groupcalc.activity.GroupMemberSettingActy
import org.phantancy.fgocalc.activity.SearchServantActy
import org.phantancy.fgocalc.adapter.*
import org.phantancy.fgocalc.databinding.FragmentGroupCalcBinding
import org.phantancy.fgocalc.entity.CalcEntity
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.fragment.BaseFragment
import org.phantancy.fgocalc.groupcalc.activity.GroupEnemyActivity
import org.phantancy.fgocalc.groupcalc.adapter.GroupChosenCardAdapter
import org.phantancy.fgocalc.groupcalc.adapter.GroupMemberAdapter
import org.phantancy.fgocalc.groupcalc.entity.bo.CardBO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupCalcVO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupEnemyVO
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO
import org.phantancy.fgocalc.item_decoration.LinearItemDecoration
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration
import org.phantancy.fgocalc.item_decoration.VerticalItemDecoration
import org.phantancy.fgocalc.groupcalc.viewmodel.GroupCalcViewModel

/**
 * 编队计算UI
 */
class GroupCalcFragment : BaseFragment() {
    private lateinit var binding: FragmentGroupCalcBinding
    private lateinit var vm: GroupCalcViewModel
    private var isBraveChain = false

    //    private var memberVO = GroupMemberVO()
    var groupEnemyVO: GroupEnemyVO = GroupEnemyVO()
    var groupCalcVO: GroupCalcVO = GroupCalcVO()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupCalcBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(GroupCalcActy@ this).get(GroupCalcViewModel::class.java)
        //成员适配器
        val memberAdapter = GroupMemberAdapter();
        //选卡适配器
        val chosenCardAdapter = GroupChosenCardAdapter()
        //搜索从者 添加成员
        val searchServantLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == BaseActy.RESULT_OK) {
                result.data?.let {
                    val svt = it.getParcelableExtra<ServantEntity>("servant")
                    //新逻辑
                    var memberVO = GroupMemberVO()
                    memberVO.svtEntity = svt
                    vm.addGroupMember(memberVO)
                }
            }
        }
        //设置条件、buff 详细设置
        val settingLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == BaseActy.RESULT_OK) {
                result.data?.let {
                    var memberVO = it.getParcelableExtra<GroupMemberVO>("groupMemberVO")
                    val memberPosition = it.getIntExtra("memberPosition", 0)
                    memberVO?.let {
                        vm.updateMember(it, memberAdapter.mList, memberPosition)
                        //条件变动，卡片复位
                        memberAdapter.chosenCardsCount = 0
                        binding.ivCardEx.visibility = View.INVISIBLE
                        binding.cbOk4.visibility = View.INVISIBLE
                        chosenCardAdapter.cleanList()
                    }
                }
            }
        }

        //跳敌方设置页
        val enemyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    //todo 更新敌方信息
                    groupEnemyVO = it.getParcelableExtra<GroupEnemyVO>("groupEnemyVO")

                }
            }
        }

        binding.viewEnemy.setTitle("设置敌方")
        binding.viewEnemy.setOnClickListener {
            enemyLauncher.launch(Intent(mActy, GroupEnemyActivity::class.java).apply { putExtra("groupEnemyVO", groupEnemyVO) })
        }
        //成员
        binding.rvMembers.adapter = memberAdapter
        binding.rvMembers.addItemDecoration(VerticalItemDecoration(ctx, 1f))
        //选了的卡
        binding.rvChosenCard.adapter = chosenCardAdapter
        val scale = resources.displayMetrics.density
        binding.rvChosenCard.addItemDecoration(LinearItemDecoration((60 * scale + 0.5f).toInt()))
        //成员事件
        memberAdapter.mListener = object : GroupMemberAdapter.GroupMemberListener {
            override fun addMember(position: Int) {
                val intent = Intent(ctx, SearchServantActy::class.java)
                searchServantLauncher.launch(intent)
            }

            override fun removeMember(member: GroupMemberVO, position: Int) {
                vm.removeMember(member, memberAdapter.mList)
                memberAdapter.chosenCardsCount = 0
                binding.ivCardEx.visibility = View.INVISIBLE
                binding.cbOk4.visibility = View.INVISIBLE
                chosenCardAdapter.cleanList()
            }

            override fun chooseCard(x: CardBO) {
                chosenCardAdapter.addEntity(x)
                Log.i(TAG, "chosenCount: ${memberAdapter.chosenCardsCount}")
            }

            override fun setSetting(member: GroupMemberVO, position: Int) {
                settingLauncher.launch(Intent(mActy, GroupMemberSettingActy::class.java).apply {
                    putExtra("groupMemberVO", member)
                    putExtra("memberPosition", position)
                })
            }
        }
        vm.memberGroup.observe(viewLifecycleOwner) {
            memberAdapter.submitList(it)
        }
        //已选卡事件
        chosenCardAdapter.groupChosenCardListener = object : GroupChosenCardAdapter.GroupChosenCardListener {

            override fun handleClickEvent(x: CardBO) {
                memberAdapter.returnEntity(x)
                //去ex卡
                binding.ivCardEx.visibility = View.INVISIBLE
                binding.cbOk4.visibility = View.INVISIBLE
            }

            override fun handleBraveChain(isBraveChain: Boolean) {
                this@GroupCalcFragment.isBraveChain = isBraveChain
                if (isBraveChain) {
                    binding.ivCardEx.visibility = View.VISIBLE
                    binding.cbOk4.visibility = View.VISIBLE
                } else {
                    binding.ivCardEx.visibility = View.INVISIBLE
                    binding.cbOk4.visibility = View.INVISIBLE
                }
            }
        }

        //监听overkill 暴击
        setOverkillCritical()

        //计算
        binding.btnCalc.setOnClickListener {

            for (x in memberAdapter.mList) {
                val card = x.cards[0]
                val out = "${x.svtEntity.name} ${card.svtId} ${card.svtPosition} ${card.position}"
                Log.i(TAG, out)
            }
            groupEnemyVO?.let {
                Log.i(TAG, "enemyCount: ${it.enemyCount}")
                for (i in 0 until it.enemyCount) {
                    Log.i(TAG, "${it.enemysNpMod[i]} ${it.enemysStarMod[i]} ${it.enemysClassPosition[i]}")
                }
            }

            vm.clickCalc(memberAdapter.mList, groupCalcVO,groupEnemyVO, chosenCardAdapter.mList, isBraveChain)
        }

        binding.btnClean.setOnClickListener {
            vm.cleanResult()
            chosenCardAdapter.cleanList()
            memberAdapter.resetList()
        }

        //结果
        val resultAdapter = ResultAdapter()
        binding.rvCalcResult.adapter = resultAdapter
        binding.rvCalcResult.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(ctx, 5.0f)))
        vm.resultList.observe(viewLifecycleOwner, Observer { result ->
            resultAdapter.submitList(result)
        })


    }

    fun safeGetCalcEntity(position: Int, list: List<CalcEntity>): CalcEntity {
        if (list?.size > 0) {
            list[position].source = 1
            return list[position]
        } else {
            val x = CalcEntity();
            x.source = 1
            return x
        }
    }

    //监听过量伤害、暴击
    private fun setOverkillCritical() {
        binding.cbOk1.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isOverkill1 = isChecked }
        binding.cbOk2.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isOverkill2 = isChecked }
        binding.cbOk3.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isOverkill3 = isChecked }
        binding.cbOk4.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isOverkill4 = isChecked }
        binding.cbCr1.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isCritical1 = isChecked }
        binding.cbCr2.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isCritical2 = isChecked }
        binding.cbCr3.setOnCheckedChangeListener { buttonView, isChecked -> groupCalcVO.isCritical3 = isChecked }
    }
}