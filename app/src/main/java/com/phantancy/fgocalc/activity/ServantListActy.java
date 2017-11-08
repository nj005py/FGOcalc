package com.phantancy.fgocalc.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.adapter.ItemAdapter;
import com.phantancy.fgocalc.adapter.MenuAdapter;
import com.phantancy.fgocalc.adapter.ServantCardViewAdapter;
import com.phantancy.fgocalc.database.DBManager;
import com.phantancy.fgocalc.dialog.AboutDialog;
import com.phantancy.fgocalc.dialog.MenulLocDialog;
import com.phantancy.fgocalc.dialog.UpdateDialog;
import com.phantancy.fgocalc.item.Item;
import com.phantancy.fgocalc.item.MenuItem;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.BaseUtils;
import com.phantancy.fgocalc.util.MyDatabaseUtil;
import com.phantancy.fgocalc.util.SharedPreferencesUtils;
import com.phantancy.fgocalc.util.ToastUtils;
import com.phantancy.fgocalc.view.ClearEditText;
import com.phantancy.fgocalc.view.DividerItemDecoration;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by PY on 2016/10/31.
 */
public class ServantListActy extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.asl_rb_search)
    RadioButton aslRbSearch;
    @BindView(R.id.asl_rb_screen)
    RadioButton aslRbScreen;
    @BindView(R.id.asl_rg_method)
    RadioGroup aslRgMethod;
    @BindView(R.id.asl_et_search)
    ClearEditText aslEtSearch;
    @BindView(R.id.asl_ll_search)
    LinearLayout aslLlSearch;
    @BindView(R.id.asl_ll_area_search)
    LinearLayout aslLlAreaSearch;
    @BindView(R.id.asl_tv_classtype)
    TextView aslTvClasstype;
    @BindView(R.id.asl_sp_classtype)
    Spinner aslSpClasstype;
    @BindView(R.id.asl_tv_star)
    TextView aslTvStar;
    @BindView(R.id.asl_sp_star)
    Spinner aslSpStar;
    @BindView(R.id.asl_btn_sreen)
    Button aslBtnSreen;
    @BindView(R.id.asl_btn_clear)
    Button aslBtnClear;
    @BindView(R.id.asl_ll_area_screen)
    LinearLayout aslLlAreaScreen;
    @BindView(R.id.asl_fl_main)
    FrameLayout aslFlMain;
    @BindView(R.id.asl_rv_menu)
    RecyclerView aslRvMenu;
    @BindView(R.id.asl_ll_menu)
    LinearLayout aslLlMenu;
    @BindView(R.id.asl_dl_menu)
    DrawerLayout aslDlMenu;
    @BindView(R.id.asl_ll_bar)
    LinearLayout aslLlBar;
    @BindView(R.id.asl_ll_side_bar)
    LinearLayout aslLlSideBar;
    @BindView(R.id.asl_iv_character)
    ImageView aslIvCharacter;
    @BindView(R.id.asl_v_character)
    View aslVCharacter;
    @BindView(R.id.asl_tv_character)
    TextView aslTvCharacter;
    @BindView(R.id.asl_rl_character)
    RelativeLayout aslRlCharacter;
    @BindView(R.id.asl_tv_left)
    TextView aslTvLeft;
    @BindView(R.id.asl_tv_about)
    TextView aslTvAbout;
    @BindView(R.id.asl_tv_right)
    TextView aslTvRight;
    @BindView(R.id.asl_rv_servant)
    RecyclerView aslRvServant;
    private DBManager dbManager;
    private SQLiteDatabase database;
    private ItemAdapter itemAdapter;
    private List<ServantItem> servantList = new ArrayList<>();
    private ServantItem sItem;//用于占位的
    private long exitTime = 0;//用于记录退出时间
    private String[] classType, starNum;
    private int[] starValue;
    private String keyWord, curClassType;
    private int curStarValue;
    private List<MenuItem> menuList;
    private MenuAdapter menuAdapter;
    private MyDatabaseUtil myDatabaseUtil;
    private int curDbVersion;
    private int preDbVersion;
    private String downloadUrl, checkUrl = "http://git.oschina.net/nj005py/fgocalc/raw/master/fgocalc_config.xml";
    private String preVersion;
    private String update;//更新文本
    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!notEmpty(etValue(aslEtSearch))) {
                setMethod(0);
            }
        }
    };
    private final int READ_WRITE = 1;
    private ServantCardViewAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_servant_list);
        mContext = this;
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initStatusBar();
        setMenu();
        preVersion = getVersion();
        preDbVersion = (int) SharedPreferencesUtils.getParam(mContext, "dbVersion", 1);
        Log.d(TAG, "preVersion->" + preVersion);
        //注册广播接收器
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ServantListActy.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE);
        } else {
            //判断网络是否可用
            if (isNetworkAvailable(mContext)) {
                //下载配置文件
                downloadConfig();
            }
        }
        if (checkDatabase()) {
            File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
            dbFile.delete();
            ToastUtils.displayShortToast(mContext, "数据库更新");
            aslRlCharacter.setVisibility(View.VISIBLE);
            aslRlCharacter.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
        }
        dbManager = new DBManager(this);
//        dbManager.openDatabase();
//        dbManager.closeDatabase();
//        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
        //实装末端servant
        sItem = new ServantItem();
        sItem.setId(999);
        sItem.setName("百度月系吧刊组");//6星天花板
        sItem.setClass_type("Creator");
        sItem.setStar(6);
//        itemAdapter = new ItemAdapter(this);
//        aslLvServant.setAdapter(itemAdapter);
        StaggeredGridLayoutManager sgL = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        aslRvServant.setLayoutManager(sgL);

        setMethod(0);
        classType = getResources().getStringArray(R.array.classType);
        starNum = getResources().getStringArray(R.array.star);
        starValue = getResources().getIntArray(R.array.star_value);
        spInitSimple(mContext, classType, aslSpClasstype);
        spInitSimple(mContext, starNum, aslSpStar);
        setListener();
    }

    private void initStatusBar() {
        int height = BaseUtils.getStatusBarHeight(mContext);
        aslLlBar.setPadding(0, height, 0, 0);
        aslLlSideBar.setPadding(0, height, 0, 0);
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
//            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    //左侧菜单
    private void setMenu() {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams lp = aslLlMenu.getLayoutParams();
        lp.width = width * 3 / 4;
        aslLlMenu.setLayoutParams(lp);
        //菜单名称列表
        String[] menuNames = {"玄学计算器", "夕学计算器", "冰学计算器", "偷渡GO", "砸圣晶石(暂无意义)", "菜单位置", "反馈"};
        menuList = new ArrayList<>();
        for (int i = 0; i < menuNames.length; i++) {
            MenuItem mItem = new MenuItem();
            mItem.setMenuName(menuNames[i]);
            menuList.add(mItem);
        }
        aslRvMenu.setLayoutManager(new LinearLayoutManager(this));
        aslRvMenu.setItemAnimator(new DefaultItemAnimator());
        aslRvMenu.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        boolean locLeft = (Boolean) SharedPreferencesUtils.getParam(mContext, "locLeft", true);
        checkMenuLoc(locLeft);
        menuAdapter = new MenuAdapter(mContext, menuList);
        aslRvMenu.setAdapter(menuAdapter);
//        aslDlMenu.closeDrawer(GravityCompat.START);
//        setDrawerLeftEdgeSize(ServantListActy.this, aslDlMenu, 0.5f);
    }

    private void setListener() {
        aslTvAbout.setOnClickListener(this);
        aslTvLeft.setOnClickListener(this);
        aslTvRight.setOnClickListener(this);
        aslLlSearch.setOnClickListener(this);
        aslBtnClear.setOnClickListener(this);
        aslBtnSreen.setOnClickListener(this);
        aslRlCharacter.setOnClickListener(this);
        aslEtSearch.addTextChangedListener(searchWatcher);
        aslEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // 先隐藏键盘
                    ((InputMethodManager) aslEtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    ServantListActy.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    setMethod(1);
                    return true;
                }
                return false;
            }
        });
        aslSpClasstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curClassType = classType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curClassType = classType[0];
            }
        });

        aslSpStar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curStarValue = starValue[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                curStarValue = starValue[0];
            }
        });

        aslRgMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.asl_rb_search:
                        aslLlAreaSearch.setVisibility(View.VISIBLE);
                        aslLlAreaScreen.setVisibility(View.GONE);
                        setMethod(0);
                        break;
                    case R.id.asl_rb_screen:
                        aslLlAreaSearch.setVisibility(View.GONE);
                        aslLlAreaScreen.setVisibility(View.VISIBLE);
                        setMethod(0);
                        break;
                }
            }
        });

//        aslLvServant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ServantItem item = (ServantItem) servantList.get(position);
//                if (item.getId() != 999) {
//                    ServantItem sItem = (ServantItem) itemAdapter.getItem(position);
//                    Intent sIntent = new Intent(ctx, MainActivity.class);
//                    sIntent.putExtra("servants", sItem);
//                    startActivity(sIntent);
//                }
//            }
//        });

        aslDlMenu.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });

        menuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(mContext, MetaphysicsActy.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(mContext, ExtractActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(mContext, ExtractActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(mContext, MapActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(mContext, FateGoActy.class);
                        startActivity(intent);
                        break;
                    case 5:
                        showMenuLocDialog();
                        break;
                    case 6:
                        sendEmail();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asl_ll_search:
                setMethod(1);
                break;
            case R.id.asl_btn_clear:
                setMethod(0);
                break;
            case R.id.asl_btn_sreen:
                setMethod(2);
                break;
            case R.id.asl_rl_character:
                aslRlCharacter.setVisibility(View.GONE);
                break;
            case R.id.asl_tv_left:
                aslDlMenu.openDrawer(aslLlMenu);
                break;
            case R.id.asl_tv_right:
                aslDlMenu.openDrawer(aslLlMenu);
                break;
            case R.id.asl_tv_about:
                showAboutDialog();
                break;
        }
    }

    private void showAboutDialog() {
        AboutDialog ad = new AboutDialog(mContext);
        ad.show();
    }

    private void showMenuLocDialog() {
        final MenulLocDialog md = new MenulLocDialog(mContext);
        boolean locLeft = (Boolean) SharedPreferencesUtils.getParam(mContext, "locLeft", true);
        md.setCheck(locLeft);
        md.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aslTvLeft.setVisibility(View.VISIBLE);
                aslTvRight.setVisibility(View.GONE);
                DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) aslLlMenu.getLayoutParams();
                lp.gravity = Gravity.START;
                aslLlMenu.setLayoutParams(lp);
                SharedPreferencesUtils.setParam(mContext, "locLeft", true);
                md.dismiss();
            }
        });

        md.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aslTvLeft.setVisibility(View.GONE);
                aslTvRight.setVisibility(View.VISIBLE);
                DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) aslLlMenu.getLayoutParams();
                lp.gravity = Gravity.END;
                aslLlMenu.setLayoutParams(lp);
                SharedPreferencesUtils.setParam(mContext, "locLeft", false);
                md.dismiss();
            }
        });
        md.show();
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
//        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:phantancy@hotmail.com"));// only email apps should handle this
//        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"phantancy@hotmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "fgo计算器反馈");
        i.putExtra(Intent.EXTRA_TEXT, "我想反馈");
        try {
            startActivity(Intent.createChooser(i, "邮件反馈"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(mContext, "没找到Email相关应用.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkMenuLoc(boolean locLeft) {
        if (locLeft) {
            aslTvLeft.setVisibility(View.VISIBLE);
            aslTvRight.setVisibility(View.GONE);
            DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) aslLlMenu.getLayoutParams();
            lp.gravity = Gravity.START;
            aslLlMenu.setLayoutParams(lp);
        } else {
            aslTvLeft.setVisibility(View.GONE);
            aslTvRight.setVisibility(View.VISIBLE);
            DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) aslLlMenu.getLayoutParams();
            lp.gravity = Gravity.END;
            aslLlMenu.setLayoutParams(lp);
        }
    }

    //更新对话框
    private void showUpdateDiag() {
        final UpdateDialog up = new UpdateDialog(mContext);
        if (notEmpty(update)) {
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
        up.show();
    }

    //设置工作模式获取数据
    private void setMethod(int method) {
        //method 0：获取全部，1：搜索，2：筛选
//        database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
        //获取数据库
        database = dbManager.getDatabase();
        Cursor cur;
        switch (method) {
            case 0:
                cur = database.rawQuery("SELECT * FROM servants", null);
                servantList = getServants(cur);
//                servantList.add(sItem);
//                itemAdapter.setItems(servantList);
//                itemAdapter.notifyDataSetChanged();
                database.close();
                sAdapter = new ServantCardViewAdapter(mContext,servantList);
                aslRvServant.setAdapter(sAdapter);
                sAdapter.notifyDataSetChanged();
                break;
            case 1:
                keyWord = etValue(aslEtSearch);
                if (notEmpty(keyWord)) {
                    keyWord = tc2sc(keyWord);
                    cur = database.rawQuery("SELECT * FROM servants WHERE name LIKE ? OR nickname LIKE ? ORDER BY id",
                            new String[]{"%" + keyWord + "%", "%" + keyWord + "%"});
                    servantList = getServants(cur);
//                    itemAdapter.setItems(servantList);
//                    itemAdapter.notifyDataSetChanged();
                }
                database.close();
                sAdapter.setItems(servantList);
                sAdapter.notifyDataSetChanged();
                break;
            case 2:
                Log.d(TAG, "curClassType->" + curClassType + " curStarValue->" + curStarValue);
                if (notEmpty(curClassType)) {
                    cur = database.rawQuery("SELECT * FROM servants WHERE class_type = ? AND star = ? ORDER BY id",
                            new String[]{curClassType, curStarValue + ""});
                    servantList = getServants(cur);
                    if (servantList != null && servantList.size() > 0) {
                        servantList.add(sItem);
                    }
//                    itemAdapter.setItems(servantList);
//                    itemAdapter.notifyDataSetChanged();
                }
                database.close();
                sAdapter.setItems(servantList);
                sAdapter.notifyDataSetChanged();
                break;
            default:
                cur = database.rawQuery("SELECT * FROM servants", null);
                servantList = getServants(cur);
//                servantList.add(sItem);
//                itemAdapter.setItems(servantList);
//                itemAdapter.notifyDataSetChanged();
                database.close();
                sAdapter.setItems(servantList);
                sAdapter.notifyDataSetChanged();
                break;
        }
        database.close();
    }

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
    }

    //获取从者
    private ArrayList<ServantItem> getServants(Cursor cur) {
        if (cur != null) {
            int NUM_SERVANT = cur.getCount();
            ArrayList<ServantItem> cache = new ArrayList<>();
            if (cur.moveToFirst()) {
                do {
                    int id = cur.getInt(0);
                    String name = cur.getString(1);
                    String nickname = cur.getString(2);
                    String class_type = cur.getString(3);
                    int star = cur.getInt(4);
                    int arts_hit = cur.getInt(5);
                    int buster_hit = cur.getInt(6);
                    int quick_hit = cur.getInt(7);
                    int ex_hit = cur.getInt(8);
                    double quick_na = cur.getDouble(9);
                    double arts_na = cur.getDouble(10);
                    double buster_na = cur.getDouble(11);
                    double ex_na = cur.getDouble(12);
                    double trump_na = cur.getDouble(13);
                    double nd = cur.getDouble(14);
                    double arts_buff = cur.getDouble(15);
                    double buster_buff = cur.getDouble(16);
                    double quick_buff = cur.getDouble(17);
                    double atk_buff = cur.getDouble(18);
                    double special_buff = cur.getDouble(19);
                    double critical_buff = cur.getDouble(20);
                    int solid_buff = cur.getInt(21);
                    int buster_num = cur.getInt(22);
                    int arts_num = cur.getInt(23);
                    int quick_num = cur.getInt(24);
                    double star_occur = cur.getDouble(25);
                    double trump_lv1 = cur.getDouble(26);
                    double trump_lv2 = cur.getDouble(27);
                    double trump_lv3 = cur.getDouble(28);
                    double trump_lv4 = cur.getDouble(29);
                    double trump_lv5 = cur.getDouble(30);
                    String trump_color = cur.getString(31);
                    ServantItem servantItem = new ServantItem();
                    servantItem.setId(id);
                    servantItem.setName(name);
                    servantItem.setNickname(nickname);
                    servantItem.setClass_type(class_type);
                    servantItem.setStar(star);
                    servantItem.setArts_hit(arts_hit);
                    servantItem.setBuster_hit(buster_hit);
                    servantItem.setQuick_hit(quick_hit);
                    servantItem.setEx_hit(ex_hit);
                    servantItem.setQuick_na(quick_na);
                    servantItem.setArts_na(arts_na);
                    servantItem.setBuster_na(buster_na);
                    servantItem.setEx_na(ex_na);
                    servantItem.setTrump_na(trump_na);
                    servantItem.setNd(nd);
                    servantItem.setArts_buff(arts_buff);
                    servantItem.setBuster_buff(buster_buff);
                    servantItem.setQuick_buff(quick_buff);
                    servantItem.setAtk_buff(atk_buff);
                    servantItem.setSpecial_buff(special_buff);
                    servantItem.setCritical_buff(critical_buff);
                    servantItem.setSolid_buff(solid_buff);
                    servantItem.setBuster_num(buster_num);
                    servantItem.setArts_num(arts_num);
                    servantItem.setQuick_num(quick_num);
                    servantItem.setStar_occur(star_occur);
                    servantItem.setTrump_lv1(trump_lv1);
                    servantItem.setTrump_lv2(trump_lv2);
                    servantItem.setTrump_lv3(trump_lv3);
                    servantItem.setTrump_lv4(trump_lv4);
                    servantItem.setTrump_lv5(trump_lv5);
                    servantItem.setTrump_color(trump_color);
                    cache.add(servantItem);
                } while (cur.moveToNext());
            }
            database.close();
            return cache;
        } else {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //判断网络是否可用
                    if (isNetworkAvailable(mContext)) {
                        //下载配置文件
                        downloadConfig();
                    }
                } else {
                    ToastUtils.displayShortToast(mContext, "您拒绝了权限");
                }
                break;
        }
    }

    //检查数据库是否需要更新true更新,false不更新
    private boolean checkDatabase() {
        InputStream in = null;
        try {
            in = getResources()
                    .getAssets().open("database.xml");
        } catch (IOException e) {
            throw new SQLiteException("database.xml is not exist");
        }
        XmlPullParserFactory factory;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(in, "UTF-8");
            int evtType = xpp.getEventType();
            String dbName = "";
            // 一直循环，直到文档结束
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();
                        if (tag.equals("dbname")) {
                            dbName = xpp.getAttributeValue(0);
                        } else if (tag.equals("version")) {
                            curDbVersion = Integer.valueOf(xpp.getAttributeValue(0));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                //获得下一个节点的信息
                evtType = xpp.next();
            }
            if (preDbVersion != curDbVersion) {
                SharedPreferencesUtils.setParam(mContext, "dbVersion", curDbVersion);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //下载版本文件
    private void downloadConfig() {
        File configFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/fgocalc_config.xml");
        if (configFile.exists()) {
            configFile.delete();
            Log.d(TAG, "配置文件存在，删除配置文件");
        }
        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(checkUrl));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("download", "/fgocalc_config.xml");
        //获取下载管理器
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
    }

    /**
     * 广播接受器, 下载完成监听器
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //下载完成了
                //获取当前完成任务的ID
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d(TAG, "配置文件下载完成");
                if (checkUpdate()) {
                    showUpdateDiag();
                    Log.d(TAG, "有更新,更新地址->" + downloadUrl);
                }
            }

            if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

            }
        }
    };

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //检查新版本
    private boolean checkUpdate() {
        InputStream in = null;
        File configFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/fgocalc_config.xml");
        Log.d(TAG, "check path->" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/fgocalc_config.xml");
        if (!configFile.exists()) {
            Log.d(TAG, "配置文件不存在");
            return false;
        } else {
            try {
                in = new FileInputStream(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            XmlPullParserFactory factory;
            try {
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(in, "UTF-8");
                int evtType = xpp.getEventType();
                String version = "";
                // 一直循环，直到文档结束
                while (evtType != XmlPullParser.END_DOCUMENT) {
                    switch (evtType) {
                        case XmlPullParser.START_TAG:
                            String tag = xpp.getName();
                            if (tag.equals("version")) {
                                version = xpp.getAttributeValue(0);
                            } else if (tag.equals("path")) {
                                downloadUrl = xpp.getAttributeValue(0);
                            } else if (tag.equals("update")) {
                                update = xpp.getAttributeValue(0);
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        default:
                            break;
                    }
                    //获得下一个节点的信息
                    evtType = xpp.next();
                }
                if (!preVersion.equals(version)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //重载返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
