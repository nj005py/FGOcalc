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
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xw.repo.BubbleSeekBar;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.data.ConditionData;
import org.phantancy.fgocalc.databinding.FragConditionBinding;
import org.phantancy.fgocalc.entity.NoblePhantasmEntity;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;
import org.phantancy.fgocalc.viewmodel.ConditionViewModel;

import java.util.ArrayList;
import java.util.List;

public class ConditionFragment extends BaseFragment {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(mActy).get(CalcViewModel.class);
        conVm = new ViewModelProvider(mActy).get(ConditionViewModel.class);
        initView();
        initNp();
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存UI数据
        Log.d(TAG, "保存条件数据");
        if (check()) {
            String atk = binding.etAtkTotal.getText().toString();
            String hp = binding.etHpTotal.getText().toString();
            String hpLeft = binding.etHpLeft.getText().toString();
            vm.saveCondition(atk, hp, hpLeft);
        }

    }

    private boolean check() {
        if (!TextUtils.isEmpty(binding.etAtkTotal.getText())) {
            ToastUtils.showToast("atk不能为空");
            return false;
        }
        if (!TextUtils.isEmpty(binding.etHpTotal.getText())) {
            ToastUtils.showToast("hp不能为空");
            return false;
        }
        if (!TextUtils.isEmpty(binding.etHpLeft.getText())) {
            ToastUtils.showToast("剩余hp不能为空");
            return false;
        }
        return true;
    }

    private void initView() {
        //职阶相性
        setSpAdapter(binding.spAffinity, ConditionData.getAffinityKeys());
        binding.spAffinity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.saveAffinity(ConditionData.getAffinityValues()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm.saveAffinity(ConditionData.getAffinityValues()[0]);
            }
        });
        //阵营相性
        setSpAdapter(binding.spAttribute, ConditionData.getAttributeKeys());
        binding.spAttribute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.saveAttribute(ConditionData.getAttributeValues()[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vm.saveAttribute(ConditionData.getAttributeValues()[0]);
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
        setSpAdapter(binding.spEssenceAtk, ConditionData.getEssenceAtkKeys());
        binding.spEssenceAtk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etAtkTotal.setText(vm.onEssenceAtkChanged(ConditionData.getEssenceAtkValues()[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //等级滑条，数据库查询成功后初始化
        vm.getSvtExpEntities().observe(getViewLifecycleOwner(), (svtExpEntities -> {
            //进度条最大值
            binding.famSbLvSvt.setProgress(vm.getRewardLv());
            vm.setSvtExpEntities(svtExpEntities);
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
        }));

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

    //宝具相关初始化
    private void initNp() {
        //获取宝具信息
        conVm.getNPEntities(vm.getServant().id).observe(getViewLifecycleOwner(), entities -> {
            String[] desArr = new String[entities.size()];
            npList.addAll(entities);

            for (int i = 0; i < entities.size(); i++) {
                desArr[i] = entities.get(i).npDes;
            }
            //宝具选择
            setSpAdapter(binding.spNpSelect, desArr);
            binding.spNpSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (npList != null) {
                        NoblePhantasmEntity it = npList.get(position);
                        vm.calcEntity.setNpEntity(it);
                        vm.parseNpBuff(it, binding.spNpLv.getSelectedItem().toString());
                        vm.parsePickCards(it);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if (npList != null) {
                        vm.calcEntity.setNpEntity(npList.get(0));
                        vm.parseNpBuff(npList.get(0), binding.spNpLv.getSelectedItem().toString());
                    }
                }
            });
            //宝具lv
            setSpAdapter(binding.spNpLv, ConditionData.npLvKeys);
            binding.spNpLv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (vm.calcEntity.getNpEntity() != null) {
                        //buff
                        vm.parseNpBuff(vm.calcEntity.getNpEntity(), binding.spNpLv.getSelectedItem().toString());
                        //倍率
                        vm.setNpDmgMultiplier(vm.calcEntity.getNpEntity(), position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    if (npList != null) {
                        //buff
                        vm.parseNpBuff(npList.get(0), binding.spNpLv.getSelectedItem().toString());
                        //倍率
                        vm.setNpDmgMultiplier(npList.get(0), 0);
                    }
                }
            });

        });
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
