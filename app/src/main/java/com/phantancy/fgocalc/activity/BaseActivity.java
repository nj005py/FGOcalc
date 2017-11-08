package com.phantancy.fgocalc.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.common.ActivityCollector;
import com.phantancy.fgocalc.util.BaseUtils;
import com.spreada.utils.chinese.ZHConverter;

/**
 * Created by PY on 2016/10/26.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    protected Context mContext;
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
//            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
            int height = BaseUtils.getStatusBarHeight(context);
            llStatusBar.setPadding(0, height, 0, 0);
        }
    }


    /**
     * 写这个方法不是为了装逼，而是为了方便设置颜色
     * */
    public int getColorName(int colorName){
        return getResources().getColor(colorName);
    }

    //只是获取输入框的值
    public String etValue(EditText et){
        String value = et.getText().toString().trim();
        if (notEmpty(value)) {
            return value;
        }else {
            return null;
        }
    }

    //只是获取输入框的值int
    public int etInt(EditText et){
        int value = Integer.parseInt(et.getText().toString().trim());
        return value;
    }

    //为输入框设置值
    public void etSet(EditText et,String s){
        if (notEmpty(s)) {
            et.setText(s);
        }
    }

    //批量重置EditText
    public void etReset(EditText[] ets){
        for(int k = 0;k < ets.length;k ++){
            ets[k].setText("");
        }
    }

    //判断不为null不为空
    public Boolean notEmpty(String s){
        if (s != null && !s.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }

    //spinner绑定数据源,simple样式限定
    public void spInitSimple(Context context,String[] str,Spinner sp){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_simple_spinner,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
        sp.setAdapter(spAdapter);
    }

    //spinner绑定数据源,特供版
    public void spInitSpecial(Context context,String[] str,Spinner sp){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_simple_spinner,str){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
        sp.setAdapter(spAdapter);
        sp.setSelection(spAdapter.getCount());
    }

    //繁体转简体
    public String tc2sc(String str) {
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
        String simplifiedStr = converter.convert(str);
        return simplifiedStr;
    }

    @Override
    public void onClick(View v) {

    }
}
