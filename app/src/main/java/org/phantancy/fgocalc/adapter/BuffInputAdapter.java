package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.phantancy.fgocalc.databinding.EntityBuffInputBinding;
import org.phantancy.fgocalc.databinding.EntityBuffInputCategoryBinding;
import org.phantancy.fgocalc.entity.BuffInputEntity;
import org.phantancy.fgocalc.entity.ShortcutBuffEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuffInputAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BuffInputEntity> mList;
    private Context ctx;
    final String TAG = "BuffInputAdapter";

    public BuffInputAdapter(Context ctx) {
        this.ctx = ctx;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //buff类型 0:整数 1:百分号数 2:分类
        if (viewType == 2) {
            EntityBuffInputCategoryBinding binding = EntityBuffInputCategoryBinding.inflate(LayoutInflater.from(ctx),parent,false);
            return new CategoryViewHolder(binding);
        } else {
            EntityBuffInputBinding binding = EntityBuffInputBinding.inflate(LayoutInflater.from(ctx),parent,false);
            return new InputViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BuffInputEntity x = getItem(position);
        int type = x.getType();
        if (type == 2) {
            ((CategoryViewHolder) holder).bindView(x);
        } else {
            ((InputViewHolder)holder).bindView(x);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    public BuffInputEntity getItem(int position){
        return mList.get(position);
    }

    public void submitList(List<BuffInputEntity> list) {
        if (list != null) {
            mList = list;
        }
        notifyDataSetChanged();
    }

    public List<BuffInputEntity> getList() {
        return mList;
    }

    //加buff
    public void addBuff(ShortcutBuffEntity x) {
        Map<String,Double> map = x.getBuffMap();
        for (String buffKey : map.keySet()) {
            for (int i = 0;i < mList.size();i++) {
                BuffInputEntity input = mList.get(i);
                if (buffKey.equals(input.getKey())) {
                    //发现需要加的buff
                    double oldVal = input.getValue();
                    double newVal = oldVal + map.get(buffKey);
                    Log.d(TAG,"newVal:" + newVal);
                    mList.get(i).setValue(newVal);
                }
            }
        }
        notifyDataSetChanged();
    }

    //减buff
    public void reduceBuff(ShortcutBuffEntity x) {
        Map<String,Double> map = x.getBuffMap();
        for (String buffKey : map.keySet()) {
            for (int i = 0;i < mList.size();i++) {
                BuffInputEntity input = mList.get(i);
                if (buffKey.equals(input.getKey())) {
                    //发现需要减的buff
                    double oldVal = input.getValue();
                    double newVal = map.get(buffKey);
                    if (oldVal >= newVal) {
                        newVal = oldVal - newVal;
                    }
                    Log.d(TAG,"newVal:" + newVal);
                    mList.get(i).setValue(newVal);
                }
            }
        }
        notifyDataSetChanged();
    }

    //重置buff，清空所有buff
    public void resetBuff() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setValue(0d);
        }
        notifyDataSetChanged();
    }

    class InputViewHolder extends RecyclerView.ViewHolder {
        EntityBuffInputBinding binding;

        public InputViewHolder(EntityBuffInputBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(BuffInputEntity x) {
            Drawable icon = ContextCompat.getDrawable(ctx,x.getIcon());
            binding.ivBuffIcon.setImageDrawable(icon);
            binding.etBuff.setHint(x.getKey());
            binding.etBuff.setText(x.getValueDisplay());
            int type = x.getType();
            if (type == 0) {
                binding.tvPercentIcon.setVisibility(View.INVISIBLE);
            } else {
                binding.tvPercentIcon.setVisibility(View.VISIBLE);
            }
            binding.etBuff.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s)) {
                        x.setValue(0d);
                    } else {
                        x.setValue(Double.parseDouble(s.toString()));
                    }
                }
            });
        }
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        EntityBuffInputCategoryBinding binding;

        public CategoryViewHolder(EntityBuffInputCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(BuffInputEntity x) {
            binding.tvBuffCategory.setText(x.getKey());

        }
    }


}
