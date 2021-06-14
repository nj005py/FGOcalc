package org.phantancy.fgocalc.common;

import android.app.Application;
import android.content.Context;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
/**
 * Created by HATTER on 2017/11/2.
 */

public class App extends Application {

    static Context appCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        appCtx = getApplicationContext();
        LiveEventBus.config()
                .autoClear(true)
                .lifecycleObserverAlwaysActive(true);
        //友盟统计 预初始化
        UMConfigure.preInit(appCtx,"5a61306b8f4a9d420400090e","Umeng");
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
