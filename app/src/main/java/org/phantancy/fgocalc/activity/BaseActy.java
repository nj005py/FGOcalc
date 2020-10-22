package org.phantancy.fgocalc.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.phantancy.fgocalc.common.ActivityCollector;
import org.phantancy.fgocalc.dialog.LoadingDialog;

/**
 * Created by HATTER on 2017/11/4.
 */

public class BaseActy extends AppCompatActivity {
    protected Context ctx;
    protected String TAG = getClass().getName();
    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ActivityCollector.addActy(this);
        PushAgent.getInstance(ctx).onAppStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActy(this);
    }

    protected void showLoading(){
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ctx);
        }
        loadingDialog.show();
    }

    protected void stopLoading(){
        if (loadingDialog != null) {
            loadingDialog.stopAnim();
        }
    }
}
