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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.phantancy.fgocalc.R;

import org.phantancy.fgocalc.common.UrlConstant;
import org.phantancy.fgocalc.database.DBManager;
import org.phantancy.fgocalc.dialog.FeedbackDialog;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item.UpdateItem;
import org.phantancy.fgocalc.util.JsonUtils;
import org.phantancy.fgocalc.util.OKhttpManager;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.util.ToolCase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

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
    private String downloadUrl, checkUrl = "http://git.oschina.net/nj005py/fgocalc/raw/master/fgocalc_config.xml";
    private String databaseExtraUrl = "https://gitee.com/nj005py/fgocalc/raw/master/servants.db";
    private String preVersion;
    private String curVersion;//当前最新app版本号
    private String ignoreVersion;//忽略版本号
    private String update;//更新文本
    private String keyWord;
    private DBManager dbManager;
//    private SQLiteDatabase database;
    private List<ServantItem> servantList = new ArrayList<>();
    private RecyclerView rv;
    private ServantItem sItem,pItem;
    private boolean isReceiverRegister = false;
    private boolean findUpdate = false;
    private boolean isExtra = false;//是否加载外置数据库
    private boolean isUpdateDatabase = false;//是否是更新数据库
    private final int SHOW_SERVANTS = 1;
    private final int SHOW_EXCEPTION = 2;

    @NonNull
    private final ServantListContract.View mView;

    public ServantListPresenter(@NonNull ServantListContract.View mView,Context ctx) {
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
        preVersion = getVersion();
        preDbVersion = (int) SharedPreferencesUtils.getParam(ctx, "dbVersion", 1);
        //判断网络是否可用
        if (isNetworkAvailable(ctx)) {
            /**
             * 终于可以不用下载xml文件检测更新了
             * 因为森林大佬帮忙写了接口
             * 于是下载、监听、解析等操作都可以下班了
             * */
            //注册广播接收器
//            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//            ctx.registerReceiver(receiver, filter);
//            isReceiverRegister = true;
            //下载配置文件
//            downloadConfig();
            checkUpdateByApi();
        }
        //判断数据库有无更新
        if (checkDatabase(ctx)) {
            File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
            dbFile.delete();
//            ToastUtils.displayShortToast(ctx, "数据库更新");
            mView.showCharacter(ctx.getResources().getString(R.string.database_upgrade_done),R.mipmap.altria_alter_a);
        }
        //实装末端servant
        sItem = new ServantItem();
        sItem.setId(999);
        sItem.setName("百度月系吧刊组");//6星天花板
        sItem.setClass_type("Creator");
        sItem.setStar(6);
        pItem = new ServantItem();
        pItem.setId(9999);
        pItem.setName("空谕");
        sItem.setClass_type("Creator");
        sItem.setStar(7);
        dbManager = new DBManager(ctx);
        getAllServants();
    }

    /**
     * 有接口了，这部分下班吧
     * 广播接受器, 下载完成监听器
     */
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
//                //下载完成了
//                //获取当前完成任务的ID
//                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                Log.d(TAG, "配置文件下载完成");
//                if (checkUpdate()) {
//                    //判断当前版本是否是忽略版本
//                    ignoreVersion = (String)SharedPreferencesUtils.getParam(context,"ignoreVersion","1");
//                    Log.d(TAG,"curVersion:" + curVersion + "ignoreVersion:" + ignoreVersion);
//                    if (!ignoreVersion.equals(curVersion)) {
//                        //升级弹框
//                        mView.showUpdateDiag(update,downloadUrl,curVersion);
//                        Log.d(TAG, "有更新,更新地址->" + downloadUrl);
//                    }
//                }
//            }
//
//            if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
//
//            }
//        }
//    };

    //用来监听下载事件的
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Log.d(TAG, "外置数据库下载完成");
                isUpdateDatabase = true;
                loadDatabaseExtra();
            }
        }
    };

    @Override
    public void unregisterReceiver(Context ctx){
        if (receiver != null && isReceiverRegister) {
            ctx.unregisterReceiver(receiver);
        }
    }

    @Override
    public void reloadDatabase() {
        File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
        dbFile.delete();
        mView.showCharacter(ctx.getResources().getString(R.string.database_reload_done),R.mipmap.altria_alter_a);
        getAllServants();
    }

    @Override
    public void loadDatabaseExtra() {
        File dbFile = new File(DBManager.DB_PATH + "/" + DBManager.DB_NAME);
        dbFile.delete();
        if (isUpdateDatabase) {
            mView.showCharacter(ctx.getResources().getString(R.string.database_upgrade_done),R.mipmap.altria_alter_a);
            isUpdateDatabase = false;
        }else {
            mView.showCharacter(ctx.getResources().getString(R.string.database_load_extra_done),R.mipmap.altria_alter_a);
        }
        isExtra = true;
        getAllServants();
    }

    @Override
    public void downloadDatabaseExtra() {
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
    }

    //检测app版本更新，目前使用，现通过api
    private void checkUpdateByApi(){
        OKhttpManager.getAsync(UrlConstant.CHECK_UPDATE, new OKhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Call call, IOException e) {

            }

            @Override
            public void requestSuccess(String result) {
                try {
                    UpdateItem item = JsonUtils.getUpdateItem(result);
                    if (item != null) {
                        curVersion = item.getVersion();
                        update = item.getUpdate();
                        downloadUrl = item.getPath();
                        if (!TextUtils.isEmpty(curVersion) && !preVersion.equals(curVersion)) {
                            //判断当前版本是否是忽略版本
                            ignoreVersion = (String)SharedPreferencesUtils.getParam(ctx,"ignoreVersion","1");
                            Log.d(TAG,"curVersion:" + curVersion + "ignoreVersion:" + ignoreVersion);
                            if (!ignoreVersion.equals(curVersion)) {
                                boolean showUpdate = false;
                                //判断版本号大小
                                String curVer[] = curVersion.split("\\.");
                                String preVer[] = preVersion.split("\\.");
                                //版本号分4段比较，第4段表示状态
                                if (curVer[0].compareTo(preVer[0]) == 1) {
                                    showUpdate = true;
                                }else{
                                    if (Integer.valueOf(curVer[1]) > Integer.valueOf(preVer[1])) {
                                        showUpdate = true;
                                    }else{
                                        if (Integer.valueOf(curVer[2]) > Integer.valueOf(preVer[2])) {
                                            showUpdate = true;
                                        }
                                    }
                                }
                                if (curVer.length == 4) {
                                    //前3个数字编号无法看出谁更大时，比较英文版本号
                                    /**
                                     *base 基础版
                                     * alpha 内测版
                                     * beta 公测版
                                     * explorer 探索版，不一定有
                                     * rc 基本成型版
                                     * release 正式版
                                     */
                                    //无第4版本号直接提示更新
                                    if(preVer.length < 4){
                                        showUpdate = true;
                                    }else{
                                        if (ToolCase.get4thVersion(curVer[3]) > ToolCase.get4thVersion(preVer[3])) {
                                            showUpdate = true;
                                        }
                                    }
                                }
                                if (showUpdate) {
                                    //升级弹框
                                    mView.showUpdateDiag(update,downloadUrl,curVersion);
                                    Log.d(TAG, "有更新,更新地址->" + downloadUrl);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //检查app版本更新，弃用，原解析xml方法
    public boolean checkUpdate() {
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
                    curVersion = version;
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

    //网络是否可用
    public boolean isNetworkAvailable(Context context) {
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

    //下载xml配置文件，弃用
    public void downloadConfig() {
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
        DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(request);
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

    @Override
    public void setMethod(int method) {

    }

    @Override
    public void getAllServants() {
        try{
            //算是被坑怕了
            if (dbManager == null) {
                dbManager = new DBManager(ctx);
            }
            //打开数据库
            if (isExtra) {
                dbManager.getDatabaseExtra();
                isExtra = false;
            }else{
                dbManager.getDatabase();
            }
            Cursor cur;
            cur = dbManager.database.rawQuery("SELECT * FROM servants", null);
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
                pItem.setId(9999);
                pItem.setName("空谕");
                sItem.setClass_type("Creator");
                sItem.setStar(7);
            }
            servantList.add(sItem);
            servantList.add(pItem);
            if (cur != null) {
                cur.close();
            }
            dbManager.closeDatabase();
            mView.setServantList(servantList);
        }catch (SecurityException e){
            mView.showCharacter(ctx.getString(R.string.permission_error),R.mipmap.altria_alter_b);
        }catch (Exception e){
            e.printStackTrace();
            mView.showCharacter(ctx.getString(R.string.database_error),R.mipmap.altria_alter_b);
        }
    }

    @Override
    public void searchServantsByKeyword(final String value) {
        try{
            if (notEmpty(value)) {
                dbManager.getDatabase();
                Cursor cur;
                keyWord = ToolCase.tc2sc(value);
                cur = dbManager.database.rawQuery("SELECT * FROM servants WHERE name LIKE ? OR nickname LIKE ? ORDER BY CAST(id AS SIGNED) ASC",
                        new String[]{"%" + value + "%", "%" + value + "%"});
                servantList = getServants(cur);
                if (cur != null) {
                    cur.close();
                }
                dbManager.closeDatabase();
                mView.setServantList(servantList);
            }else{
                servantList = null;
                mView.setServantList(servantList);
            }
        }catch (Exception e){
            e.printStackTrace();
            mView.showCharacter(ctx.getString(R.string.database_error),R.mipmap.altria_alter_b);
        }
    }

    @Override
    public void searchServantsByCondition(final String classType,final int star,final String orderType) {
        try{
            if (notEmpty(classType)) {
                dbManager.getDatabase();
                Cursor cur;
                String[] order = orderType.split(",");
                boolean ifAllClass = false;//是否职阶不限
                boolean ifAllStar = false;//是否星数不限
                boolean ifMultiplier = false;//是否需要计算系数
                //判断职阶
                if (classType.equals("All")) {
                    ifAllClass = true;
                }
                //判断星数
                if (star == 0) {
                    ifAllStar = true;
                }
                //判断排序方式
                if (order.length == 3) {
                    ifMultiplier = true;
                }
                //判断使用哪种sql语句
                if (ifAllClass && ifAllStar && ifMultiplier) {
                    cur = dbManager.database.rawQuery("SELECT a.*,(a.default_atk * b.multiplier) new_atk " +
                                    " FROM servants a" +
                                    " LEFT JOIN class b ON a.class_type = b.class_type" +
                                    " ORDER BY CAST( new_atk AS SIGNED)" + order[1],
                            null);
                }else if (ifAllClass && ifMultiplier) {
                    cur = dbManager.database.rawQuery("SELECT a.*,(a.default_atk * b.multiplier) new_atk " +
                                    " FROM servants a" +
                                    " LEFT JOIN class b ON a.class_type = b.class_type" +
                                    " WHERE a.star = ?" +
                                    " ORDER BY CAST( new_atk AS SIGNED)" + order[1],
                            new String[]{star + ""});
                }else if (ifAllStar && ifMultiplier) {
                    cur = dbManager.database.rawQuery("SELECT a.*,(a.default_atk * b.multiplier) new_atk " +
                                    " FROM servants a" +
                                    " LEFT JOIN class b ON a.class_type = b.class_type" +
                                    " WHERE a.class_type = ?" +
                                    " ORDER BY CAST( new_atk AS SIGNED)" + order[1],
                            new String[]{classType});
                }else if (ifAllClass && ifAllStar) {
                    cur = dbManager.database.rawQuery("SELECT * FROM servants ORDER BY CAST(" + order[0] +" AS SIGNED)" + order[1],
                            null);
                }else if (ifMultiplier) {
                    cur = dbManager.database.rawQuery("SELECT a.*,(a.default_atk * b.multiplier) new_atk " +
                                    " FROM servants a" +
                                    " LEFT JOIN class b ON a.class_type = b.class_type" +
                                    " WHERE a.class_type = ? AND star = ?" +
                                    " ORDER BY CAST( new_atk AS SIGNED)" + order[1],
                            new String[]{classType,star + ""});
                }else if (ifAllStar) {
                    cur = dbManager.database.rawQuery("SELECT * FROM servants WHERE class_type = ? ORDER BY CAST(" + order[0] +" AS SIGNED)" + order[1],
                            new String[]{classType});
                }else if (ifAllClass) {
                    cur = dbManager.database.rawQuery("SELECT * FROM servants WHERE star = ? ORDER BY CAST(" + order[0] +" AS SIGNED)" + order[1],
                            new String[]{star + ""});
                }else{
                    cur = dbManager.database.rawQuery("SELECT * FROM servants WHERE class_type = ? AND star = ? ORDER BY CAST(" + order[0] + " AS SIGNED)" + order[1],
                            new String[]{classType, star + ""});
                }
                servantList = getServants(cur);
                if (cur != null) {
                    cur.close();
                }
                dbManager.closeDatabase();
                mView.setServantList(servantList);
            }else{
                servantList = null;
                mView.setServantList(servantList);
            }
        }catch (Exception e){
            e.printStackTrace();
            mView.showCharacter(ctx.getString(R.string.database_error),R.mipmap.altria_alter_b);
        }
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

    public ArrayList<ServantItem> getServants(Cursor cur) {
        if (cur != null) {
            int NUM_SERVANT = cur.getCount();
            ArrayList<ServantItem> cache = new ArrayList<>();
            while (cur.moveToNext()){
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
                cache.add(servantItem);
            }
            return cache;
        } else {
            return null;
        }
    }


}
