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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.metaphysics.MetaphysicsActy;
import org.phantancy.fgocalc.activity.WebviewActy;
import org.phantancy.fgocalc.adapter.ServantCardViewAdapter;
import org.phantancy.fgocalc.base.BaseFrag;
import org.phantancy.fgocalc.dialog.AboutDialog;
import org.phantancy.fgocalc.dialog.MenulLocDialog;
import org.phantancy.fgocalc.dialog.UpdateDialog;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item_decoration.GridItemDecoration;
import org.phantancy.fgocalc.util.BaseUtils;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.util.ToolCase;
import org.phantancy.fgocalc.view.ClearEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by HATTER on 2017/11/1.
 */

public class ServantListFragment extends BaseFrag implements
        ServantListContract.View,
        View.OnClickListener{

    @BindView(R.id.fsl_ll_bar)
    LinearLayout fslLlBar;
    @BindView(R.id.fsl_tv_left)
    TextView fslTvLeft;
    @BindView(R.id.fsl_rb_search)
    RadioButton fslRbSearch;
    @BindView(R.id.fsl_rb_screen)
    RadioButton fslRbScreen;
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
    @BindView(R.id.fsl_tv_classtype)
    TextView fslTvClasstype;
    @BindView(R.id.fsl_sp_classtype)
    Spinner fslSpClasstype;
    @BindView(R.id.fsl_tv_star)
    TextView fslTvStar;
    @BindView(R.id.fsl_sp_star)
    Spinner fslSpStar;
    @BindView(R.id.fsl_btn_sreen)
    Button fslBtnSreen;
    @BindView(R.id.fsl_btn_clear)
    Button fslBtnClear;
    @BindView(R.id.fsl_ll_area_screen)
    LinearLayout fslLlAreaScreen;
    @BindView(R.id.fsl_rv_servant)
    RecyclerView fslRvServant;
    @BindView(R.id.fsl_iv_character)
    ImageView fslIvCharacter;
    @BindView(R.id.fsl_v_character)
    View fslVCharacter;
    @BindView(R.id.fsl_tv_character)
    TextView fslTvCharacter;
    @BindView(R.id.fsl_rl_character)
    RelativeLayout fslRlCharacter;
    @BindView(R.id.fsl_fl_main)
    FrameLayout fslFlMain;
    @BindView(R.id.fsl_nv_menu)
    NavigationView fslNvMenu;
    @BindView(R.id.fsl_dl_menu)
    DrawerLayout fslDlMenu;
    Unbinder unbinder;
    @BindView(R.id.fsl_ll_side_statusbar)
    LinearLayout fslLlSideStatusbar;
    @BindView(R.id.fsl_ll_sidebar)
    LinearLayout fslLlSidebar;
    Unbinder unbinder1;
    private ServantListContract.Presenter mPresenter;
    private final int READ_WRITE = 1;
    private final int WRITE_DOWNLOAD = 2;
    private ServantCardViewAdapter sAdapter;
    private String[] classType, starNum;//职阶 星
    private int[] starValue;//星(int)
    private String keyWord, curClassType;
    private int curStarValue;
    Bundle savedInstanceStateO;
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
        //获取数组，设置搜索栏
        classType = getResources().getStringArray(R.array.classType);
        starNum = getResources().getStringArray(R.array.star);
        starValue = getResources().getIntArray(R.array.star_value);
        ToolCase.spInitSimple(ctx, classType, fslSpClasstype);
        ToolCase.spInitSimple(ctx, starNum, fslSpStar);
        //判断侧滑菜单
        boolean locLeft = (Boolean) SharedPreferencesUtils.getParam(ctx, "locLeft", true);
        checkMenuLoc(locLeft);
        //设置列表展示方式
        View v = getLayoutInflater(savedInstanceState).inflate(R.layout.item_servant_cardview,null);
        CardView cv = (CardView)v.findViewById(R.id.isc_cv_servant);
        cv.measure(0,0);
        int width = cv.getMeasuredWidth();
        WindowManager wm = mActy.getWindowManager();
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int num = (int)Math.floor(screenWidth / width);
        Log.d(TAG,"width:" + width + " screenWidth:" + screenWidth + " num:" + num);
        StaggeredGridLayoutManager sgL = new StaggeredGridLayoutManager(num,StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager gl = new GridLayoutManager(ctx,num);
        fslRvServant.setLayoutManager(gl);
        fslRvServant.addItemDecoration(new GridItemDecoration(ctx,5));
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
    public void onResume() {
        super.onResume();
        mPresenter.start();
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
        unbinder.unbind();
    }

    @Override
    public void setPresenter(ServantListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void init() {
//        sAdapter = new ServantCardViewAdapter(ctx,mPresenter.getAllServants());
        sAdapter = new ServantCardViewAdapter(null,ctx,fslTvCharacter,fslRlCharacter);
        fslRvServant.setAdapter(sAdapter);
        //检查app版本、数据库版本
        mPresenter.simpleCheck(ctx,mActy);
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
        fslRlCharacter.setOnClickListener(this);
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
                    mPresenter.searchServantsByKeyword(keyWord);
                    return true;
                }
                return false;
            }
        });
        fslSpClasstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curClassType = classType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curClassType = classType[0];
            }
        });

        fslSpStar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curStarValue = starValue[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curStarValue = starValue[0];
            }
        });

        fslRgMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fsl_rb_search:
                        fslLlAreaSearch.setVisibility(View.VISIBLE);
                        fslLlAreaScreen.setVisibility(View.GONE);
                        mPresenter.getAllServants();
                        break;
                    case R.id.fsl_rb_screen:
                        fslLlAreaSearch.setVisibility(View.GONE);
                        fslLlAreaScreen.setVisibility(View.VISIBLE);
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
                    case R.id.nsm_metaphysics:
                        intent.setClass(ctx, MetaphysicsActy.class);
                        startActivity(intent);
                        break;
                    case R.id.nsm_notice:
                        intent.setClass(ctx, WebviewActy.class);
                        intent.putExtra("url","http://nj005py.gitee.io/fgocalc/");
                        startActivity(intent);
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
                            ActivityCompat.requestPermissions(mActy, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_DOWNLOAD );
                        } else {
                            mPresenter.downloadDatabaseExtra();
                        }
                        break;
                    case R.id.nsm_fgotool:
                        mPresenter.fgotool();
                        break;
                    case R.id.nsm_fgosimulator:
                        mPresenter.fgosimulator();
                        break;
                    case R.id.nsm_feedback:
                        mPresenter.feedback();
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
            case R.id.fsl_rl_character:
                fslRlCharacter.setVisibility(View.GONE);
                break;
            case R.id.fsl_ll_search:
                keyWord = ToolCase.getViewValue(fslEtSearch);
                mPresenter.searchServantsByKeyword(keyWord);
                break;
            case R.id.fsl_btn_clear:
                mPresenter.getAllServants();
                break;
            case R.id.fsl_btn_sreen:
                mPresenter.searchServantsByCondition(curClassType,curStarValue);
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
        AboutDialog ad = new AboutDialog(ctx);
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.app_about)).append("\n\n当前版本").append(mPresenter.getVersion());
        ad.setAbout(sb.toString());
        ad.show();
    }

    @Override
    public void showUpdateDiag(String update, final String downloadUrl, final String curVersion) {
        final UpdateDialog up = new UpdateDialog(ctx);
        if (ToolCase.notEmpty(update)) {
            up.setUpdate(update);
        }
        up.setDownloadListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(downloadUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        up.setIgnoreListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG,"curVersion:" + curVersion);
                    SharedPreferencesUtils.setParam(ctx,"ignoreVersion",curVersion);
                }else{
                    Log.d(TAG,"curVersion:1");
                    SharedPreferencesUtils.setParam(ctx,"ignoreVersion","1");
                }
            }
        });
        up.show();
    }

    @Override
    public void showCharacter(String content,int img) {
        ToolCase.setViewValue(fslTvCharacter,content);
        fslIvCharacter.setImageResource(img);
        fslRlCharacter.setVisibility(View.VISIBLE);
        fslRlCharacter.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
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
                    mPresenter.downloadDatabaseExtra();
                } else {
                    ToastUtils.displayShortToast(ctx, "您拒绝了权限，无法下载更新");
                }
                break;
        }
    }
}
