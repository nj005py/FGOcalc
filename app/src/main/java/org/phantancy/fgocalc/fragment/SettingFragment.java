package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import org.phantancy.fgocalc.adapter.BuffInputAdapter;
import org.phantancy.fgocalc.databinding.FragBuffBinding;
import org.phantancy.fgocalc.databinding.FragSettingBinding;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;
import org.phantancy.fgocalc.viewmodel.MainViewModel;

public class SettingFragment extends BaseFragment {
//    private MainViewModel vm;
//    private FragSettingBinding binding;

    private FragBuffBinding binding;
    private CalcViewModel vm;
    private BuffInputAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragSettingBinding.inflate(getLayoutInflater());
        binding = FragBuffBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        vm = new ViewModelProvider(mActy).get(MainViewModel.class);
        vm = new ViewModelProvider(mActy).get(CalcViewModel.class);
        adapter = new BuffInputAdapter(ctx);
        GridLayoutManager layoutManager = new GridLayoutManager(ctx,2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = adapter.getItemViewType(position);
                if (type == 2) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        binding.rvBuffInput.setAdapter(adapter);
        binding.rvBuffInput.setLayoutManager(layoutManager);

        adapter.submitList(vm.getBuffInputList());
    }
}
