package org.phantancy.fgocalc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.phantancy.fgocalc.databinding.EntityCardPickBinding;
import org.phantancy.fgocalc.entity.CardPickEntity;

import java.util.ArrayList;
import java.util.List;

public class PickAdapter extends RecyclerView.Adapter<PickAdapter.MyViewHolder> {
    private List<CardPickEntity> mList;

    public PickAdapter() {
        mList = new ArrayList<>();
    }

    public void addEntity(CardPickEntity x) {
        mList.add(x);
        notifyDataSetChanged();
    }

    public List<CardPickEntity> getEntities() {
        return mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EntityCardPickBinding binding = EntityCardPickBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public CardPickEntity getItem(int position) {
        return mList.get(position);
    }

    IEntityListener entityListener;

    public void setEntityListenr(IEntityListener x) {
        this.entityListener = x;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        EntityCardPickBinding binding;

        public MyViewHolder(@NonNull EntityCardPickBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(final int position) {
            CardPickEntity x = getItem(position);
            binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(),x.getImg()));
            binding.ivPickCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    entityListener.handleClickEvent(x);
                    notifyItemRemoved(position);
                    mList.remove(x);
                    notifyItemRangeChanged(position, getItemCount() - 1);
                }
            });
        }
    }

    public interface IEntityListener {
        void handleClickEvent(CardPickEntity x);
    }


}
