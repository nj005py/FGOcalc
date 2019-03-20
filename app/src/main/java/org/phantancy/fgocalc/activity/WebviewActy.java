package org.phantancy.fgocalc.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.item.TipItem;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.util.ToolCase;
import org.phantancy.fgocalc.view.ProgressWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2016/9/2.
 */
public class WebviewActy extends BaseActivity {

    @BindView(R.id.aw_wv_web)
    ProgressWebView awWvWeb;
    @BindView(R.id.status_bar)
    LinearLayout statusBar;
    @BindView(R.id.aw_fab_back)
    FloatingActionButton awFabBack;
    @BindView(R.id.aw_fab_forward)
    FloatingActionButton awFabForward;
    @BindView(R.id.aw_srl_refresh)
    SwipeRefreshLayout awSrlRefresh;
    private String url;
    private int id;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.acty_webview);//自己手动设置正确的布局
            ButterKnife.bind(this);
            ctx = this;
            init();
        } catch (Exception e) {
            e.printStackTrace();
            url = getIntent().getStringExtra("url");
            if (!TextUtils.isEmpty(url)) {
                Uri uris = Uri.parse(url);
                startActivity(new Intent(Intent.ACTION_VIEW, uris));
                ToastUtils.displayShortToast(ctx, "系统组件缺失，帮您跳转外部浏览器");
            }
        }
    }

    private void init() {
        boolean webViewFirstTime = (Boolean) SharedPreferencesUtils.getParam(ctx,"webViewFirstTime",true);
        if (webViewFirstTime) {
            ToolCase.showTip(ctx, "tip_webview_intro.json");
            SharedPreferencesUtils.setParam(ctx,"webViewFirstTime",false);
        }
        url = getIntent().getStringExtra("url");
        int color = ContextCompat.getColor(ctx, R.color.colorGrayWiki);
        ToolCase.setStatusBar(llStatusBar, color, this, true, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        findViews();
        setListeners();
        if (!TextUtils.isEmpty(url)) {
            awWvWeb.setWebViewClient(new WebViewClient() {
                //覆盖shouldOverrideUrlLoading 方法
                @SuppressWarnings("deprecation")
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @TargetApi(Build.VERSION_CODES.N)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
            });
            settings = awWvWeb.getSettings();
            settings.setUseWideViewPort(true);

            settings.setTextZoom(100);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            settings.setSupportZoom(true);

            settings.setLoadWithOverviewMode(true);
            settings.setJavaScriptEnabled(true);
            settings.setAppCacheEnabled(true);
            //设置 缓存模式
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            // 开启 DOM storage API 功能
            settings.setDomStorageEnabled(true);
//            String url = new StringBuilder().append(url).toString();
            awWvWeb.loadUrl(url);
        }

        awSrlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        awWvWeb.loadUrl(url);
                        awSrlRefresh.setRefreshing(false);
                    }
                },1000);
            }
        });
    }

    private void findViews() {
    }

    private void setListeners() {
        awFabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awWvWeb.goBack();
            }
        });

        awFabForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awWvWeb.goForward();
            }
        });
    }
}
