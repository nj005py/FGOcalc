package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.databinding.EntityInfoTextBinding;
import org.phantancy.fgocalc.entity.InfoEntity;

public class InfoAdapter extends ListAdapter<InfoEntity, RecyclerView.ViewHolder> {
    static DiffUtil.ItemCallback<InfoEntity> diff = new DiffUtil.ItemCallback<InfoEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull InfoEntity oldItem, @NonNull InfoEntity newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull InfoEntity oldItem, @NonNull InfoEntity newItem) {
            return oldItem.getData().equals(newItem.getData());
        }
    };

    public InfoAdapter() {
        super(diff);
    }

    class TextViewHolder extends RecyclerView.ViewHolder {
        EntityInfoTextBinding binding;

        public TextViewHolder(EntityInfoTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(InfoEntity x) {
            binding.tvInfoTitle.setText(x.getTitle());
            binding.tvInfoData.setText(x.getData());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextViewHolder(EntityInfoTextBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TextViewHolder)holder).bindView(getItem(position));
    }

}
