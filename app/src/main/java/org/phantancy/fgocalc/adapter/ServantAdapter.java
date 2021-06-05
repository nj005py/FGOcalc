package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.activity.CalcActy;
import org.phantancy.fgocalc.databinding.EntityServantBinding;
import org.phantancy.fgocalc.entity.ServantEntity;

public class ServantAdapter extends ListAdapter<ServantEntity, ServantAdapter.ViewHolder> {
    static DiffUtil.ItemCallback<ServantEntity> diff = new DiffUtil.ItemCallback<ServantEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ServantEntity oldItem, @NonNull ServantEntity newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ServantEntity oldItem, @NonNull ServantEntity newItem) {
            return oldItem.name.equals(newItem.name);
        }
    };

    public ServantAdapter() {
        super(diff);
    }

    private IServantClickListener servantClickListener;

    public void setIServantClickListener(IServantClickListener x) {
        this.servantClickListener = x;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EntityServantBinding binding;

        public ViewHolder(@NonNull EntityServantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(ServantEntity x) {
            if (x.avatarRes != -1) {
                Glide.with(binding.getRoot().getContext())
                        .load(x.avatarRes)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivAvatar);
            } else {
                Glide.with(binding.getRoot().getContext())
                        .load(x.avatarUrl)
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(binding.ivAvatar);
            }

            binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    servantClickListener.openCalcPage(x,binding.ivAvatar);
                }
            });
        }
    }

    @NonNull
    @Override
    public ServantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(EntityServantBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ServantAdapter.ViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }

    public interface IServantClickListener {
        void openCalcPage(ServantEntity x, ImageView y);
    }

}
