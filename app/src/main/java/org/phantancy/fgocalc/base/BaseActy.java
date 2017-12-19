package org.phantancy.fgocalc.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.phantancy.fgocalc.common.ActivityCollector;

/**
 * Created by HATTER on 2017/11/4.
 * Created for MVP
 * 为MVP模式创建的BaseActy与另一个BaseAcitity区分
 */

public class BaseActy extends AppCompatActivity {
    protected Context ctx;
    protected String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ActivityCollector.addActy(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActy(this);
    }
}
