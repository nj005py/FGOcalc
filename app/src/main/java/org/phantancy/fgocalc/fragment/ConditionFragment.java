package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xw.repo.BubbleSeekBar;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.data.ConditionData;
import org.phantancy.fgocalc.databinding.FragConditionBinding;
import org.phantancy.fgocalc.entity.NoblePhantasmEntity;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;
import org.phantancy.fgocalc.viewmodel.ConditionViewModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionFragment extends LazyFragment {
    private FragConditionBinding binding;
    private CalcViewModel vm;
    private ConditionViewModel conVm;
    List<NoblePhantasmEntity> npList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragConditionBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存UI数据
        Log.d(TAG, "保存条件数据");
        if (!TextUtils.isEmpty(binding.etHpTotal.getText())) {
            vm.inputData.setHp(Double.parseDouble(binding.etHpTotal.getText().toString()));
        } else {
            vm.inputData.setHp(0d);
        }
        if (!TextUtils.isEmpty(binding.etHpLeft.getText())) {
            vm.inputData.setHpLeft(Double.parseDouble(binding.etHpLeft.getText().toString()));
        } else {
            vm.inputData.setHpLeft(0d);
        }
        vm.saveCondition();
    }

    @Override
    protected void init() {
        vm = ViewModelProviders.of(mActy).get(CalcViewModel.class);
        conVm = ViewModelProviders.of(mActy).get(ConditionViewModel.class);
        initView();
        //获取宝具信息
        conVm.getNPEntities(vm.getServant().id).observe(this, entities -> {
            String[] desArr = new String[entities.size()];
            npList.addAll(entities);

            for (int i = 0; i < entities.size(); i++) {
                desArr[i] = entities.get(i).npDes;
            }
            setSpAdapter(binding.spNpSelect, desArr);
        });
    }

    private void initView() {
        //职阶相性
        setSpAdapter(binding.spAffinity, ConditionData.getAffinityKeys());
        binding.spAffinity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.inputData.setAffinityType(ConditionData.getAffinityKeys()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm.inputData.setAffinityType(ConditionData.getAffinityKeys()[0]);
            }
        });
        //阵营相性
        setSpAdapter(binding.spAttribute, ConditionData.getAttributeKeys());
        binding.spAttribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.inputData.setAttributeType(ConditionData.getAttributeKeys()[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm.inputData.setAttributeType(ConditionData.getAttributeKeys()[0]);
            }
        });
        //宝具选择
        binding.spNpSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (npList != null) {
                    NoblePhantasmEntity it = npList.get(position);
                    vm.inputData.setNpEntity(it);
                    vm.parseNpBuff(it,binding.spNpLv.getSelectedItem().toString());
                    vm.parsePickCards(it);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (npList != null) {
                    vm.inputData.setNpEntity(npList.get(0));
                    vm.parseNpBuff(npList.get(0),binding.spNpLv.getSelectedItem().toString());
                }
            }
        });
        //宝具lv
        setSpAdapter(binding.spNpLv, ConditionData.npLvKeys);
        binding.spNpLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (vm.inputData.getNpEntity() != null) {
                    //buff
                    vm.parseNpBuff(vm.inputData.getNpEntity(),binding.spNpLv.getSelectedItem().toString());
                    //倍率
                    vm.setNpDmgMultiplier(vm.inputData.getNpEntity(),position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (npList != null) {
                    //buff
                    vm.parseNpBuff(npList.get(0),binding.spNpLv.getSelectedItem().toString());
                    //倍率
                    vm.setNpDmgMultiplier(npList.get(0),0);
                }
            }
        });
        //芙芙atk
        setSpAdapter(binding.spFouAtk, ConditionData.getFouAtkKeys());
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
        setSpAdapter(binding.spEssenceAtk, ConditionData.essenceAtkKeys);
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
        setSpAdapter(binding.spEnemyCount, ConditionData.getEnemyCountKeys());
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
        setSpAdapter(binding.spEnemyClass1, ConditionData.classTypeKeys);
        setSpAdapter(binding.spEnemyClass2, ConditionData.classTypeKeys);
        setSpAdapter(binding.spEnemyClass3, ConditionData.classTypeKeys);
    }

    private void setSpAdapter(Spinner sp, String[] x) {
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(ctx, R.layout.entity_spinner, x);
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
    }
}
