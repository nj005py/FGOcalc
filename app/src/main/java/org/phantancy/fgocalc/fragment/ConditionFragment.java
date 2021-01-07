package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.xw.repo.BubbleSeekBar;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.data.ConditionData;
import org.phantancy.fgocalc.databinding.FragConditionBinding;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

public class ConditionFragment extends LazyFragment {
    private FragConditionBinding binding;
    private CalcViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragConditionBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void init() {
        vm = ViewModelProviders.of(mActy).get(CalcViewModel.class);
        initView();
    }

    private void initView(){
        //职阶相性
        setSpAdapter(binding.spAffinity,ConditionData.getAffinityKeys());
        //阵营相性
        setSpAdapter(binding.spAttribute,ConditionData.getAttributeKeys());
        //宝具lv
        setSpAdapter(binding.spNpLv,ConditionData.npLvKeys);
        //芙芙atk
        setSpAdapter(binding.spFouAtk,ConditionData.getFouAtkKeys());
        //默认选1000
        binding.spFouAtk.setSelection(1);
        binding.spFouAtk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etAtkTotal.setText(vm.onFouAtkChanged(ConditionData.getFouAtkValues()[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //礼装atk
        setSpAdapter(binding.spEssenceAtk,ConditionData.essenceAtkKeys);
        binding.spEssenceAtk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etAtkTotal.setText(vm.onEssenceAtkChanged(ConditionData.essenceAtkValues[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //等级滑条
        binding.famSbLvSvt.setProgress(vm.getRewardLv());
        binding.famSbLvSvt.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                binding.etAtkTotal.setText(vm.onAtkLvChanged(progress));
                binding.etHpTotal.setText(vm.onHpLvChanged(progress));
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        //atk
        binding.etAtkTotal.setText(vm.getAtkDefaultKey());
        //总hp
        binding.etHpTotal.setText(vm.getHpDefaultKey());
        //剩余hp
        binding.etHpLeft.setText(vm.getHpLeftKey());
        //敌人数量
        setSpAdapter(binding.spEnemyCount,ConditionData.getEnemyCountKeys());
        binding.spEnemyCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleEnemyClassDisplay(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //敌人职阶
        setSpAdapter(binding.spEnemyClass1,ConditionData.classTypeKeys);
        setSpAdapter(binding.spEnemyClass2,ConditionData.classTypeKeys);
        setSpAdapter(binding.spEnemyClass3,ConditionData.classTypeKeys);
    }

    private void setSpAdapter(Spinner sp,String[] x) {
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(ctx, R.layout.entity_spinner,x);
        spAdapter.setDropDownViewResource(R.layout.entity_spinner);
        sp.setAdapter(spAdapter);
    }

    //控制显示敌方职阶设置
    private void handleEnemyClassDisplay(int position) {
        int count = ConditionData.getEnemyCountValues()[position];
        if (count == 1) {
            binding.llEnemySetting2.setVisibility(View.GONE);
            binding.llEnemySetting3.setVisibility(View.GONE);
        }
        if (count == 2) {
            binding.llEnemySetting2.setVisibility(View.VISIBLE);
            binding.llEnemySetting3.setVisibility(View.GONE);
        }
        if (count == 3) {
            binding.llEnemySetting2.setVisibility(View.VISIBLE);
            binding.llEnemySetting3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (isFirstTime && !hidden) {
//            initView();
//            isFirstTime = false;
//        }

    }
}
