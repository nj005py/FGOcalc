package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import org.phantancy.fgocalc.databinding.FragWikiBinding;
import org.phantancy.fgocalc.databinding.LayoutWebviewBinding;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

public class WikiFragment extends LazyFragment {
    private FragWikiBinding binding;
    private LayoutWebviewBinding webviewBinding;
    private CalcViewModel vm;
    private String url;
    private ViewPager2 parentPager;

    public void setParentPager(ViewPager2 parentPager){
        this.parentPager = parentPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragWikiBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        //清理webview
        if (webviewBinding != null && webviewBinding.wvWiki != null) {
            webviewBinding.wvWiki.setWebChromeClient(null);
            webviewBinding.wvWiki.setWebViewClient(null);
            webviewBinding.wvWiki.getSettings().setJavaScriptEnabled(false);
            webviewBinding.wvWiki.clearCache(true);
            webviewBinding.wvWiki.removeAllViews();
            webviewBinding.wvWiki.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void init() {
        vm = ViewModelProviders.of(mActy).get(CalcViewModel.class);
        url = vm.getServantWiki();

        binding.tvLoadWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.vsWebview.inflate();
            }
        });


        //占位视图监听
        binding.vsWebview.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                webviewBinding = LayoutWebviewBinding.bind(inflated);
                loadWebview();
            }
        });
    }

    private void loadUrl() {
        webviewBinding.wvWiki.loadUrl(url);
    }

    private void loadWebview() {
        webviewBinding.wvWiki.setParentPager(parentPager);
        webviewBinding.wvWiki.setOverScrollMode(WebView.OVER_SCROLL_IF_CONTENT_SCROLLS);
//        webviewBinding.srlWiki.setEnabled(false);
        webviewBinding.srlWiki.setNestedScrollingEnabled(false);
        webviewBinding.srlWiki.requestDisallowInterceptTouchEvent(true);
//        webviewBinding.wvWiki.loadUrl(url);
        webviewBinding.srlWiki.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webviewBinding.wvWiki.loadUrl(url);
                        webviewBinding.srlWiki.setRefreshing(false);
                    }
                },1000);
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webviewBinding.wvWiki.goBack();
            }
        });

        binding.ivForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webviewBinding.wvWiki.goForward();
            }
        });

        //load url
        loadUrl();
    }
}
