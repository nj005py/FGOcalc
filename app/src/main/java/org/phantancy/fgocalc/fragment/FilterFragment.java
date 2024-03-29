package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.phantancy.fgocalc.adapter.FilterAdapter;
import org.phantancy.fgocalc.databinding.FragFilterBinding;
import org.phantancy.fgocalc.entity.FilterEntity;
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.util.List;

public class FilterFragment extends BaseFragment {

    private FragFilterBinding binding;
    private MainViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragFilterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(mActy).get(MainViewModel.class);

        FilterAdapter adapter = new FilterAdapter(ctx,null);
        binding.rvFilter.setAdapter(adapter);
        binding.rvFilter.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(ctx,5)));

        //获取筛选项
        Log.d(TAG,"获取筛选内容");
        vm.getFilters();

        vm.filters.observe(getViewLifecycleOwner(), new Observer<List<FilterEntity>>() {
            @Override
            public void onChanged(List<FilterEntity> filterEntities) {
                adapter.setItems(filterEntities);
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.setCurrentPage(0);
                vm.getServantByFilter(adapter.getItems());
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearFilter();
                vm.getAllServants();
            }
        });
    }

    /**
     * 职阶
     * 星数
     * 阵营
     * 排序
     * 特性
     * 宝具卡色
     * 宝具类型
     */
}
