package org.phantancy.fgocalc.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.viewpager2.widget.ViewPager2;

import org.phantancy.fgocalc.R;

/**
 * Created by PY on 2017/11/10.
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
    private ProgressBar progressbar;
    private ViewPager2 parentPager;
    final String TAG = "ProgressWebView";
    private int progress = 0;

    public void setParentPager(ViewPager2 parentPager) {
        this.parentPager = parentPager;
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                10, 0, 0));

        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());

        WebSettings settings = getSettings();
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
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progress = newProgress;
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    public int getProgress(){
        return progress;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if ((parentPager != null) && (progress == 100)) {
                parentPager.setUserInputEnabled(false);
                Log.d(TAG,"parentPager false");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (clampedX && (parentPager != null) && (progress == 100)) {
            parentPager.setUserInputEnabled(true);
            Log.d(TAG,"parentPager true");
        }
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }


}
