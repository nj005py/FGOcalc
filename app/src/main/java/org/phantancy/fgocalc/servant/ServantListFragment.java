package org.phantancy.fgocalc.servant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.activity.PartyActy;
import org.phantancy.fgocalc.activity.WebviewActy;
import org.phantancy.fgocalc.adapter.FilterAdapter;
import org.phantancy.fgocalc.adapter.ServantCardViewAdapter;
import org.phantancy.fgocalc.base.BaseFrag;
import org.phantancy.fgocalc.dialog.AboutDialog;
import org.phantancy.fgocalc.dialog.MenulLocDialog;
import org.phantancy.fgocalc.dialog.UpdateDialog;
import org.phantancy.fgocalc.event.DatabaseEvent;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item.TipItem;
import org.phantancy.fgocalc.item.UpdateItem;
import org.phantancy.fgocalc.item_decoration.GridItemDecoration;
import org.phantancy.fgocalc.metaphysics.MetaphysicsActy;
import org.phantancy.fgocalc.util.BaseUtils;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.util.ToolCase;
import org.phantancy.fgocalc.view.ClearEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by HATTER on 2017/11/1.
 */

public class ServantListFragment extends BaseFrag implements
        ServantListContract.View,
        View.OnClickListener {

    @BindView(R.id.fsl_ll_bar)
    LinearLayout fslLlBar;
    @BindView(R.id.fsl_tv_left)
    TextView fslTvLeft;
    @BindView(R.id.fsl_rb_search)
    RadioButton fslRbSearch;
    @BindView(R.id.fsl_rb_filter)
    RadioButton fslRbFilter;
    @BindView(R.id.fsl_rg_method)
    RadioGroup fslRgMethod;
    @BindView(R.id.fsl_tv_right)
    TextView fslTvRight;
    @BindView(R.id.fsl_et_search)
    ClearEditText fslEtSearch;
    @BindView(R.id.fsl_ll_search)
    LinearLayout fslLlSearch;
    @BindView(R.id.fsl_ll_area_search)
    LinearLayout fslLlAreaSearch;
    @BindView(R.id.fsl_rv_filter)
    RecyclerView fslRvFilter;
    @BindView(R.id.fsl_btn_sreen)
    Button fslBtnSreen;
    @BindView(R.id.fsl_btn_clear)
    Button fslBtnClear;
    @BindView(R.id.fsl_ll_area_filter)
    LinearLayout fslLlAreaFilter;
    @BindView(R.id.fsl_rv_servant)
    RecyclerView fslRvServant;
    @BindView(R.id.fsl_fl_main)
    FrameLayout fslFlMain;
    @BindView(R.id.fsl_ll_side_statusbar)
    LinearLayout fslLlSideStatusbar;
    @BindView(R.id.fsl_nv_menu)
    NavigationView fslNvMenu;
    @BindView(R.id.fsl_ll_sidebar)
    LinearLayout fslLlSidebar;
    @BindView(R.id.fsl_dl_menu)
    DrawerLayout fslDlMenu;
    Unbinder unbinder;
    private ServantListContract.Presenter mPresenter;
    private final int READ_WRITE = 1;
    private final int WRITE_DOWNLOAD = 2;
    private ServantCardViewAdapter sAdapter;
    private FilterAdapter fAdapter;
    private String keyWord;
    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!ToolCase.notEmpty(ToolCase.etValue(fslEtSearch))) {
                mPresenter.getAllServants();
            }
        }
    };

    public static ServantListFragment newInstance() {
        return new ServantListFragment();
    }

    //require empty public constructor
    public ServantListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_servant_list, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化界面、状态栏
        initStatusBar();

        //判断侧滑菜单
        boolean locLeft = (Boolean) SharedPreferencesUtils.getParam(ctx, "locLeft", true);
        checkMenuLoc(locLeft);

        //设置从者列表
        View v = getLayoutInflater().inflate(R.layout.item_servant_cardview, null);
        ConstraintLayout cv = (ConstraintLayout) v.findViewById(R.id.isc_cl_card);
        cv.measure(0, 0);
        int width = cv.getMeasuredWidth();
        DisplayMetrics dm = new DisplayMetrics();
        mActy.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int num = (int) Math.floor(screenWidth / width);
        int widthSpace = (int) ((screenWidth - (width * num)) / (num * 2));
        GridLayoutManager lm = new GridLayoutManager(ctx, num);
        fslRvServant.setLayoutManager(lm);
        fslRvServant.addItemDecoration(new GridItemDecoration(ctx, widthSpace, 5));

        //设置筛选列表
        fAdapter = new FilterAdapter(ctx,mPresenter.getFilterItems());
        LinearLayoutManager lmFilter = new LinearLayoutManager(ctx);
        lmFilter.setOrientation(LinearLayoutManager.HORIZONTAL);
        fslRvFilter.setAdapter(fAdapter);
        fslRvFilter.setLayoutManager(lmFilter);

        //设置监听
        setListener();

        //检查权限
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActy, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE);
        } else {
            init();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterReceiver(ctx);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fslEtSearch.removeTextChangedListener(searchWatcher);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DatabaseEvent e) {
        if (e.ifReload) {
            mPresenter.reloadDatabase();
        }
    }

    @Override
    public void setPresenter(ServantListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void init() {
//        sAdapter = new ServantCardViewAdapter(ctx,mPresenter.getAllServants());
        sAdapter = new ServantCardViewAdapter(null, ctx, mActy);
        fslRvServant.setAdapter(sAdapter);
        //检查app版本、数据库版本
        mPresenter.simpleCheck(ctx, mActy);
//        mPresenter.getAllServants();
    }

    public void initStatusBar() {
        int height = BaseUtils.getStatusBarHeight(ctx);
        fslLlBar.setPadding(0, height, 0, 0);
        fslLlSideStatusbar.setPadding(0, height, 0, 0);
        if (Build.VERSION.SDK_INT == 19) {
            mActy.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mActy.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //將側邊欄頂部延伸至status bar
            //将侧滑菜单顶部延伸到状态栏
            fslDlMenu.setFitsSystemWindows(true);
            fslDlMenu.setClipToPadding(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = mActy.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);
//            calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

    public void setListener() {
        fslTvLeft.setOnClickListener(this);
        fslTvRight.setOnClickListener(this);
        fslLlSearch.setOnClickListener(this);
        fslBtnClear.setOnClickListener(this);
        fslBtnSreen.setOnClickListener(this);
        fslEtSearch.addTextChangedListener(searchWatcher);
        fslEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // 先隐藏键盘
                    ((InputMethodManager) fslEtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    mActy.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    keyWord = ToolCase.etValue(fslEtSearch);
                    //过滤中英文逗号
                    keyWord = keyWord.replace(",","");
                    keyWord = keyWord.replace("，","");
                    mPresenter.searchServantsByKeyword(keyWord);
                    return true;
                }
                return false;
            }
        });

        fslRgMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fsl_rb_search:
                        fslLlAreaSearch.setVisibility(View.VISIBLE);
                        fslLlAreaFilter.setVisibility(View.GONE);
                        mPresenter.getAllServants();
                        break;
                    case R.id.fsl_rb_filter:
                        fslLlAreaSearch.setVisibility(View.GONE);
                        fslLlAreaFilter.setVisibility(View.VISIBLE);
                        mPresenter.getAllServants();
                        break;
                }
            }
        });
        fslNvMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.nsm_about:
                        showAboutDialog();
                        break;
                    case R.id.nsm_follow:
                        mPresenter.follow();
                        break;
                    case R.id.nsm_party:
                        mPresenter.goParty();
                        break;
                    case R.id.nsm_metaphysics:
                        intent.setClass(ctx, MetaphysicsActy.class);
                        startActivity(intent);
                        break;
                    case R.id.nsm_notice:
                        if (BaseUtils.isNetworkAvailable(ctx)) {
                            intent.setClass(ctx, WebviewActy.class);
                            intent.putExtra("url", "https://nj005py.github.io/FGOcalc_web/data/html/guide");
                            startActivity(intent);
                        } else {
                            ToolCase.showTip(ctx, "tip_no_network.json");
                        }
                        break;
                    case R.id.nsm_check_update:
                        if (BaseUtils.isNetworkAvailable(ctx)) {
                            mPresenter.checkAppUpdate(true);
                        } else {
                            ToolCase.showTip(ctx, "tip_no_network.json");
                        }
                        break;
                    case R.id.nsm_menu_loc:
                        showMenuLocDialog();
                        break;
                    case R.id.nsm_reload_database:
                        //ctrl + alt + b直接跳到实现方法
                        mPresenter.reloadDatabase();
                        break;
                    case R.id.nsm_download_database_extra:
                        //判断有无写入权限
                        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(mActy, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_DOWNLOAD);
                        } else {
                            if (BaseUtils.isNetworkAvailable(ctx)) {
                                mPresenter.downloadDatabaseExtra();
                            } else {
                                ToolCase.showTip(ctx, "tip_no_network.json");
                            }
                        }
                        break;
                    case R.id.nsm_fgotool:
                        mPresenter.fgotool();
                        break;
                    case R.id.nsm_fgosimulator:
                        mPresenter.fgosimulator();
                        break;
                    case R.id.nsm_qq:
                        mPresenter.qq();
                        break;
                    case R.id.nsm_feedback:
                        mPresenter.feedback();
                        break;
                    case R.id.nsm_share:
                        ToolCase.copy2Clipboard(ctx, "FGOcalc，你的掌上FGO计算器APP：https://nj005py.github.io/FGOcalc_web/data/html/guide", "共享链接已复制到剪切板");
                        break;
                }
                fslDlMenu.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fsl_ll_search:
                keyWord = ToolCase.getViewValue(fslEtSearch);
                //过滤中英文逗号
                keyWord = keyWord.replace(",","");
                keyWord = keyWord.replace("，","");
                mPresenter.searchServantsByKeyword(keyWord);
                break;
            case R.id.fsl_btn_clear:
                fAdapter.clearFilter();
                mPresenter.getAllServants();
                break;
            case R.id.fsl_btn_sreen:
                mPresenter.searchServantsByCondition(fAdapter.getItems());
                break;
            case R.id.fsl_tv_left:
                fslDlMenu.openDrawer(fslLlSidebar);
                break;
            case R.id.fsl_tv_right:
                fslDlMenu.openDrawer(fslLlSidebar);
                break;
        }
    }

    @Override
    public void showMenuLocDialog() {
        final MenulLocDialog md = new MenulLocDialog(ctx);
        boolean locLeft = (Boolean) SharedPreferencesUtils.getParam(ctx, "locLeft", true);
        md.setCheck(locLeft);
        md.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fslTvLeft.setVisibility(View.VISIBLE);
                fslTvRight.setVisibility(View.GONE);
                DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) fslLlSidebar.getLayoutParams();
                lp.gravity = Gravity.START;
                fslLlSidebar.setLayoutParams(lp);
                SharedPreferencesUtils.setParam(ctx, "locLeft", true);
                fslDlMenu.openDrawer(fslLlSidebar);
                md.dismiss();
            }
        });

        md.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fslTvLeft.setVisibility(View.GONE);
                fslTvRight.setVisibility(View.VISIBLE);
                DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) fslLlSidebar.getLayoutParams();
                lp.gravity = Gravity.END;
                fslLlSidebar.setLayoutParams(lp);
                SharedPreferencesUtils.setParam(ctx, "locLeft", false);
                fslDlMenu.openDrawer(fslLlSidebar);
                md.dismiss();
            }
        });
        md.show();
    }

    private void checkMenuLoc(boolean locLeft) {
        if (locLeft) {
            fslTvLeft.setVisibility(View.VISIBLE);
            fslTvRight.setVisibility(View.GONE);
            DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) fslLlSidebar.getLayoutParams();
            lp.gravity = Gravity.START;
            fslLlSidebar.setLayoutParams(lp);
        } else {
            fslTvLeft.setVisibility(View.GONE);
            fslTvRight.setVisibility(View.VISIBLE);
            DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) fslLlSidebar.getLayoutParams();
            lp.gravity = Gravity.END;
            fslLlSidebar.setLayoutParams(lp);
        }
    }

    @Override
    public void showAboutDialog() {
        //设置dialogue尺寸
        DisplayMetrics metrics = new DisplayMetrics();
        mActy.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AboutDialog ad = new AboutDialog(ctx);
        ad.getWindow().setLayout((int) (width * 0.9), (int) (height * 0.8));
        StringBuilder sb = new StringBuilder();
        sb.append(mPresenter.getVersion());
        ad.setVersion(sb.toString());
        ad.show();
    }

    @Override
    public void showUpdateDiag(final UpdateItem updateItem) {
        final UpdateDialog up = new UpdateDialog(ctx);
        if (updateItem != null) {
            up.setUpdate(updateItem);
        }
        up.setDownloadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(updateItem.getUrl());
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        up.setIgnoreListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtils.setParam(ctx, "ignVersion", updateItem.getVersionCode());
                } else {
                    SharedPreferencesUtils.setParam(ctx, "ignVersion", 0);
                }
            }
        });
        up.show();
    }

    @Override
    public void setServantList(List<ServantItem> list) {
        sAdapter.setItems(list);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    ToastUtils.displayShortToast(ctx, "您拒绝了权限，部分功能无法正常工作");
                }
                break;
            case WRITE_DOWNLOAD:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (BaseUtils.isNetworkAvailable(ctx)) {
                        mPresenter.downloadDatabaseExtra();
                    } else {
                        ToolCase.showTip(ctx, "tip_no_network.json");
                    }
                } else {
                    ToastUtils.displayShortToast(ctx, "您拒绝了权限，无法下载更新");
                }
                break;
        }
    }
}
