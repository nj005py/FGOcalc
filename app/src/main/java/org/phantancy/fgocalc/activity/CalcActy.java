package org.phantancy.fgocalc.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter;
import org.phantancy.fgocalc.databinding.ActyCalcBinding;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.fragment.BuffFragment;
import org.phantancy.fgocalc.fragment.CalcContainerFragment;
import org.phantancy.fgocalc.fragment.CalcFragment;
import org.phantancy.fgocalc.fragment.ConditionFragment;
import org.phantancy.fgocalc.fragment.InfoFragment;
import org.phantancy.fgocalc.fragment.WikiFragment;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

import java.util.ArrayList;
import java.util.List;

public class CalcActy extends BaseActy {
    private ActyCalcBinding binding;
    private CalcViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActyCalcBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ServantEntity servant = (ServantEntity) getIntent().getParcelableExtra("servant");

        vm = new ViewModelProvider(this).get(CalcViewModel.class);
        if (servant != null) {
            vm.setServant(servant);
        }
        /**
         * 创建ViewPager Adapter
         */
        WikiFragment wikiFrag = new WikiFragment();
        wikiFrag.setParentPager(binding.vpCalcPager);

        List<Fragment> fragments = new ArrayList<Fragment>() {
            {
                add(new InfoFragment());
                add(wikiFrag);
//                add(new BuffFragment());
//                add(new ConditionFragment());
//                add(new CalcFragment());
                add(new CalcContainerFragment());
            }
        };

        List<String> tabs = new ArrayList<String>() {
            {
                add("从者信息");
                add("wiki");
//                add("Buff设置");
//                add("条件设置");
                add("计算");
            }
        };

        //set viewpager
        CalcViewPagerAdapter pagerAdapter = new CalcViewPagerAdapter(this, fragments);
        binding.vpCalcPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(binding.tlCalcTab, binding.vpCalcPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabs.get(position));
            }
        }).attach();

        vm.getCurrentPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer page) {
                binding.vpCalcPager.setCurrentItem(page);
            }
        });

        //延迟加载
        postponeEnterTransition();
//        scheduleStartPostonedTransition(binding.vpCalcPager);

    }

    private void scheduleStartPostonedTransition(final View x){
        x.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){
            @Override
            public boolean onPreDraw() {
                x.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.vpCalcPager.setOffscreenPageLimit(5);
    }
}
