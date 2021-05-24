package org.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.BuffInputAdapter;
import org.phantancy.fgocalc.entity.IBuffShortCut;
import org.phantancy.fgocalc.databinding.FragBuffBinding;
import org.phantancy.fgocalc.dialog.ShortcutMstEquipmentDialog;
import org.phantancy.fgocalc.dialog.ShortcutSupportDialog;
import org.phantancy.fgocalc.entity.ShortcutBuffEntity;
import org.phantancy.fgocalc.util.ToastUtils;
import org.phantancy.fgocalc.viewmodel.CalcViewModel;

public class BuffFragment extends BaseFragment {
    private FragBuffBinding binding;
    private CalcViewModel vm;
    private BuffInputAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragBuffBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        //重置键
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resetBuff();
            }
        });

        //拐快捷键
        binding.btnShortcutSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics metrics = new DisplayMetrics();
                mActy.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                ShortcutSupportDialog x = new ShortcutSupportDialog(ctx,new IBuffShortCut() {
                    @Override
                    public void addBuffs(ShortcutBuffEntity x) {
                        ToastUtils.displayShortToast(ctx,x.getBuffDes());
                        adapter.addBuff(x);
                    }

                    @Override
                    public void reduceBuffs(ShortcutBuffEntity x) {
                        ToastUtils.displayShortToast(ctx,ctx.getResources().getString(R.string.reduce_buff));
                        adapter.reduceBuff(x);
                    }
                });
                x.getWindow().setLayout((int) (width * 0.9), (int) (height * 0.7));
                x.show();
            }
        });

        //御主服快捷键
        binding.btnShortcutMstEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayMetrics metrics = new DisplayMetrics();
                mActy.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                ShortcutMstEquipmentDialog x = new ShortcutMstEquipmentDialog(ctx, new IBuffShortCut() {
                    @Override
                    public void addBuffs(ShortcutBuffEntity x) {
                        ToastUtils.displayShortToast(ctx,x.getBuffDes());
                        adapter.addBuff(x);
                    }

                    @Override
                    public void reduceBuffs(ShortcutBuffEntity x) {
                        ToastUtils.displayShortToast(ctx,ctx.getResources().getString(R.string.reduce_buff));
                        adapter.reduceBuff(x);
                    }
                });
                x.getWindow().setLayout((int) (width * 0.9), (int) (height * 0.7));
                x.show();
            }
        });

        //监听宝具自带buff变化
        vm.getBuffFromNp().observe(getViewLifecycleOwner(), buffMap -> {
            adapter.addBuffFromNp(buffMap,vm.preNpBuff);
            //缓存本次宝具buff
            vm.preNpBuff = buffMap;
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //保存UI数据
        Log.d(TAG,"保存buff数据");
        vm.saveBuff(adapter.getList());

    }
}
