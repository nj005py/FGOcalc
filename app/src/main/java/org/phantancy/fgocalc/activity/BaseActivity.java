package org.phantancy.fgocalc.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.ActivityCollector;
import org.phantancy.fgocalc.util.BaseUtils;
import com.spreada.utils.chinese.ZHConverter;

/**
 * Created by PY on 2016/10/26.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected Context ctx;
    public String TAG = getClass().getName();
    public LinearLayout llStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void initBaseStatusBar(Context context){
        llStatusBar = (LinearLayout)findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT == 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int height = BaseUtils.getStatusBarHeight(context);
            llStatusBar.setPadding(0, height, 0, 0);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
            int height = BaseUtils.getStatusBarHeight(context);
            llStatusBar.setPadding(0, height, 0, 0);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
