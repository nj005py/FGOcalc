package org.phantancy.fgocalc.calc;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.base.BaseActy;
import org.phantancy.fgocalc.calc.atk.AtkFrag;
import org.phantancy.fgocalc.calc.atk.AtkPresenter;
import org.phantancy.fgocalc.calc.info.InfoFrag;
import org.phantancy.fgocalc.calc.info.InfoPresenter;
import org.phantancy.fgocalc.calc.np.NpFrag;
import org.phantancy.fgocalc.calc.np.NpPresenter;
import org.phantancy.fgocalc.calc.star.StarFrag;
import org.phantancy.fgocalc.calc.star.StarPresenter;
import org.phantancy.fgocalc.calc.trump.TrumpFrag;
import org.phantancy.fgocalc.calc.trump.TrumpPresenter;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HATTER on 2017/11/4.
 */

public class CalcActy extends BaseActy {

    @BindView(R.id.status_bar)
    LinearLayout statusBar;
    @BindView(R.id.ac_tl_tabtop)
    TabLayout acTlTabtop;
    @BindView(R.id.ac_vp_pager)
    ViewPager acVpPager;
    private List<String> indicatorList;
    private List<Fragment> fragList;
    private ContentPagerAdapter contentAdapter;
    private ServantItem servantItem;
    private BuffsItem buffsItem;
    private InfoFrag infoFrag;
    private AtkFrag atkFrag;
    private NpFrag npFrag;
    private TrumpFrag trumpFrag;
    private StarFrag starFrag;
    private InfoPresenter infoPresenter;
    private AtkPresenter atkPresenter;
    private NpPresenter npPresenter;
    private TrumpPresenter trumpPresenter;
    private StarPresenter starPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_calc);
        ButterKnife.bind(this);
        //状态栏
        initStatusBar();
        servantItem = (ServantItem) getIntent().getSerializableExtra("servants");
        initContent();
        initTab();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initStatusBar() {
        int height = BaseUtils.getStatusBarHeight(ctx);
        statusBar.setPadding(0, height, 0, 0);
        if (Build.VERSION.SDK_INT == 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initTab() {
        acTlTabtop.setTabMode(TabLayout.MODE_SCROLLABLE);
        //未选中的颜色，选中的颜色
        acTlTabtop.setTabTextColors(ContextCompat.getColor(this, R.color.colorGray),
                ContextCompat.getColor(this, R.color.colorWhite));
        //游标的颜色
        acTlTabtop.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorGolden));
        ViewCompat.setElevation(acTlTabtop, 10);
        acTlTabtop.setupWithViewPager(acVpPager);
    }

    private void initContent() {
        Bundle data = new Bundle();
        data.putSerializable("servantItem", servantItem);
        data.putSerializable("buffsItem", buffsItem);
        //标签列表
        indicatorList = new ArrayList<>();
        indicatorList.add("从者信息");
        indicatorList.add("伤害计算");
        indicatorList.add("NP充能计算");
        indicatorList.add("宝具计算");
        indicatorList.add("打星计算");
        //检查fragment
        if (infoFrag == null) {
            infoFrag = new InfoFrag();
            infoFrag.setArguments(data);
        }
        if (atkFrag == null) {
            atkFrag = AtkFrag.newInstance();
            atkFrag.setArguments(data);
        }
        if (npFrag == null) {
            npFrag = NpFrag.newInstance();
            npFrag.setArguments(data);
        }
        if (trumpFrag == null) {
            trumpFrag = TrumpFrag.newInstance();
            trumpFrag.setArguments(data);
        }
        if (starFrag == null) {
            starFrag = StarFrag.newInstance();
            starFrag.setArguments(data);
        }
        //Fragment列表
        fragList = new ArrayList<>();
        fragList.add(infoFrag);
        fragList.add(atkFrag);
        fragList.add(npFrag);
        fragList.add(trumpFrag);
        fragList.add(starFrag);
        //创建Presenter
        infoPresenter = new InfoPresenter(infoFrag,ctx);
        atkPresenter = new AtkPresenter(atkFrag,ctx);
        npPresenter = new NpPresenter(npFrag,ctx);
        trumpPresenter = new TrumpPresenter(trumpFrag,ctx);
        starPresenter = new StarPresenter(starFrag,ctx);
        //ViewPagerAdapter
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        acVpPager.setAdapter(contentAdapter);
    }

    public BuffsItem getBuffsItem() {
        return buffsItem;
    }

    public void setBuffsItem(BuffsItem buffsItem) {
        this.buffsItem = buffsItem;
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
