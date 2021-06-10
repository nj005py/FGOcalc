package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.phantancy.fgocalc.adapter.CardsAdapter;
import org.phantancy.fgocalc.adapter.PickAdapter;
import org.phantancy.fgocalc.adapter.ResultAdapter;
import org.phantancy.fgocalc.databinding.FragCalcBinding;
import org.phantancy.fgocalc.entity.CardPickEntity;
import org.phantancy.fgocalc.item_decoration.LinearItemDecoration;
import org.phantancy.fgocalc.item_decoration.SpacesItemDecoration;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

import java.util.List;

/**
 * 计算页
 */
public class CalcFragment extends BaseFragment {
    private FragCalcBinding binding;
    private CalcViewModel vm;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragCalcBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(mActy).get(CalcViewModel.class);

        PickAdapter pickAdapter = new PickAdapter();
        CardsAdapter cardsAdapter = new CardsAdapter();

        binding.rvPicked.setAdapter(pickAdapter);
        final float scale = getResources().getDisplayMetrics().density;
        binding.rvPicked.addItemDecoration(new LinearItemDecoration((int) (60 * scale + 0.5f)));
        binding.rvCards.setAdapter(cardsAdapter);

        //已选卡
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

        vm.cardPicks.observe(getViewLifecycleOwner(), new Observer<List<CardPickEntity>>() {
            @Override
            public void onChanged(List<CardPickEntity> x) {
                cardsAdapter.submitList(x);
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

        //清理结果
        binding.btnClean.setOnClickListener(v -> {
            //恢复选卡
            vm.parsePickCards();
            pickAdapter.cleanList();
            //清理结果
            vm.cleanResult();
        });

        vm.getCalcResult().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvCalcResult.setText(s);
            }
        });

        //解析配卡
        vm.parsePickCards();

        ResultAdapter resultAdapter = new ResultAdapter();
        binding.rvCalcResult.setAdapter(resultAdapter);
        binding.rvCalcResult.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(ctx,5)));
        //设置结果
        vm.resultList.observe(getViewLifecycleOwner(),result -> resultAdapter.submitList(result));
    }

    private void setOverkillCritical() {
        binding.cbOk1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setOverkill1(isChecked);
            }
        });
        binding.cbOk2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setOverkill2(isChecked);
            }
        });
        binding.cbOk3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setOverkill3(isChecked);
            }
        });
        binding.cbOk4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setOverkill4(isChecked);
            }
        });
        binding.cbCr1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setCritical1(isChecked);
            }
        });
        binding.cbCr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setCritical2(isChecked);
            }
        });
        binding.cbCr3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vm.calcEntity.setCritical3(isChecked);
            }
        });
    }
}
