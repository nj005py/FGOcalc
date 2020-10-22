package org.phantancy.fgocalc.common;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.util.ToolCase;

import java.io.File;

/**
 * Created by HATTER on 2017/11/2.
 */

public class App extends Application {

    static Context appCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        appCtx = getApplicationContext();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //友盟统计
                //初始化sdk
                UMConfigure.init(appCtx, "5a61306b8f4a9d420400090e", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "635f1a6fb0fe4b7fff7e18bfe4af9a77");
                // 选用AUTO页面采集模式
//                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
                PushAgent mPushAgent = PushAgent.getInstance(appCtx);
                //注册推送服务，每次调用register方法都会回调该接口
                mPushAgent.register(new IUmengRegisterCallback() {

                    @Override
                    public void onSuccess(String deviceToken) {
                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });
            }
        });
        thread.start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public static Context getAppContext(){
        return appCtx;
    }

}
