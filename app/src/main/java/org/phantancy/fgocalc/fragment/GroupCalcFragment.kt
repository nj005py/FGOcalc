package org.phantancy.fgocalc.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.phantancy.fgocalc.activity.BaseActy
import org.phantancy.fgocalc.activity.GroupSettingActy
import org.phantancy.fgocalc.activity.SearchServantActy
import org.phantancy.fgocalc.adapter.*
import org.phantancy.fgocalc.databinding.FragmentGroupCalcBinding
import org.phantancy.fgocalc.entity.CalcEntity
import org.phantancy.fgocalc.entity.CardPickEntity
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.item_decoration.LinearItemDecoration
import org.phantancy.fgocalc.item_decoration.PickCardItemDecoration
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration
import org.phantancy.fgocalc.item_decoration.VerticalItemDecoration
import org.phantancy.fgocalc.util.ToastUtils
import org.phantancy.fgocalc.viewmodel.GroupCalcViewModel

/**
 * 编队计算UI
 */
class GroupCalcFragment : BaseFragment() {
    private lateinit var binding: FragmentGroupCalcBinding
    private var svtPosition = 0
    private lateinit var vm: GroupCalcViewModel
    private lateinit var settingButtons: List<View>
    private var isBraveChain = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupCalcBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(GroupCalcActy@ this).get(GroupCalcViewModel::class.java)
        settingButtons = listOf(binding.btnSetting1, binding.btnSetting2, binding.btnSetting3)

        //搜索从者
        val searchServantLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == BaseActy.RESULT_OK) {
                result.data?.let {
                    val svt = it.getParcelableExtra<ServantEntity>("servant")
                    vm.addServant(svt, svtPosition)
                    //显示从者设置按钮
                    settingButtons[svtPosition].visibility = View.VISIBLE
//                    displaySettingServants(svtAdapter.mList)
                    svtPosition = 0
                }
            }
        }
        //设置条件、buff
        val settingLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == BaseActy.RESULT_OK) {
                result.data?.let {
                    val calcEntity = it.getParcelableExtra("calcEntity") as CalcEntity
                    calcEntity?.let {
                        vm.updateServantCards(svtPosition, calcEntity)
                    }
                    svtPosition = 0
                }
            }
        }
        binding.viewEnemy.setTitle("设置敌方")
        binding.viewEnemy.setOnClickListener {
            ToastUtils.showToast("click")
        }
        //成员
        val memberAdapter = GroupMemberAdapter();
        binding.rvMembers.adapter = memberAdapter
        binding.rvMembers.addItemDecoration(PickCardItemDecoration(ctx, 1f))
        //可以选的卡
        val cardAdapter = CardsAdapter()
        binding.rvCards.adapter = cardAdapter
        binding.rvCards.addItemDecoration(PickCardItemDecoration(ctx, 1f))
        //从者
        val svtAdapter = GroupServantAdapter()
        binding.rvSvts.adapter = svtAdapter
        binding.rvSvts.addItemDecoration(VerticalItemDecoration(ctx, 5f))
        //选了的卡
        val pickedAdapter = PickAdapter()
        binding.rvPicked.adapter = pickedAdapter
        val scale = resources.displayMetrics.density
        binding.rvPicked.addItemDecoration(LinearItemDecoration((60 * scale + 0.5f).toInt()))

        vm.cardPicks.observe(viewLifecycleOwner, Observer { list ->
            cardAdapter.submitList(list)
        })


        /**
         * 移除从者时，把已选的卡也清空
         */
        svtAdapter.mListener = object : GroupServantAdapter.GroupSvtListener {
            override fun addSvt(position: Int) {
                Log.i(TAG, "addSvt position: $position")
                svtPosition = position
                searchServantLauncher.launch(Intent(ctx, SearchServantActy::class.java))
            }

            override fun removeSvt(svt: ServantEntity, position: Int) {
                vm.removeServant(svt)
                pickedAdapter.cleanList()
                binding.ivCardEx.visibility = View.INVISIBLE
                binding.cbOk4.visibility = View.INVISIBLE
                vm.cleanResult()
                //隐藏从者设置按钮
                displaySettingServants(svtAdapter.mList)
            }

        }

        vm.svtGroup.observe(viewLifecycleOwner, Observer { list ->
            svtAdapter.submitList(list)
            vm.parseServantsCards(list)
        })

        //点击卡池
        cardAdapter.setEntityListenr { x ->
            pickedAdapter.addEntity(x)
        }

        //点击已选卡
        pickedAdapter.setEntityListenr(object : PickAdapter.IEntityListener {
            override fun handleClickEvent(x: CardPickEntity?) {
                cardAdapter.returnEntity(x)
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
        })

        //监听overkill 暴击
        setOverkillCritical()

        //计算
        binding.btnCalc.setOnClickListener {
            vm.clickCalc(pickedAdapter.entities as ArrayList<CardPickEntity>,isBraveChain)
        }

        binding.btnSetting1.setOnClickListener { v -> launchSetting(svtAdapter.mList[0],0,safeGetCalcEntity(0,vm.calcEntites),settingLauncher) }
        binding.btnSetting2.setOnClickListener { v -> launchSetting(svtAdapter.mList[1],1,safeGetCalcEntity(1,vm.calcEntites),settingLauncher) }
        binding.btnSetting3.setOnClickListener { v -> launchSetting(svtAdapter.mList[2],2,safeGetCalcEntity(2,vm.calcEntites),settingLauncher) }


        //结果
        val resultAdapter = ResultAdapter()
        binding.rvCalcResult.adapter = resultAdapter
        binding.rvCalcResult.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(ctx,5.0f)))
        vm.resultList.observe(viewLifecycleOwner, Observer { result ->
            resultAdapter.submitList(result)
        })
    }

    fun displaySettingServants(list: List<ServantEntity>) {
        val size = list.size
        for (i in 0 until settingButtons.size) {
            settingButtons[i].visibility = View.GONE
        }
        for (i in 0 until size) {
            settingButtons[i].visibility = View.VISIBLE
        }

    }

    fun launchSetting(svt: ServantEntity, position: Int, calcEntity: CalcEntity,
                      settingLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(ctx, GroupSettingActy::class.java).apply {
            putExtra("servant", svt)
            putExtra("calcEntity", calcEntity)
        }
        svtPosition = position
        settingLauncher.launch(intent)
    }

    fun safeGetCalcEntity(position: Int, list: List<CalcEntity>): CalcEntity{
        if (list?.size > 0){
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
        binding.cbOk1.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isOverkill1 = isChecked }
        binding.cbOk2.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isOverkill2 = isChecked }
        binding.cbOk3.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isOverkill3 = isChecked }
        binding.cbOk4.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isOverkill4 = isChecked }
        binding.cbCr1.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isCritical1 = isChecked }
        binding.cbCr2.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isCritical2 = isChecked }
        binding.cbCr3.setOnCheckedChangeListener { buttonView, isChecked -> vm.groupCalcEntity.isCritical3 = isChecked }
    }
}