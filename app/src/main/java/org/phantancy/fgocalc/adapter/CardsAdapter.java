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

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
    private List<CardPickEntity> mList;

    public CardsAdapter() {
        mList = new ArrayList<>();
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

    public interface IEntityListener{
        void handleClickEvent(CardPickEntity x);
    }

    IEntityListener entityListener;

    public void setEntityListenr(IEntityListener x){
        this.entityListener = x;
    }

    public void submitList(List<CardPickEntity> x) {
        mList = x;
        visibleCount = 6;
        notifyDataSetChanged();
    }

    public void returnEntity(CardPickEntity x){
        int position = x.getId();
        mList.get(position).setVisible(true);
        visibleCount++;
        notifyItemChanged(position);
    }

    public CardPickEntity getItem(int position) {
        return mList.get(position);
    }

    int visibleCount = 6;

    class MyViewHolder extends RecyclerView.ViewHolder {
        EntityCardPickBinding binding;

        public MyViewHolder(@NonNull EntityCardPickBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(final int position) {
            final CardPickEntity x = getItem(position);
            boolean isVisible = x.isVisible();
            int visibility = isVisible ? View.VISIBLE : View.INVISIBLE;
            binding.ivPickCard.setVisibility(visibility);
            binding.ivPickCard.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(),x.getImg()));
            binding.ivPickCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (visibleCount > 3) {
                        entityListener.handleClickEvent(x);
                        binding.ivPickCard.setVisibility(View.INVISIBLE);
                        visibleCount--;
                    }
                }
            });
        }
    }
}
