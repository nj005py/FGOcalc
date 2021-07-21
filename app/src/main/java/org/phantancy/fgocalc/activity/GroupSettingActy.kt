package org.phantancy.fgocalc.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter
import org.phantancy.fgocalc.common.Constant.ENTRY_GROUP
import org.phantancy.fgocalc.databinding.ActyCalcBinding
import org.phantancy.fgocalc.entity.CalcEntity
import org.phantancy.fgocalc.entity.ServantEntity
import org.phantancy.fgocalc.fragment.CalcContainerFragment
import org.phantancy.fgocalc.fragment.InfoFragment
import org.phantancy.fgocalc.fragment.WikiFragment
import org.phantancy.fgocalc.viewmodel.CalcViewModel

class GroupSettingActy : BaseActy() {
    private lateinit var binding: ActyCalcBinding
    private lateinit var vm: CalcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val servant = intent.getParcelableExtra<Parcelable>("servant") as ServantEntity
        val calcEntity = intent.getParcelableExtra<Parcelable>("calcEntity") as CalcEntity

        vm = ViewModelProvider(this).get(CalcViewModel::class.java)
        //设置编队计算
        vm.entry = ENTRY_GROUP
        servant?.let { vm.servant = servant }
        calcEntity?.let { vm.calcEntity = calcEntity }

        //tab标题列表
        val tabs = arrayListOf<String>("从者信息", "设置", "wiki")
        //wiki页初始化
        val wikiFragment = WikiFragment()
        wikiFragment.setParentPager(binding.vpCalcPager)
        //碎片页列表
        val fragments = arrayListOf<Fragment>(InfoFragment(), CalcContainerFragment(ENTRY_GROUP), wikiFragment)

        val pagerAdapter = CalcViewPagerAdapter(this, fragments)
        binding.vpCalcPager.adapter = pagerAdapter
        binding.tlCalcTab.tabMode = TabLayout.MODE_FIXED
        TabLayoutMediator(binding.tlCalcTab, binding.vpCalcPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        //跳到条件页，很重要，不然获取不到宝具信息
        binding.vpCalcPager.setCurrentItem(1)
    }

    override fun onStart() {
        super.onStart()
        binding.vpCalcPager.offscreenPageLimit = 3
    }

    //重写返回键保存条件、buff
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            val intent = Intent().apply {
                putExtra("calcEntity",vm.calcEntity)
            }
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}