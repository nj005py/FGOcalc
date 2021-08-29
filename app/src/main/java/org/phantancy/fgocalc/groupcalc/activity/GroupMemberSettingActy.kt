package org.phantancy.fgocalc.groupcalc.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.phantancy.fgocalc.activity.BaseActy
import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter
import org.phantancy.fgocalc.common.Constant.ENTRY_GROUP
import org.phantancy.fgocalc.databinding.ActyCalcBinding
import org.phantancy.fgocalc.fragment.CalcContainerFragment
import org.phantancy.fgocalc.fragment.InfoFragment
import org.phantancy.fgocalc.fragment.WikiFragment
import org.phantancy.fgocalc.groupcalc.adapter.GroupMemberSettingFragment
import org.phantancy.fgocalc.groupcalc.entity.vo.GroupMemberVO
import org.phantancy.fgocalc.groupcalc.viewmodel.GroupSettingViewModel
import org.phantancy.fgocalc.viewmodel.CalcViewModel

/**
 * 成员设置
 */
class GroupMemberSettingActy : BaseActy() {
    private lateinit var binding: ActyCalcBinding
    private lateinit var calcViewModel: CalcViewModel
    private lateinit var groupSettingViewModel: GroupSettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActyCalcBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val servant = intent.getParcelableExtra<Parcelable>("servant") as ServantEntity
//        val calcEntity = intent.getParcelableExtra<Parcelable>("calcEntity") as CalcEntity
        val groupMemberVO = intent.getParcelableExtra<GroupMemberVO>("groupMemberVO")

        calcViewModel = ViewModelProvider(this).get(CalcViewModel::class.java)
        groupSettingViewModel = ViewModelProvider(this).get(GroupSettingViewModel::class.java)
        //设置编队计算
        calcViewModel.entry = ENTRY_GROUP
//        servant?.let { vm.servant = servant }
//        calcEntity?.let { vm.calcEntity = calcEntity }
        groupMemberVO?.let {
            groupSettingViewModel.servant = groupMemberVO.svtEntity
            groupSettingViewModel.memberVO = groupMemberVO
            calcViewModel.servant = groupMemberVO.svtEntity
        }
        //tab标题列表
        val tabs = arrayListOf<String>("从者信息", "设置", "wiki")
        //wiki页初始化
        val wikiFragment = WikiFragment()
        wikiFragment.setParentPager(binding.vpCalcPager)
//        containerFragment = CalcContainerFragment(ENTRY_GROUP)
        //碎片页列表
        val fragments = arrayListOf<Fragment>(InfoFragment(), GroupMemberSettingFragment(), wikiFragment)

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
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
//            Log.d(TAG,"点击返回键")
//            val intent = Intent().apply {
//                putExtra("calcEntity",vm.calcEntity)
//            }
//            setResult(Activity.RESULT_OK,intent)
//            finish()
//        }
//        return super.onKeyDown(keyCode, event)
//    }

    override fun onBackPressed() {
        //保存条件、buff
        val intent = Intent().apply {
//            putExtra("calcEntity", calcViewModel.calcEntity)
            putExtra("groupMemberVO",groupSettingViewModel.memberVO)
        }
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}