package org.phantancy.fgocalc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import org.phantancy.fgocalc.databinding.FragmentCalcContainerBinding

class CalcContainerFragment : LazyFragment() {
    private lateinit var binding: FragmentCalcContainerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCalcContainerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun init() {
        super.init()
        val fragments = ArrayList<Fragment>().apply {
            add(ConditionFragment())
            add(BuffFragment())
            add(CalcFragment())
        }
        val adapter = CalcPagerAdapter(mActy, fragments)
        binding.vpCalc.adapter = adapter
        binding.vpCalc.orientation = ORIENTATION_VERTICAL
        binding.vpCalc.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when {
                    position == 0 -> {
                        binding.btnUp.visibility = View.GONE
                        binding.btnDown.visibility = View.VISIBLE
                    }
                    position == (fragments.size - 1) -> {
                        binding.btnUp.visibility = View.VISIBLE
                        binding.btnDown.visibility = View.GONE

                    }
                    else -> {
                        binding.btnUp.visibility = View.VISIBLE
                        binding.btnDown.visibility = View.VISIBLE
                    }
                }
            }
        })
        binding.btnUp.setOnClickListener {
            val curPage = binding.vpCalc.currentItem
            binding.vpCalc.setCurrentItem(curPage - 1)
        }
        binding.btnDown.setOnClickListener {
            val curPage = binding.vpCalc.currentItem
            binding.vpCalc.setCurrentItem(curPage + 1)
        }
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