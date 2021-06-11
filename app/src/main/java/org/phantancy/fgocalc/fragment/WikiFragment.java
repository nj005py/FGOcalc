package org.phantancy.fgocalc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.activity.SearchServantActy;
import org.phantancy.fgocalc.databinding.FragWikiBinding;
import org.phantancy.fgocalc.databinding.LayoutWebviewBinding;
import org.phantancy.fgocalc.dialog.CharacterDialog;
import org.phantancy.fgocalc.entity.CharacterEntity;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WikiFragment extends LazyFragment {
    private FragWikiBinding binding;
    private LayoutWebviewBinding webviewBinding;
    private CalcViewModel vm;
    private String url;
    private ViewPager2 parentPager;
    public static int SEARCH_SERVANT = 0X0;
    private ActivityResultLauncher<Intent> resultLauncher;

    public void setParentPager(ViewPager2 parentPager) {
        this.parentPager = parentPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragWikiBinding.inflate(inflater, container, false);
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
        vm = new ViewModelProvider(mActy).get(CalcViewModel.class);
        //搜索
        binding.tvSearchSvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultLauncher != null) {
                    Intent i = new Intent(ctx, SearchServantActy.class);
                    resultLauncher.launch(i);
                }
            }
        });

        //加载
        binding.tvLoad.setOnClickListener(v -> {
            selectWiki(vm.getServant().id);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ServantEntity svt = (ServantEntity) data.getParcelableExtra("servant");
                            ToastUtils.showToast(svt.name);
                            selectWiki(svt.id);
                        }
                    }
                }
        );
    }

    //选wiki来源
    private void selectWiki(int id) {
        CharacterDialog d = new CharacterDialog(ctx);
        CharacterEntity e = new CharacterEntity("选个", R.drawable.doctor);
        e.options = Stream.of(
                new CharacterEntity.OptionEntity("fgowiki", () -> {
                    url = vm.getServantWiki(id);
                    if (webviewBinding == null) {
                        binding.vsWebview.inflate();
                    } else {
                        loadWebview();
                    }
                    d.dismiss();
                }),
                new CharacterEntity.OptionEntity("mooncell", () -> {
                    url = vm.getServantMooncell(id);
                    if (webviewBinding == null) {
                        binding.vsWebview.inflate();
                    } else{
                        loadWebview();
                    }
                    d.dismiss();
                })
        ).collect(Collectors.toList());
        d.setEntity(e);
        d.show();
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
                }, 1000);
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
