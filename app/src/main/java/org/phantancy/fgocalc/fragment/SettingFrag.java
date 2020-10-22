package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.phantancy.fgocalc.databinding.FragSettingBinding;
import org.phantancy.fgocalc.entity.FilterEntity;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

import java.util.List;

public class SettingFrag extends BaseFrag{
    private MainViewModel vm;
    private FragSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragSettingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(mActy).get(MainViewModel.class);

    }
}
