package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.entity.IBuffShortCut;
import org.phantancy.fgocalc.databinding.EntityShortcutSupportBinding;
import org.phantancy.fgocalc.entity.ShortcutSupportEntity;

public class ShortcutSupportAdapter extends ListAdapter<ShortcutSupportEntity, ShortcutSupportAdapter.MyViewHolder> {

    static DiffUtil.ItemCallback<ShortcutSupportEntity> diffCallback = new DiffUtil.ItemCallback<ShortcutSupportEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShortcutSupportEntity oldItem, @NonNull ShortcutSupportEntity newItem) {
            return oldItem.getAvatar() == newItem.getAvatar();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShortcutSupportEntity oldItem, @NonNull ShortcutSupportEntity newItem) {
            return oldItem.getBuffDes().equals(newItem.getBuffDes());
        }
    };

    public ShortcutSupportAdapter(IBuffShortCut buffShortCut) {
        super(diffCallback);
        this.buffShortCut = buffShortCut;
    }
    private IBuffShortCut buffShortCut;


    @NonNull
    @Override
    public ShortcutSupportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context ctx = parent.getContext();
        EntityShortcutSupportBinding x = EntityShortcutSupportBinding.inflate(LayoutInflater.from(ctx),parent,false);
        return new MyViewHolder(x);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutSupportAdapter.MyViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        EntityShortcutSupportBinding binding;

        public MyViewHolder(EntityShortcutSupportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(ShortcutSupportEntity x){
            Glide.with(binding.ivSupportAvatar)
                    .load(x.getAvatar())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.ivSupportAvatar);

            binding.tvSupportBuffDes.setText(x.getBuffDes());
            binding.tvSupportBuffDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buffShortCut.addBuffs(x);
                }
            });
            binding.btnReduceSupportBuff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buffShortCut.reduceBuffs(x);
                }
            });
        }
    }

}
