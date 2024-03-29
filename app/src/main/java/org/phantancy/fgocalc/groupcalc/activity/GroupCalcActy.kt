package org.phantancy.fgocalc.groupcalc.activity

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.activity.BaseActy
import org.phantancy.fgocalc.databinding.ActyGroupCalcBinding
import org.phantancy.fgocalc.groupcalc.fragment.GroupCalcFragment
import org.phantancy.fgocalc.groupcalc.viewmodel.GroupCalcViewModel

class GroupCalcActy : BaseActy() {
    private lateinit var binding: ActyGroupCalcBinding
    private lateinit var vm: GroupCalcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyGroupCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        vm = ViewModelProvider(GroupCalcActy@ this).get(GroupCalcViewModel::class.java)
        supportFragmentManager.commit {
            replace<GroupCalcFragment>(R.id.container)
            setReorderingAllowed(false)
        }
//        //搜索从者
//        val searchServantLauncher = registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == RESULT_OK) {
//                result.data?.let {
//                    val svt = it.getParcelableExtra<ServantEntity>("servant")
//                    vm.addServant(svt, svtPosition)
//                    svtPosition = 0
//                }
//            }
//        }
//        //设置条件、buff
//        val settingLauncher = registerForActivityResult(
//                ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == RESULT_OK) {
//                result.data?.let {
//                    val calcEntity = it.getParcelableExtra("calcEntity") as CalcEntity
//                    calcEntity?.let {
//                        Log.i(TAG, "count: ${calcEntity.enemyCount}")
//                        vm.updateServantCards(svtPosition, calcEntity.npEntity)
//                    }
//                    svtPosition = 0
//                }
//            }
//        }
//        val cardAdapter = CardsAdapter()
//        binding.rvCards.adapter = cardAdapter
//        binding.rvCards.addItemDecoration(VerticalItemDecoration(this, 30f))
//
//        val svtAdapter = GroupServantAdapter()
//        binding.rvSvts.adapter = svtAdapter
//
//        val pickedAdapter = PickAdapter()
//        binding.rvPicked.adapter = pickedAdapter
//        val scale = resources.displayMetrics.density
//        binding.rvPicked.addItemDecoration(LinearItemDecoration((60 * scale + 0.5f).toInt()))
//
//        vm.cardPicks.observe(this, Observer { list ->
//            cardAdapter.submitList(list)
//        })
//
//        svtAdapter.mListener = object : GroupServantAdapter.GroupSvtListener {
//            override fun addSvt(position: Int) {
//                Log.i(TAG, "addSvt position: $position")
//                svtPosition = position
//                searchServantLauncher.launch(Intent(ctx, SearchServantActy::class.java))
//            }
//
//            override fun removeSvt(svt: ServantEntity) {
//                vm.removeServant(svt)
//            }
//
//            override fun setSetting(position: Int) {
//                val intent = Intent(this@GroupCalcActy, GroupSettingActy::class.java).apply {
//                    putExtra("servant", svtAdapter.mList[position])
//                }
//                svtPosition = position
//                settingLauncher.launch(intent)
//            }
//        }
//
//        vm.svtGroup.observe(this, Observer { list ->
//            svtAdapter.submitList(list)
//            vm.parseServantsCards(list)
//        })
//
//        //点击卡池
//        cardAdapter.setEntityListenr { x ->
//            pickedAdapter.addEntity(x)
//        }
//
//        //点击已选卡
//        pickedAdapter.setEntityListenr(object : PickAdapter.IEntityListener{
//            override fun handleClickEvent(x: CardPickEntity?) {
//                cardAdapter.returnEntity(x)
//                //去ex卡
//                binding.ivCardEx.visibility = View.INVISIBLE
//                binding.cbOk4.visibility = View.INVISIBLE
//            }
//
//            override fun handleBraveChain(isBraveChain: Boolean) {
//                if (isBraveChain) {
//                    binding.ivCardEx.visibility = View.VISIBLE
//                    binding.cbOk4.visibility = View.VISIBLE
//                } else {
//                    binding.ivCardEx.visibility = View.INVISIBLE
//                    binding.cbOk4.visibility = View.INVISIBLE
//                }
//            }
//        })
    }

}