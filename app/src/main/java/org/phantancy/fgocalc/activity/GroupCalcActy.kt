package org.phantancy.fgocalc.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import org.phantancy.fgocalc.adapter.CardsAdapter
import org.phantancy.fgocalc.adapter.GroupServantAdapter
import org.phantancy.fgocalc.data.ServantAvatar
import org.phantancy.fgocalc.databinding.ActyGroupCalcBinding
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration
import org.phantancy.fgocalc.item_decoration.VerticalItemDecoration
import org.phantancy.fgocalc.viewmodel.GroupCalcViewModel

class GroupCalcActy :BaseActy() {
    private lateinit var binding: ActyGroupCalcBinding
    private var svtPosition = 0
    private lateinit var vm: GroupCalcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyGroupCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(GroupCalcActy@this).get(GroupCalcViewModel::class.java)

        val resultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val svt = data.getParcelableExtra<ServantEntity>("servant")
                    vm.addServant(svt,svtPosition)
                    svtPosition = 0
                }
            }
        }
        val cardAdapter = CardsAdapter()
        binding.rvCards.adapter = cardAdapter
        binding.rvCards.addItemDecoration(VerticalItemDecoration(this,40f))

        val svtAdapter = GroupServantAdapter()
        binding.rvSvts.adapter = svtAdapter

        vm.cardPicks.observe(this, Observer { list ->
            cardAdapter.submitList(list)
        })

        svtAdapter.mListener = object :GroupServantAdapter.GroupSvtListener{
            override fun addSvt(position: Int) {
                Log.i(TAG,"addSvt position: $position")
                svtPosition = position
                resultLauncher.launch(Intent(ctx,SearchServantActy::class.java))
            }

            override fun removeSvt(svt: ServantEntity) {
                vm.removeServant(svt)
            }

            override fun setSetting(position: Int) {
            }
        }

        vm.svtGroup.observe(this, Observer { list ->
            svtAdapter.submitList(list)
            vm.parseServantsCards(list)
        })
    }
}