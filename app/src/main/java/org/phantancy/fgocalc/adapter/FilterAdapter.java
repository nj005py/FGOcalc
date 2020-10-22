package org.phantancy.fgocalc.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.databinding.EntityFilterBinding;
import org.phantancy.fgocalc.entity.FilterEntity;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private Context ctx;
    private List<FilterEntity> mList;

    public FilterAdapter(Context ctx, List<FilterEntity> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    public void setItems(List<FilterEntity> list){
        if (list != null && mList != null) {
            mList.clear();
            mList.addAll(list);
        }else if (list != null && mList == null) {
            mList = list;
        }else {
            if (mList != null) {
                mList.clear();
            }
        }
        notifyDataSetChanged();
    }

    public List<FilterEntity> getItems(){
        return mList;
    }

    public void clearFilter(){
        if (mList != null) {
            for (int i = 0;i < mList.size(); i ++) {
                mList.get(i).setReset(true);
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        EntityFilterBinding binding;

        public ViewHolder(EntityFilterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(FilterEntity x) {
            binding.tvFilter.setText(x.getTitle());
            //设置选择栏数据
            ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(ctx, R.layout.entity_spinner,x.getHint());
            spAdapter.setDropDownViewResource(R.layout.entity_spinner);
            binding.spFilter.setAdapter(spAdapter);

            binding.spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    x.setSelectionPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    x.setSelectionPosition(0);
                }
            });

            if (x.isReset()) {
                binding.spFilter.setSelection(0);
                x.setReset(false);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(EntityFilterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        holder.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
