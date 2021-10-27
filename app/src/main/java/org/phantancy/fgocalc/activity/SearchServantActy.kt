package org.phantancy.fgocalc.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter
import org.phantancy.fgocalc.databinding.ActyMainBinding
import org.phantancy.fgocalc.fragment.FilterFragment
import org.phantancy.fgocalc.fragment.ServantListFragment
import org.phantancy.fgocalc.viewmodel.MainViewModel

/**
 * 从者搜索
 */
class SearchServantActy : BaseActy() {
    private lateinit var binding: ActyMainBinding
    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this).get(MainViewModel::class.java)

        val fragments = listOf(ServantListFragment(ServantListFragment.ENTRY_SEARCH), FilterFragment())
        val tabs = listOf("搜索", "筛选")

        val pagerAdapter = CalcViewPagerAdapter(this, fragments)
        binding.vpPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tlTab, binding.vpPager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.setText(tabs[position])
        }).attach()

        vm.currentPage.observe(this, Observer { page -> binding.vpPager.setCurrentItem(page) })

    }

}