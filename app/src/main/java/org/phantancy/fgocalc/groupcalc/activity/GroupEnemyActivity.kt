package org.phantancy.fgocalc.groupcalc.activity

import android.os.Bundle
import org.phantancy.fgocalc.activity.BaseActy
import org.phantancy.fgocalc.databinding.ActivityGroupEnemyBinding

//编队计算，敌人设置
class GroupEnemyActivity : BaseActy(){
    private lateinit var binding: ActivityGroupEnemyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupEnemyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}