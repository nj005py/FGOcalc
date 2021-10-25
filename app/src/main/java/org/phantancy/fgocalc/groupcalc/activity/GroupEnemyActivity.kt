package org.phantancy.fgocalc.groupcalc.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.activity.BaseActy
import org.phantancy.fgocalc.data.ConditionData
import org.phantancy.fgocalc.databinding.ActivityGroupEnemyBinding
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupEnemyVO
import org.phantancy.fgocalc.view.EnemySelectView

//编队计算，敌人设置
class GroupEnemyActivity : BaseActy() {
    private lateinit var binding: ActivityGroupEnemyBinding
    private lateinit var viewEnemyList: List<EnemySelectView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupEnemyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val groupEnemyVO = intent.getParcelableExtra<GroupEnemyVO>("groupEnemyVO")

        viewEnemyList = arrayListOf(binding.viewEnemy1, binding.viewEnemy2, binding.viewEnemy3,
                binding.viewEnemy4, binding.viewEnemy5, binding.viewEnemy6)
        //敌人数量
        setSpAdapter(binding.spEnemyCount, ConditionData.getEnemyCountKeys())
        binding.spEnemyCount.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                handleEnemyClassDisplay(position)
                val count = ConditionData.getEnemyCountValues()[position]
                groupEnemyVO.enemyCount = count

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //敌人职阶

        groupEnemyVO?.let {
            binding.spEnemyCount.setSelection(it.enemyCount - 1)
            binding.viewEnemy1.setSelection(it.enemysClassPosition[0])
            if (binding.viewEnemy2.visibility == View.VISIBLE) {
                binding.viewEnemy2.setSelection(it.enemysClassPosition[1])
            }
            if (binding.viewEnemy3.visibility == View.VISIBLE) {
                binding.viewEnemy3.setSelection(it.enemysClassPosition[2])
            }
            if (binding.viewEnemy4.visibility == View.VISIBLE) {
                binding.viewEnemy4.setSelection(it.enemysClassPosition[3])
            }
            if (binding.viewEnemy5.visibility == View.VISIBLE) {
                binding.viewEnemy5.setSelection(it.enemysClassPosition[4])
            }
            if (binding.viewEnemy6.visibility == View.VISIBLE) {
                binding.viewEnemy6.setSelection(it.enemysClassPosition[5])
            }
        }

        binding.viewEnemy1.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[0] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[0] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[0] = position
            }
        }

        binding.viewEnemy1.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[0] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[0] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[0] = position
            }
        }

        binding.viewEnemy2.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[1] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[1] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[1] = position
            }
        }

        binding.viewEnemy3.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[2] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[2] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[2] = position
            }
        }

        binding.viewEnemy4.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[3] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[3] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[3] = position
            }
        }

        binding.viewEnemy5.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[4] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[4] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[4] = position
            }
        }

        binding.viewEnemy6.mListener = object : EnemySelectView.EnemySelectListener {
            override fun onSelected(position: Int) {
                groupEnemyVO.enemysNpMod[5] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[5] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[5] = position
            }
        }


        binding.btnSave.setOnClickListener {
            setResult(RESULT_OK, Intent().apply { putExtra("groupEnemyVO", groupEnemyVO) })
            finish()
        }
    }

    private fun setSpAdapter(sp: Spinner, x: Array<String>) {
        val spAdapter = ArrayAdapter(ctx, R.layout.entity_spinner, x)
        spAdapter.setDropDownViewResource(R.layout.entity_spinner)
        sp.adapter = spAdapter
    }

    //控制显示敌方职阶设置
    private fun handleEnemyClassDisplay(position: Int) {
        val count = ConditionData.getEnemyCountValues()[position]
        for ((index,view) in viewEnemyList.withIndex()){
            if (index < count){
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}