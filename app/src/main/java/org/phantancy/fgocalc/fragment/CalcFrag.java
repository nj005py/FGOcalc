package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.phantancy.fgocalc.adapter.CardsAdapter;
import org.phantancy.fgocalc.adapter.PickAdapter;
import org.phantancy.fgocalc.databinding.FragCalcBinding;
import org.phantancy.fgocalc.entity.CardPickEntity;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

import java.util.List;

public class CalcFrag extends BaseFrag {
    private FragCalcBinding binding;
    private CalcViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragCalcBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = ViewModelProviders.of(mActy).get(CalcViewModel.class);

        PickAdapter pickAdapter = new PickAdapter();
        CardsAdapter cardsAdapter = new CardsAdapter();

        binding.rvPicked.setAdapter(pickAdapter);
        binding.rvCards.setAdapter(cardsAdapter);

        //选卡
        pickAdapter.setEntityListenr(new PickAdapter.IEntityListener() {
            @Override
            public void handleClickEvent(CardPickEntity x) {
                cardsAdapter.returnEntity(x);
            }
        });

        //备选卡
        cardsAdapter.setEntityListenr(new CardsAdapter.IEntityListener() {
            @Override
            public void handleClickEvent(CardPickEntity x) {
                pickAdapter.addEntity(x);
            }
        });

        vm.getCardPicks().observe(this, new Observer<List<CardPickEntity>>() {
            @Override
            public void onChanged(List<CardPickEntity> x) {
                cardsAdapter.submitList(x);
            }
        });

        binding.btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.clickCalc(pickAdapter.getEntities());
            }
        });

        //监听overkill 暴击
        setOverkillCritical();

        //计算
        binding.btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vm.clickCalc(pickAdapter.getEntities());
            }
        });

        vm.getCalcResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvCalcResult.setText(s);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //解析配卡
        vm.parsePickCards();
    }

    private void setOverkillCritical() {
        binding.cbOk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setOverkill1(isChecked);
            }
        });
        binding.cbOk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setOverkill2(isChecked);
            }
        });
        binding.cbOk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setOverkill3(isChecked);
            }
        });
        binding.cbOk4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setOverkill4(isChecked);
            }
        });
        binding.cbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setCritical1(isChecked);
            }
        });
        binding.cbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setCritical2(isChecked);
            }
        });
        binding.cbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.inputData.setCritical3(isChecked);
            }
        });
    }
}
