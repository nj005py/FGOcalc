package org.phantancy.fgocalc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import org.phantancy.fgocalc.adapter.BuffInputAdapter;
import org.phantancy.fgocalc.adapter.SettingAdapter;
import org.phantancy.fgocalc.databinding.FragBuffBinding;
import org.phantancy.fgocalc.databinding.FragSettingBinding;
import org.phantancy.fgocalc.entity.SettingEntity;
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends BaseFragment {
    private MainViewModel vm;
    private FragSettingBinding binding;
    final static int FLUTTER_VIEW = 0X0;
    final static int RELOAD_DATABASE = 0X1;
    final static int JOIN_GROUP = 0X2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragSettingBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(mActy).get(MainViewModel.class);

        SettingAdapter adapter = new SettingAdapter();
        binding.rvSetting.setAdapter(adapter);
        binding.rvSetting.addItemDecoration(new SpacesItemDecoration(15));
        adapter.setSettingInterface(new SettingAdapter.SettingInterface() {
            @Override
            public void handleClick(int code) {
                Intent intent = new Intent();
                switch (code) {
                    case RELOAD_DATABASE:
                        break;
                    case JOIN_GROUP:
                        break;
                }
            }
        });
        adapter.submitList(getSettings());
    }

    private List<SettingEntity> getSettings() {
        List<SettingEntity> list = new ArrayList<>();
        list.add(new SettingEntity(RELOAD_DATABASE,"重载数据库"));
        list.add(new SettingEntity(JOIN_GROUP,"加QQ群"));
        return list;
    }
}
