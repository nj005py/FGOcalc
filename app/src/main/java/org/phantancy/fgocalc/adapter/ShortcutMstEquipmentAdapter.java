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
import org.phantancy.fgocalc.databinding.EntityShortcutMstEquipmentBinding;
import org.phantancy.fgocalc.entity.ShortcutMstEquipmentEntity;

public class ShortcutMstEquipmentAdapter extends ListAdapter<ShortcutMstEquipmentEntity, ShortcutMstEquipmentAdapter.MyViewHolder> {

    static DiffUtil.ItemCallback<ShortcutMstEquipmentEntity> diffCallback = new DiffUtil.ItemCallback<ShortcutMstEquipmentEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShortcutMstEquipmentEntity oldItem, @NonNull ShortcutMstEquipmentEntity newItem) {
            return oldItem.getAvatar1() == newItem.getAvatar1();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShortcutMstEquipmentEntity oldItem, @NonNull ShortcutMstEquipmentEntity newItem) {
            return oldItem.getBuffDes().equals(newItem.getBuffDes());
        }
    };

    private IBuffShortCut buffShortCut;

    public ShortcutMstEquipmentAdapter(IBuffShortCut buffShortCut) {
        super(diffCallback);
        this.buffShortCut = buffShortCut;
    }

    @NonNull
    @Override
    public ShortcutMstEquipmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context ctx = parent.getContext();
        EntityShortcutMstEquipmentBinding x = EntityShortcutMstEquipmentBinding.inflate(LayoutInflater.from(ctx),parent,false);
        return new MyViewHolder(x);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutMstEquipmentAdapter.MyViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        EntityShortcutMstEquipmentBinding binding;

        public MyViewHolder(EntityShortcutMstEquipmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(ShortcutMstEquipmentEntity x){
            Glide.with(binding.ivMstA)
                    .load(x.getAvatar1())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.ivMstA);

            Glide.with(binding.ivMstB)
                    .load(x.getAvatar2())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.ivMstB);

            binding.tvMstDes.setText(x.getBuffDes());
            //添加buff
            binding.tvMstDes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buffShortCut.addBuffs(x);
                }
            });
            //删除buff
            binding.btnReduceMstBuff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buffShortCut.reduceBuffs(x);
                }
            });
        }
    }

}
