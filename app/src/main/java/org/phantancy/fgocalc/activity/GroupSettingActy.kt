package org.phantancy.fgocalc.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter
import org.phantancy.fgocalc.databinding.ActyCalcBinding
import org.phantancy.fgocalc.fragment.CalcContainerFragment
import org.phantancy.fgocalc.fragment.InfoFragment
import org.phantancy.fgocalc.fragment.WikiFragment

class GroupSettingActy : BaseActy() {
    private lateinit var binding: ActyCalcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabs = arrayListOf<String>("从者信息", "设置", "wiki")
        val wikiFragment = WikiFragment()
        wikiFragment.setParentPager(binding.vpCalcPager)
        val fragments = arrayListOf<Fragment>(InfoFragment(), CalcContainerFragment(), wikiFragment)

        val pagerAdapter = CalcViewPagerAdapter(this, fragments)
        binding.vpCalcPager.adapter = pagerAdapter
        binding.tlCalcTab.tabMode = TabLayout.MODE_FIXED
        TabLayoutMediator(binding.tlCalcTab, binding.vpCalcPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        binding.vpCalcPager.offscreenPageLimit = 3
    }
}