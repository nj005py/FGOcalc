package org.phantancy.fgocalc.servant;

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
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.phantancy.fgocalc.R;

import org.phantancy.fgocalc.database.DBManager;
import org.phantancy.fgocalc.dialog.FeedbackDialog;
import org.phantancy.fgocalc.dialog.LoadingDialog;
import org.phantancy.fgocalc.item.FilterItem;
import org.phantancy.fgocalc.item.OptionItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item.TipItem;
import org.phantancy.fgocalc.item.UpdateItem;
import org.phantancy.fgocalc.util.BaseUtils;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToolCase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.phantancy.fgocalc.util.ToolCase.notEmpty;

/**
 * Created by HATTER on 2017/10/24.
 */

public class ServantListPresenter implements ServantListContract.Presenter {
    private Context ctx;
    private Activity acty;
    private final String TAG = getClass().getSimpleName();
    private int curDbVersion;
    private int preDbVersion;
    private String databaseExtraUrl = "https://gitee.com/nj005py/fgocalc/raw/master/servants.db";
    private int locVersion;
    private int netVersion;//当前最新app版本号
    private int ignVersion;//忽略版本号
    private String update;//更新文本
    private String keyWord;
    //    private DBManager dbManager;
    private List<ServantItem> servantList = new ArrayList<>();
    private ServantItem sItem;//吧刊组
    private ServantItem pItem;//空谕
    private ServantItem sliverItem;//遗忘的银灵
    private ServantItem blueItem;//纯蓝魔法使
    private boolean isReceiverRegister = false;
    private boolean isExtra = false;//是否加载外置数据库
    private boolean isReload = false;//是否重载数据库
    private boolean isManual = false;//是否手动检测更新
    private LoadingDialog loadingDialog;

    private int DB_ERROR = 0;//0 ok,1 permission,2 error

    private final int CHECK_APP_UPDATE = 1;//检查更新完毕

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CHECK_APP_UPDATE:
                    if ((msg.obj != null) && (msg.obj instanceof  UpdateItem)) {
                        UpdateItem updateItem = (UpdateItem)msg.obj;
                        netVersion = updateItem.getVersionCode();
                        if (netVersion != 0 && locVersion != 0) {
                            //判断当前版本是否是忽略版本
                            ignVersion = (int) SharedPreferencesUtils.getParam(ctx, "ignVersion", 0);
                            if (ignVersion != netVersion) {
                                //比较版本号
                                if (netVersion > locVersion) {
                                    //升级弹框
                                    mView.showUpdateDiag(updateItem);
                                } else {
                                    if (isManual) {
                                        isManual = false;
                                        TipItem tItem = new TipItem();
                                        tItem.setHasTip(true);
                                        tItem.setImgId(R.drawable.altria_alter_a);
                                        tItem.setTip("App版本已是最新，无需更新！");
                                        ToolCase.showTip(ctx, tItem);
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    });

    @NonNull
    private final ServantListContract.View mView;

    public ServantListPresenter(@NonNull ServantListContract.View mView, Context ctx) {
        this.ctx = ctx;
        this.mView = mView;
        mView.setPresenter(this);
    }

    public ServantListPresenter(Context ctx, Activity acty, @NonNull ServantListContract.View mView) {
        this.ctx = ctx;
        this.acty = acty;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void simpleCheck(Context ctx, Activity acty) {
        this.ctx = ctx;
        this.acty = acty;
        preDbVersion = (int) SharedPreferencesUtils.getParam(ctx, "dbVersion", 1);
        checkAppUpdate(false);//是否人工检测更新？否
        //判断本地数据库有无更新
        if (checkDatabase(ctx)) {
            File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
            dbFile.delete();
            TipItem tItem = new TipItem();
            tItem.setHasTip(true);
            tItem.setHasOption(false);
            tItem.setImgId(R.drawable.altria_alter_a);
            tItem.setTip(ctx.getResources().getString(R.string.database_upgrade_done));
            ToolCase.showTip(ctx, tItem);
        }
        //实装末端servant
        //吧刊组
        sItem = new ServantItem();
        sItem.setId(999);
        sItem.setName("百度月系吧刊组");//6星天花板
        sItem.setClass_type("Creator");
        sItem.setStar(6);
        //空谕
        pItem = new ServantItem();
        pItem.setId(1000);
        pItem.setName("空谕");
        pItem.setClass_type("Creator");
        pItem.setStar(6);
        //遗忘的银灵
        sliverItem = new ServantItem();
        sliverItem.setId(1001);
        sliverItem.setName("遗忘的银灵");
        sliverItem.setClass_type("Creator");
        sliverItem.setStar(6);
        //纯蓝魔法使
        blueItem = new ServantItem();
        blueItem.setId(1005);
        blueItem.setName("纯蓝魔法使");
        blueItem.setClass_type("Creator");
        blueItem.setStar(6);
//        dbManager = DBManager.getInstance();
        getAllServants();
    }

    //用来监听下载事件的
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                loadDatabaseExtra();
            }
        }
    };

    @Override
    public void unregisterReceiver(Context ctx) {
        if (receiver != null && isReceiverRegister) {
            ctx.unregisterReceiver(receiver);
        }
    }

    @Override
    public void reloadDatabase() {
        File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        isReload = true;
        getAllServants();
    }

    @Override
    public void loadDatabaseExtra() {
        File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        isExtra = true;
        getAllServants();
    }

    @Override
    public void downloadDatabaseExtra() {
        //判断本地与远端数据库版本号
        int locVer = (int) SharedPreferencesUtils.getParam(ctx, "dbVersion", 0);
        String remote = (String) SharedPreferencesUtils.getParam(ctx, "remoteDb", "");
        if (locVer != 0 && !TextUtils.isEmpty(remote)) {
            int remoteVer = Integer.valueOf(remote);
            if (remoteVer > locVer) {
                File configFile = new File(Environment.getExternalStoragePublicDirectory("Download") + "/servants.db");
                if (configFile.exists()) {
                    configFile.delete();
                    Log.d(TAG, "数据库文件存在，删除配置文件");
                }
                //注册广播接收器
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                ctx.registerReceiver(receiver, filter);
                isReceiverRegister = true;
                //创建下载任务,downloadUrl就是下载链接
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(databaseExtraUrl));
                //指定下载路径和下载文件名
                request.setDestinationInExternalPublicDir("Download", "/servants.db");
                //获取下载管理器
                DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                //将下载任务加入下载队列，否则不会进行下载
                downloadManager.enqueue(request);
            } else {
                TipItem tItem = new TipItem();
                tItem.setHasTip(true);
                tItem.setTip("数据库版本已是最新，无需更新！");
                tItem.setImgId(R.drawable.altria_alter_a);
                ToolCase.showTip(ctx, tItem);
            }
        } else {
            TipItem tItem = new TipItem();
            tItem.setHasTip(true);
            tItem.setTip("数据库版本已是最新，无需更新！");
            tItem.setImgId(R.drawable.altria_alter_a);
            ToolCase.showTip(ctx, tItem);
        }
    }

    //检查数据库版本
    public boolean checkDatabase(Context ctx) {
        InputStream in = null;
        try {
            in = ctx.getResources()
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
                SharedPreferencesUtils.setParam(ctx, "dbVersion", curDbVersion);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return false;
    }

    //获取app的版本号
    @Override
    public String getVersion() {
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private int getVersionCode(){
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(),0);
            int code = info.versionCode;
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void checkAppUpdate(boolean isManual) {
        this.isManual = isManual;
        locVersion = getVersionCode();//获取本地版本号
        //判断网络是否可用
        if (BaseUtils.isNetworkAvailable(ctx)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String checkUrl = "http://nj005py.gitee.io/fgocalc/version";
                    UpdateItem item = null;
                    try {
                        item = new UpdateItem();
                        //从一个URL加载一个Document对象。
                        Document doc = Jsoup.connect(checkUrl).get();
                        Element eVer = doc.getElementById("version");
                        Element eVerCode = doc.getElementById("version_code");
                        Element eUrl = doc.getElementById("url");
                        Element eCon = doc.getElementById("content");
                        Element eDb = doc.getElementById("db");
                        item.setVersion(eVer.text());
                        item.setVersionCode(Integer.valueOf(eVerCode.text()));
                        item.setUrl(eUrl.text());
                        item.setContent(eCon.text());
                        item.setCheckUrl(checkUrl);
                        SharedPreferencesUtils.setParam(ctx, "remoteDb", eDb.text());
                        Message msg = new Message();
                        msg.what = CHECK_APP_UPDATE;
                        msg.obj = item;
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public List<FilterItem> getFilterItems() {
        String[] classType = ctx.getResources().getStringArray(R.array.classType);
        String[] starNum = ctx.getResources().getStringArray(R.array.star);
        int[] starValue = ctx.getResources().getIntArray(R.array.star_value);
        String[] orderType = ctx.getResources().getStringArray(R.array.order_type);
        String[] orderTypeValue = ctx.getResources().getStringArray(R.array.order_type_value);
        String[] attribute = ctx.getResources().getStringArray(R.array.attribute);
        int[] attributeValue = ctx.getResources().getIntArray(R.array.attribute_value);
        String[] traits = ctx.getResources().getStringArray(R.array.traits);
        String[] npClassification = ctx.getResources().getStringArray(R.array.np_classification);
        String[] npClassificationValue = ctx.getResources().getStringArray(R.array.np_classification_value);
        String[] npColor = ctx.getResources().getStringArray(R.array.np_color);
        String[] npColorValue = ctx.getResources().getStringArray(R.array.np_color_value);
        List<FilterItem> list = new ArrayList<>();
        list.add(new FilterItem("职阶",classType,classType));
        list.add(new FilterItem("星数",starNum,starValue,1));
        list.add(new FilterItem("阵营",attribute,attributeValue,1));
        list.add(new FilterItem("排序",orderType,orderTypeValue));
        list.add(new FilterItem("特性",traits,traits));
        list.add(new FilterItem("宝具卡色",npColor,npColorValue));
        list.add(new FilterItem("宝具类型",npClassification,npClassificationValue));
        return list;
    }

    @Override
    public void getAllServants() {
        AllServantsTask allServantsTask = new AllServantsTask();
        allServantsTask.execute();
    }

    @Override
    public void searchServantsByKeyword(String value) {
        KeywordTask keywordTask = new KeywordTask(value);
        keywordTask.execute();
    }

    @Override
    public void searchServantsByCondition(List<FilterItem> filterItems) {
        ConditionTask conditionTask = new ConditionTask(filterItems);
        conditionTask.execute();
    }

    @Override
    public void checkMenuLoc(boolean locLeft) {

    }

    @Override
    public void fgotool() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("https://fgotool.com");
        intent.setData(content_url);
        ctx.startActivity(intent);
    }

    @Override
    public void fgosimulator() {
        Intent i = new Intent();
        i.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse("http://fgosimulator.webcrow.jp/Material");
        i.setData(uri);
        ctx.startActivity(i);
    }

    @Override
    public void feedback() {
        FeedbackDialog d = new FeedbackDialog(ctx);
        Window dialogWindow = d.getWindow();
        WindowManager m = acty.getWindowManager();
        Display dm = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (dm.getHeight() * 0.8); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (dm.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        d.show();
    }

    @Override
    public void sendEmail(Context ctx) {
        Intent i = new Intent(Intent.ACTION_SENDTO);
//        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:phantancy@hotmail.com"));// only email apps should handle this
//        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"phantancy@hotmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "fgo计算器反馈");
        i.putExtra(Intent.EXTRA_TEXT, "我想反馈");
        try {
            ctx.startActivity(Intent.createChooser(i, "邮件反馈"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(ctx, "没找到Email相关应用.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void follow() {
        TipItem tItem = new TipItem();
        List<OptionItem> list = new ArrayList<>();
        tItem.setHasTip(true);
        tItem.setHasOption(true);
        tItem.setImgId(R.drawable.altria_a);
        tItem.setTip("FGOcalc Android版、Web版的作者。求关注、硬币、收藏……");
        list.clear();
        list.add(new OptionItem("去Bilibili关注TA","https://space.bilibili.com/532252/#/"));
        tItem.setOptionList(list);
        ToolCase.showTip(ctx,tItem);
    }

    @Override
    public void qq() {
        ToolCase.copy2ClipboardCharacter(ctx,"422969398","QQ群号已复制剪切板，快去加群吧！");
    }

    public ArrayList<ServantItem> getServants(Cursor cur) {
        if (cur != null) {
            int NUM_SERVANT = cur.getCount();
            ArrayList<ServantItem> cache = new ArrayList<>();
            while (cur.moveToNext()) {
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
                int default_hp = cur.getInt(32);
                int default_atk = cur.getInt(33);
                double star_occur_extra = cur.getDouble(34);
                double trump_lv1_before = cur.getDouble(35);
                double trump_lv2_before = cur.getDouble(36);
                double trump_lv3_before = cur.getDouble(37);
                double trump_lv4_before = cur.getDouble(38);
                double trump_lv5_before = cur.getDouble(39);
                int trump_upgraded = cur.getInt(40);
                int attribute = cur.getInt(41);
                int np_hit = cur.getInt(42);
                String pic = cur.getString(43);
                String traits = cur.getString(44);
                String alignments = cur.getString(45);
                String np_classification = cur.getString(46);//宝具分类
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
                servantItem.setDefault_hp(default_hp);
                servantItem.setDefault_atk(default_atk);
                servantItem.setStar_occur_extra(star_occur_extra);
                servantItem.setTrump_lv1_before(trump_lv1_before);
                servantItem.setTrump_lv2_before(trump_lv2_before);
                servantItem.setTrump_lv3_before(trump_lv3_before);
                servantItem.setTrump_lv4_before(trump_lv4_before);
                servantItem.setTrump_lv5_before(trump_lv5_before);
                servantItem.setTrump_upgraded(trump_upgraded);
                servantItem.setAttribute(attribute);
                servantItem.setNp_hit(np_hit);
                servantItem.setPic(pic);
                servantItem.setTraits(traits);
                servantItem.setAlignments(alignments);
                servantItem.setNp_classification(np_classification);
                cache.add(servantItem);
            }
            return cache;
        } else {
            return null;
        }
    }

    private class AllServantsTask extends AsyncTask<Void, Void, List<ServantItem>> {

        public AllServantsTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(ctx);
            }
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(List<ServantItem> servantItems) {
            if (loadingDialog != null) {
                loadingDialog.stopAnim();
            }
            if (DB_ERROR == 0) {
                if (isExtra) {
                    TipItem tItem = new TipItem();
                    tItem.setHasTip(true);
                    tItem.setHasOption(false);
                    tItem.setImgId(R.drawable.altria_alter_a);
                    tItem.setTip(ctx.getResources().getString(R.string.database_upgrade_done));
                    ToolCase.showTip(ctx, tItem);
                }
                if (isReload) {
                    TipItem tItem = new TipItem();
                    tItem.setHasTip(true);
                    tItem.setHasOption(false);
                    tItem.setImgId(R.drawable.altria_alter_a);
                    tItem.setTip(ctx.getResources().getString(R.string.database_reload_done));
                    ToolCase.showTip(ctx, tItem);
                }
            }else{
                TipItem tItem = new TipItem();
                servantItems = null;
                if (DB_ERROR == 1) {
                    tItem.setHasTip(true);
                    tItem.setHasOption(false);
                    tItem.setImgId(R.drawable.altria_alter_b);
                    tItem.setTip(ctx.getString(R.string.permission_error));
                    ToolCase.showTip(ctx, tItem);
                }
                if (DB_ERROR == 2) {
                    List<OptionItem> list = new ArrayList<>();
                    list.add(new OptionItem("还能用", null));
                    list.add(new OptionItem("我自己去重载数据库", null));
                    list.add(new OptionItem("Saber，帮我重载数据库T_T", "reload_db"));
                    tItem.setHasTip(true);
                    tItem.setHasOption(true);
                    tItem.setImgId(R.drawable.altria_alter_b);
                    tItem.setTip(ctx.getString(R.string.database_error));
                    tItem.setOptionList(list);
                    ToolCase.showTip(ctx, tItem);
                }
                DB_ERROR = 0;
            }
            isExtra = false;
            isReload = false;
            mView.setServantList(servantItems);
            super.onPostExecute(servantItems);
        }

        @Override
        protected List<ServantItem> doInBackground(Void... voids) {
            Cursor cur = null;
            try {
                //打开数据库
                if (isExtra) {
                    DBManager.getInstance().getDatabaseExtra();
                } else {
                    DBManager.getInstance().getDatabase();
                }
                cur = DBManager.getInstance().database.rawQuery("SELECT * FROM servants", null);
                servantList = getServants(cur);
                if (sItem == null) {
                    //实装末端servant
                    sItem = new ServantItem();
                    sItem.setId(999);
                    sItem.setName("百度月系吧刊组");//6星天花板
                    sItem.setClass_type("Creator");
                    sItem.setStar(6);
                }
                if (pItem == null) {
                    pItem = new ServantItem();
                    pItem.setId(1000);
                    pItem.setName("空谕");
                    sItem.setClass_type("Creator");
                    sItem.setStar(6);
                }
                //遗忘的银灵
                if (sliverItem == null) {
                    sliverItem = new ServantItem();
                    sliverItem.setId(1001);
                    sliverItem.setName("遗忘的银灵");
                    sliverItem.setClass_type("Creator");
                    sliverItem.setStar(6);
                }
                //纯蓝魔法使
                if (blueItem == null) {
                    blueItem = new ServantItem();
                    blueItem.setId(1005);
                    blueItem.setName("纯蓝魔法使");
                    blueItem.setClass_type("Creator");
                    blueItem.setStar(6);
                }
                servantList.add(sItem);
                servantList.add(pItem);
                servantList.add(sliverItem);
                servantList.add(blueItem);
            } catch (SecurityException e) {
                DB_ERROR = 1;
            } catch (Exception e) {
                DB_ERROR = 2;
            } finally {
                if (cur != null) {
                    cur.close();
                }
                DBManager.getInstance().closeDatabase();
            }
            return servantList;
        }
    }

    private class KeywordTask extends AsyncTask<Void, Void, List<ServantItem>> {
        private String value;

        public KeywordTask(String value) {
            this.value = value;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(ctx);
            }
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(List<ServantItem> servantItems) {
            if (loadingDialog != null) {
                loadingDialog.stopAnim();
            }
            if (DB_ERROR == 2) {
                TipItem tItem = new TipItem();
                List<OptionItem> list = new ArrayList<>();
                list.add(new OptionItem("还能用", null));
                list.add(new OptionItem("我自己去重载数据库", null));
                list.add(new OptionItem("Saber，帮我重载数据库T_T", "reload_db"));
                tItem.setHasTip(true);
                tItem.setHasOption(true);
                tItem.setImgId(R.drawable.altria_alter_b);
                tItem.setTip(ctx.getString(R.string.database_error));
                ToolCase.showTip(ctx, tItem);
                DB_ERROR = 0;
            }
            mView.setServantList(servantItems);
            super.onPostExecute(servantItems);
        }

        @Override
        protected List<ServantItem> doInBackground(Void... voids) {
            Cursor cur = null;
            try {
                if (notEmpty(value)) {
                    DBManager.getInstance().getDatabase();
                    keyWord = ToolCase.tc2sc(value);
                    cur = DBManager.getInstance().database.rawQuery("SELECT * FROM servants WHERE name LIKE ? OR nickname LIKE ? ORDER BY CAST(id AS SIGNED) ASC",
                            new String[]{"%" + value + "%", "%" + value + "%"});
                    servantList = getServants(cur);
                } else {
                    servantList = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                DB_ERROR = 2;
            } finally {
                if (cur != null) {
                    cur.close();
                }
                DBManager.getInstance().closeDatabase();
            }
            return servantList;
        }
    }

    private class ConditionTask extends AsyncTask<Void, Void, List<ServantItem>> {
        private String classType;//职阶
        private int star;//星数
        private String orderType;//排序
        private int attributeValue;//阵营
        private String traits;//特性
        private String alignments;//属性
        private String npColor;//宝具卡色
        private String npClassification;//宝具类型

        public ConditionTask(List<FilterItem> filterItems) {
            /**
             * 职阶0
             * 星数1
             * 阵营2
             * 排序3
             * 特性4
             * 宝具卡色5
             * 宝具类型6
             * */
            this.classType = filterItems.get(0).getValueString();
            this.star = filterItems.get(1).getValueInt();
            this.attributeValue = filterItems.get(2).getValueInt();
            this.orderType = filterItems.get(3).getValueString();
            this.traits = filterItems.get(4).getValueString();
            this.npColor = filterItems.get(5).getValueString();
            this.npClassification = filterItems.get(6).getValueString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(ctx);
            }
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(List<ServantItem> servantItems) {
            if (loadingDialog != null) {
                loadingDialog.stopAnim();
            }
            if (DB_ERROR == 2) {
                TipItem tItem = new TipItem();
                List<OptionItem> list = new ArrayList<>();
                list.add(new OptionItem("还能用", null));
                list.add(new OptionItem("我自己去重载数据库", null));
                list.add(new OptionItem("Saber，帮我重载数据库T_T", "reload_db"));
                tItem.setHasTip(true);
                tItem.setHasOption(true);
                tItem.setImgId(R.drawable.altria_alter_b);
                tItem.setTip(ctx.getString(R.string.database_error));
                ToolCase.showTip(ctx, tItem);
                DB_ERROR = 0;
            }
            mView.setServantList(servantItems);
            super.onPostExecute(servantItems);
        }

        @Override
        protected List<ServantItem> doInBackground(Void... voids) {
            Cursor cur = null;
            try {
                if (notEmpty(classType)) {
                    DBManager.getInstance().getDatabase();
                    String[] order = orderType.split(",");
                    boolean ifAllClass = false;//是否职阶不限
                    boolean ifAllStar = false;//是否星数不限
                    boolean ifAllAttribute = false;//是否阵营不限
                    boolean ifMultiplier = false;//是否需要计算系数
                    boolean ifAllTraits = false;//是否特性不限
                    boolean ifAllNpColor = false;//是否宝具卡色不限
                    boolean ifAllNpClassification = false;//是否宝具类型不限
                    //判断职阶
                    if (classType.equals("All")) {
                        ifAllClass = true;
                    }
                    //判断星数
                    if (star == 0) {
                        ifAllStar = true;
                    }
                    //判断属性
                    if (attributeValue == -1) {
                        ifAllAttribute = true;
                    }
                    //判断特性
                    if (traits.equals("不限")) {
                        ifAllTraits = true;
                    }
                    //判断排序方式
                    if (order.length == 3) {
                        ifMultiplier = true;
                    }
                    //判断宝具卡色
                    if (npColor.equals("none")) {
                        ifAllNpColor = true;
                    }
                    //判断宝具类型
                    if (npClassification.equals("none")) {
                        ifAllNpClassification = true;
                    }
                    //默认sql开篇
                    String sql = "";
                    String sqlValue[];
                    List<String> listValue = new ArrayList<>();
                    if (ifMultiplier) {
                        sql = "SELECT a.*,(a.default_atk * b.multiplier) new_atk " +
                                " FROM servants a" +
                                " LEFT JOIN class b ON a.class_type = b.class_type" +
                                " WHERE 1 = 1";
                    } else {
                        sql = "SELECT * " +
                                "FROM servants " +
                                "WHERE 1 = 1";
                    }
                    //职阶
                    if (!ifAllClass) {
                        if (ifMultiplier) {
                            sql += " AND a.class_type = ?";
                        } else {
                            sql += " AND class_type = ?";
                        }
                        listValue.add(classType);
                    }
                    //星数
                    if (!ifAllStar) {
                        if (ifMultiplier) {
                            sql += " AND a.star = ?";
                        } else {
                            sql += " AND star = ?";
                        }
                        listValue.add(star + "");
                    }
                    //阵营
                    if (!ifAllAttribute) {
                        if (ifMultiplier) {
                            sql += " AND a.attribute = ?";
                        } else {
                            sql += " AND attribute = ?";
                        }
                        listValue.add(attributeValue + "");
                    }
                    //特性
                    if (!ifAllTraits) {
                        if (ifMultiplier) {
                            sql += " AND a.traits LIKE ?";
                        } else {
                            sql += " AND traits LIKE ?";
                        }
                        listValue.add("%" + traits + "%");
                    }
                    //宝具卡色
                    if (!ifAllNpColor) {
                        if (ifMultiplier) {
                            sql += " AND a.np_color = ?";
                        } else {
                            sql += " AND np_color = ?";
                        }
                        listValue.add(npColor + "");
                    }
                    //宝具类型
                    if (!ifAllNpClassification) {
                        if (ifMultiplier) {
                            sql += " AND a.np_classification = ?";
                        } else {
                            sql += " AND np_classification = ?";
                        }
                        listValue.add(npClassification + "");
                    }
                    //条件值处理
                    if (listValue.size() == 0) {
                        sqlValue = null;
                    } else {
                        sqlValue = new String[listValue.size()];
                        for (int i = 0; i < listValue.size(); i++) {
                            sqlValue[i] = listValue.get(i);
                        }
                    }
                    //收官排序
                    if (ifMultiplier) {
                        sql += " ORDER BY CAST( new_atk AS SIGNED)" + order[1];
                    } else {
                        sql += " ORDER BY CAST(" + order[0] + " AS SIGNED)" + order[1];
                    }
                    //使用sql
                    cur = DBManager.getInstance().database.rawQuery(sql, sqlValue);
                    servantList = getServants(cur);
                } else {
                    servantList = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                DB_ERROR = 2;
            } finally {
                if (cur != null) {
                    cur.close();
                }
                DBManager.getInstance().closeDatabase();
            }
            return servantList;
        }
    }
}
