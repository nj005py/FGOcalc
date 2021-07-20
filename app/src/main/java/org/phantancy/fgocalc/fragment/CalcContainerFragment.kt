package org.phantancy.fgocalc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import org.phantancy.fgocalc.R
import org.phantancy.fgocalc.common.Constant.ENTRY_SINGLE
import org.phantancy.fgocalc.databinding.FragmentCalcContainerBinding
import org.phantancy.fgocalc.viewmodel.CalcViewModel

class CalcContainerFragment(val entry: Int) : LazyFragment() {
    private lateinit var binding: FragmentCalcContainerBinding
    private lateinit var vm: CalcViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalcContainerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun init() {
        super.init()
        vm = ViewModelProvider(mActy).get(CalcViewModel::class.java)
        val fragments = ArrayList<Fragment>().apply {
            add(ConditionFragment())
            add(BuffFragment())
            if (entry == ENTRY_SINGLE) {
                add(CalcFragment())
            }
        }
        val adapter = CalcPagerAdapter(mActy, fragments)
        binding.vpCalc.adapter = adapter
        binding.vpCalc.orientation = ORIENTATION_VERTICAL
        //true:滑动，false：禁止滑动
        binding.vpCalc.isUserInputEnabled = true
        //上一页、下一页
//        binding.btnUp.setOnClickListener {
//            val curPage = binding.vpCalc.currentItem
//            binding.vpCalc.setCurrentItem(curPage - 1)
//        }
//        binding.btnDown.setOnClickListener {
//            val curPage = binding.vpCalc.currentItem
//            binding.vpCalc.setCurrentItem(curPage + 1)
//        }

        //跳页
        vm.conditionPage.observe(viewLifecycleOwner, Observer { index -> binding.vpCalc.currentItem = index })
    }

    inner class CalcPagerAdapter(fa: FragmentActivity, val fragments: List<Fragment>) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return fragments?.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments?.get(position)
        }

    }
}