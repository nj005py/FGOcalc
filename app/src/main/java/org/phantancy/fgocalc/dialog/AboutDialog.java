package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ToolCase;

/**
 * Created by HATTER on 2017/8/8.
 */

public class AboutDialog extends Dialog{
    private Context mContext;
    private TextView tvVersion;
    private WebView wv;

    public AboutDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_about);
        mContext = context;
        tvVersion = findViewById(R.id.da_tv_version);
        wv = findViewById(R.id.da_wv_about);
        //设置显示内容
        setCast();
    }

    public void setVersion(String value){
        ToolCase.setViewValue(tvVersion,"当前版本:" + value);
    }

    public void setCast(){
//        WebSettings webSettings = wv.getSettings();
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
        wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wv.setScrollbarFadingEnabled(false);
        wv.loadUrl("file:///android_asset/cast.html");
    }
}
