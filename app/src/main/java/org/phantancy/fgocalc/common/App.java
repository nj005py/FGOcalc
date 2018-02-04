package org.phantancy.fgocalc.common;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
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

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        //友盟统计
        UMConfigure.init(this, "5a61306b8f4a9d420400090e", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "635f1a6fb0fe4b7fff7e18bfe4af9a77");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
//                Log.d("my token",deviceToken);
//                //获取剪贴板管理器：
//                ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                // 创建普通字符型ClipData
//                ClipData mClipData = ClipData.newPlainText("fgocalc_token", deviceToken);
//                // 将ClipData内容放到系统剪贴板里。
//                cm.setPrimaryClip(mClipData);
//                SharedPreferencesUtils.setParam(getApplicationContext(),"isToken",true);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initImageLoader(){
        File cacheDir = StorageUtils.getCacheDirectory(this);  //缓存文件夹路径
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480,800)// default = device screen dimensions 内存缓存文件的最大长宽
                .diskCacheExtraOptions(480,800,null) // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
//                .taskExecutor(...)
//                .taskExecutorForCachedImages()
                .threadPoolSize(3)// default  线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)// default 设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO)// default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2* 1024* 1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(2* 1024* 1024) // 内存缓存的最大值
                .memoryCacheSizePercentage(13)// default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径
                .diskCacheSize(50* 1024* 1024)// 50 Mb sd卡(本地)缓存的最大值
                .diskCacheFileCount(100) // 可以缓存的文件数量
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())// default
                .writeDebugLogs()// 打印debug log
			    .build();
        ImageLoader.getInstance().init(config);
    }

}
