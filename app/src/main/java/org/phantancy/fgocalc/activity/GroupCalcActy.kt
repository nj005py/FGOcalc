package org.phantancy.fgocalc.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import org.phantancy.fgocalc.adapter.CardsAdapter
import org.phantancy.fgocalc.data.ServantAvatar
import org.phantancy.fgocalc.databinding.ActyGroupCalcBinding
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.viewmodel.GroupCalcViewModel

class GroupCalcActy :BaseActy() {
    private lateinit var binding: ActyGroupCalcBinding
    private var svtMsg = 0
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
                    Glide.with(ctx)
                            .load(ServantAvatar.getServantAvatar(svt.id))
                            .into(getSvtView(svtMsg))
                    svtMsg = 0
                    vm.addServant(svt)
                }
            }
        }

        binding.ivSvt1.setOnClickListener {
            //如果为空搜索从者
            svtMsg = 1
            resultLauncher.launch(Intent(ctx,SearchServantActy::class.java))
        }
        //选中从者则删除
        binding.ivSvt1.setOnLongClickListener {
            true
        }
        binding.ivSvt2.setOnClickListener {
            svtMsg = 2
            resultLauncher.launch(Intent(ctx,SearchServantActy::class.java))
        }
        binding.ivSvt3.setOnClickListener {
            svtMsg = 3
            resultLauncher.launch(Intent(ctx,SearchServantActy::class.java))
        }

        val cardAdapter = CardsAdapter()
        binding.rvCards.adapter = cardAdapter
        vm.cardPicks.observe(this, Observer { list ->
            cardAdapter.submitList(list)
        })
    }

    private fun getSvtView(id:Int): ImageView{
        return when(id){
            1 -> binding.ivSvt1
            2 -> binding.ivSvt2
            3 -> binding.ivSvt3
            else -> binding.ivSvt1
        }
    }
}