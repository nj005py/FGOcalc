 package org.phantancy.fgocalc.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter;
import org.phantancy.fgocalc.common.ActivityCollector;
import org.phantancy.fgocalc.databinding.ActyMainBinding;
import org.phantancy.fgocalc.fragment.FilterFragment;
import org.phantancy.fgocalc.fragment.MainFragment;
import org.phantancy.fgocalc.fragment.SettingFragment;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActy extends BaseActy {

    private ActyMainBinding binding;
    private MainViewModel vm;
    //用于记录退出时间
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);

        /**
         * 检查权限
         * 电话状态-统计用户量
         * 文件读取-读数据库，图片缓存
         * 文件写入-下载更新、图片
         */
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            binding = ActyMainBinding.inflate(getLayoutInflater());
                            setContentView(binding.getRoot());
                            init();
                        } else {
                            ToastUtils.displayShortToast(ctx, getString(R.string.permission_deny));
                        }
                    }
                });

    }

    private void init() {

        vm = new ViewModelProvider(this).get(MainViewModel.class);

        //搜索 筛选fragment
        List<Fragment> fragments = new ArrayList<>();
        //准备fragment
        fragments.add(new MainFragment(MainFragment.ENTRY_MAIN));
        fragments.add(new FilterFragment());
        fragments.add(new SettingFragment());

        //set tabs
        List<String> tabs = new ArrayList<>();
        tabs.add("搜索");
        tabs.add("筛选");
        tabs.add("设置");
        //set viewpager
        CalcViewPagerAdapter pagerAdapter = new CalcViewPagerAdapter(this,fragments);
        binding.vpPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tlTab, binding.vpPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabs.get(position));
            }
        }).attach();

        vm.getCurrentPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer page) {
                binding.vpPager.setCurrentItem(page);
            }
        });
    }

    //重载返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //判断搜索栏状态
            if (TextUtils.isEmpty(vm.getCurrentKeyword())) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(ctx, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    ActivityCollector.finishAll();
                }
            }else{
                vm.clearKeyword();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
