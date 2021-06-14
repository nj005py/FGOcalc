package org.phantancy.fgocalc.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.CalcViewPagerAdapter;
import org.phantancy.fgocalc.character_factory.DatabaseCharacter;
import org.phantancy.fgocalc.common.ActivityCollector;
import org.phantancy.fgocalc.common.App;
import org.phantancy.fgocalc.databinding.ActyMainBinding;
import org.phantancy.fgocalc.event.DatabaseEvent;
import org.phantancy.fgocalc.fragment.FilterFragment;
import org.phantancy.fgocalc.fragment.MainFragment;
import org.phantancy.fgocalc.fragment.SettingFragment;
import org.phantancy.fgocalc.util.DisplayUtil;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.text.MessageFormat;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //初始化窗口宽高
        initDisplay();
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
                .onExplainRequestReason((scope, deniedList) -> {
                    String msg = "电话状态仅用于统计APP安装数量\n存储权限用于数据库加载、图片缓存";
                    scope.showRequestReasonDialog(deniedList,msg,"确定");
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            //初始化友盟统计
                            UMConfigure.init(App.getAppContext(), "5a61306b8f4a9d420400090e", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "635f1a6fb0fe4b7fff7e18bfe4af9a77");
                            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
                            //初始化UI
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
        LiveEventBus.get(DatabaseEvent.class)
                .observe(this,(x) -> {
                    if (x.getCode() == DatabaseEvent.SUCCESS){
                        new DatabaseCharacter(MainActy.this).onSuccess();
                    }
                    if (x.getCode() == DatabaseEvent.ERROR) {
                        new DatabaseCharacter(MainActy.this).onError();
                    }
                    if (x.getCode() == DatabaseEvent.RELOAD) {
                        if (vm != null) {
                            vm.reloadDatabase();
                        }
                    }
                });
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
        CalcViewPagerAdapter pagerAdapter = new CalcViewPagerAdapter(this, fragments);
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
            } else {
                vm.clearKeyword();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DisplayUtil.INSTANCE.init(metrics.widthPixels, metrics.heightPixels);
    }
}
