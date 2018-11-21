package org.phantancy.fgocalc.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.UpdateItem;

/**
 * Created by PY on 2017/7/19.
 */
public class BaseUtils {
    /**
     * 用于获取状态栏的高度。 使用Resource对象获取
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId=0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }

    //是否有sim卡
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d("SIM", result + "");
        return result;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isGPSOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 判断网络连接是否已开
     * true 已打开  false 未打开
     */
    public static boolean isConn(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //网络是否可用
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

    //通过html获取版本号
    public static void getVersionByHtml(final Handler handler){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final String checkUrl = "http://nj005py.gitee.io/fgocalc/version";
                        try {
                            UpdateItem item = new UpdateItem();
                            //从一个URL加载一个Document对象。
                            Document doc = Jsoup.connect(checkUrl).get();
                            Elements version = doc.select("li.version");
                            Elements url = doc.select("li.url");
                            Elements content = doc.select("li.content");
                            item.setVersion(version.text());
                            item.setUrl(url.text());
                            item.setContent(content.text());
                            item.setCheckUrl(checkUrl);
                            Message msg = new Message();
                            msg.obj = item;
                            msg.what = Constant.CHECK_APP_VERSION;
                            handler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    //通过卡色获取html样式的卡色String
    public static String getCardTypeWithColour(String cardType){
        String vaule = "";
        switch (cardType) {
            case "b":
                vaule = new StringBuilder().append("<font color='#AF0301'>b卡</font>在").toString();
                break;
            case "a":
                vaule = new StringBuilder().append("<font color='#3F51B5'>a卡</font>在").toString();
                break;
            case "q":
                vaule = new StringBuilder().append("<font color='#16580B'>q卡</font>在").toString();
                break;
            case "ex":
                vaule = new StringBuilder().append("<font color='black'>ex卡</font>在").toString();
                break;
            case "np":
                vaule = new StringBuilder().append("<font color='#E4B222'>宝具卡</font>在").toString();
                break;
        }
        return vaule;
    }
}
