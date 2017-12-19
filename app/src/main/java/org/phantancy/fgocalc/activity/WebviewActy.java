package org.phantancy.fgocalc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.view.ProgressWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2016/9/2.
 */
public class WebviewActy extends BaseActivity {

    @BindView(R.id.aw_wv_web)
    ProgressWebView awWvWeb;
    private String url;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.acty_webview);//自己手动设置正确的布局
            ButterKnife.bind(this);
            mContext = this;
            init();
        } catch (Exception e) {
            e.printStackTrace();
            url = getIntent().getStringExtra("url");
            if (!TextUtils.isEmpty(url)) {
                Uri uris = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uris));
                ToastUtils.displayShortToast(mContext, "系统组件缺失，帮您跳转外部浏览器");
            }
        }
    }

    private void init() {
        url = getIntent().getStringExtra("url");
        findViews();
        setListeners();
        if (!TextUtils.isEmpty(url)) {
            awWvWeb.setWebViewClient(new WebViewClient() {
                //覆盖shouldOverrideUrlLoading 方法
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            awWvWeb.getSettings().setJavaScriptEnabled(true);
            awWvWeb.getSettings().setAppCacheEnabled(true);
            //设置 缓存模式
            awWvWeb.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            // 开启 DOM storage API 功能
            awWvWeb.getSettings().setDomStorageEnabled(true);
//            String url = new StringBuilder().append(url).toString();
            awWvWeb.loadUrl(url);
        }
    }

    private void findViews() {
    }

    private void setListeners() {
    }
}
