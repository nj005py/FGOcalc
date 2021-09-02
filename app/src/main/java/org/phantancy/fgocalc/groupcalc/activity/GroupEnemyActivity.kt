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

//编队计算，敌人设置
class GroupEnemyActivity : BaseActy() {
    private lateinit var binding: ActivityGroupEnemyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupEnemyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val groupEnemyVO = intent.getParcelableExtra<GroupEnemyVO>("groupEnemyVO")

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
        setSpAdapter(binding.spEnemyClass1, ConditionData.getEnemyNpModsKeys())
        setSpAdapter(binding.spEnemyClass2, ConditionData.getEnemyNpModsKeys())
        setSpAdapter(binding.spEnemyClass3, ConditionData.getEnemyNpModsKeys())

        groupEnemyVO?.let {
            binding.spEnemyCount.setSelection(it.enemyCount - 1)
            binding.spEnemyClass1.setSelection(it.enemysClassPosition[0])
            if (binding.spEnemyClass2.visibility == View.VISIBLE) {
                binding.spEnemyClass2.setSelection(it.enemysClassPosition[1])
            }
            if (binding.spEnemyClass3.visibility == View.VISIBLE) {
                binding.spEnemyClass3.setSelection(it.enemysClassPosition[2])
            }
        }

        binding.spEnemyClass1.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                groupEnemyVO.enemysNpMod[0] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[0] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[0] = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spEnemyClass2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                groupEnemyVO.enemysNpMod[1] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[1] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[1] = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spEnemyClass3.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                groupEnemyVO.enemysNpMod[2] = ConditionData.getEnemyNpModsValues()[position]
                groupEnemyVO.enemysStarMod[2] = ConditionData.getEnemyStarModsValues()[position]
                groupEnemyVO.enemysClassPosition[2] = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnSave.setOnClickListener {
            setResult(RESULT_OK,Intent().apply { putExtra("groupEnemyVO",groupEnemyVO) })
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
        if (count == 1) {
            binding.llEnemySetting2.visibility = View.GONE
            binding.llEnemySetting3.visibility = View.GONE
        }
        if (count == 2) {
            binding.llEnemySetting2.visibility = View.VISIBLE
            binding.llEnemySetting3.visibility = View.GONE
        }
        if (count == 3) {
            binding.llEnemySetting2.visibility = View.VISIBLE
            binding.llEnemySetting3.visibility = View.VISIBLE
        }
    }
}