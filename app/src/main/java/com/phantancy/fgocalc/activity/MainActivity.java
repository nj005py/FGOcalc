package com.phantancy.fgocalc.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.fragment.AtkFrag;
import com.phantancy.fgocalc.fragment.InfoFrag;
import com.phantancy.fgocalc.fragment.MetaphysicsFrag;
import com.phantancy.fgocalc.fragment.NpFrag;
import com.phantancy.fgocalc.fragment.TrumpFrag;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.at_tl_tabtop)
    TabLayout atTlTabtop;
    @BindView(R.id.at_vp_pager)
    ViewPager atVpPager;
    private List<String> indicatorList;
    private List<Fragment> fragList;
    private ContentPagerAdapter contentAdapter;
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ServantItem servantItem;
    private AtkFrag atkFrag = new AtkFrag();
    private NpFrag npFrag = new NpFrag();
    private InfoFrag infoFrag = new InfoFrag();
    private TrumpFrag trumpFrag = new TrumpFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        initBaseStatusBar(mContext);
        servantItem = (ServantItem) getIntent().getSerializableExtra("servants");
        Bundle data = new Bundle();
        data.putSerializable("servants", servantItem);
        infoFrag.setArguments(data);//通过Bundle向Activity中传递值
        atkFrag.setArguments(data);
        npFrag.setArguments(data);
        trumpFrag.setArguments(data);
        initContent();
        initTab();
    }

//    private void initStatusBar() {
//        if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            int height = BaseUtils.getStatusBarHeight(mContext);
//            atLlBar.setPadding(0, height, 0, 0);
//        }
//    }

    private void initTab() {
        atTlTabtop.setTabMode(TabLayout.MODE_SCROLLABLE);
        //未选中的颜色，选中的颜色
        atTlTabtop.setTabTextColors(ContextCompat.getColor(this, R.color.colorGray),
                ContextCompat.getColor(this, R.color.colorWhite));
        //游标的颜色
        atTlTabtop.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorGolden));
        ViewCompat.setElevation(atTlTabtop, 10);
        atTlTabtop.setupWithViewPager(atVpPager);
    }

    private void initContent() {
        //标签列表
        indicatorList = new ArrayList<>();
        indicatorList.add("从者信息");
        indicatorList.add("伤害计算器");
        indicatorList.add("NP计算器");
        indicatorList.add("宝具计算器");
        //Fragment列表
        fragList = new ArrayList<>();
        fragList.add(infoFrag);
        fragList.add(atkFrag);
        fragList.add(npFrag);
        fragList.add(trumpFrag);
        //ViewPagerAdapter
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        atVpPager.setAdapter(contentAdapter);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return indicatorList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return indicatorList.get(position);
        }
    }


}
