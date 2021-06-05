package org.phantancy.fgocalc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.phantancy.fgocalc.activity.CalcActy;
import org.phantancy.fgocalc.adapter.ServantAdapter;
import org.phantancy.fgocalc.databinding.FragMainBinding;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.util.ToolCase;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.util.List;

public class MainFragment extends BaseFragment {

    private FragMainBinding binding;
    //    final String TAG = "MainFrag";
    private MainViewModel vm;
    //入口，判断点击从者头像行为
    //主界面
    public final static int ENTRY_MAIN = 0X0;
    //搜索界面
    public final static int ENTRY_SEARCH = 0X1;
    private int entry = ENTRY_MAIN;

    public MainFragment() {
    }

    public MainFragment(int entry) {
        this.entry = entry;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(mActy).get(MainViewModel.class);
        //列表绑定适配器
        ServantAdapter adapter = new ServantAdapter();
        binding.rvList.setAdapter(adapter);

        //获取从者列表
        Log.d(TAG, "获取从者列表");
        vm.getAllServants();

        //获得从者数据时，更新列表
        vm.getServants().observe(getViewLifecycleOwner(), new Observer<List<ServantEntity>>() {
            @Override
            public void onChanged(List<ServantEntity> servantEntities) {
                Log.d(TAG, "size:" + servantEntities.size());
                adapter.submitList(servantEntities);
            }
        });

        //关键词搜索
        vm.getKeyword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (TextUtils.isEmpty(s)) {
                    vm.getAllServants();
                } else {
                    vm.getServantsByKeyword(s);
                }
                Log.d(TAG, "keyword:" + s);
            }
        });

        vm.getClearSearch().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean needClear) {
                if (needClear) {
                    binding.etSearch.setText("");
                    ToolCase.closeKeybord(mActy);
                }
            }
        });

        //listener
        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String x = ToolCase.getViewValue(binding.etSearch);
                    x = x.replace(",", "");
                    x = x.replace("，", "");
                    vm.setKeyword(x);
                    Log.d(TAG, "etSearch: search");
                    return true;
                }
                return false;
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                vm.setKeyword(s.toString());
            }
        });

        adapter.setIServantClickListener(new ServantAdapter.IServantClickListener() {
            @Override
            public void openCalcPage(ServantEntity x, ImageView y) {
                switch (entry) {
                    case ENTRY_MAIN:
                        navigateCalc(x, y);
                        break;
                    case ENTRY_SEARCH:
                        navigateSearch(x);
                        break;
                    default:
                        navigateCalc(x, y);
                        break;
                }

            }
        });
    }

    private void navigateCalc(ServantEntity svt, ImageView imageView) {
        Intent i = new Intent(mActy, CalcActy.class);
        ActivityOptionsCompat actyOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mActy, imageView, "avatar");
        i.putExtra("servant", svt);
        startActivity(i, actyOptions.toBundle());
    }

    private void navigateSearch(ServantEntity svt) {
        Intent i = new Intent();
        i.putExtra("servant", svt);
        mActy.setResult(Activity.RESULT_OK, i);
        mActy.finish();
    }

}
