package org.phantancy.fgocalc.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.databinding.ItemOptionBinding;
import org.phantancy.fgocalc.entity.CharacterEntity;


public class CharacterOptionAdapter extends ListAdapter<CharacterEntity.OptionEntity, CharacterOptionAdapter.ViewHolder> {
    static DiffUtil.ItemCallback<CharacterEntity.OptionEntity> diffCallback = new DiffUtil.ItemCallback<CharacterEntity.OptionEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CharacterEntity.OptionEntity o, @NonNull CharacterEntity.OptionEntity n) {
            return o == n;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CharacterEntity.OptionEntity o, @NonNull CharacterEntity.OptionEntity n) {
            return o.text.equals(n.text);
        }
    };


    public CharacterOptionAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemOptionBinding binding = ItemOptionBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(getItem(i));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemOptionBinding binding;

        public ViewHolder(@NonNull ItemOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final CharacterEntity.OptionEntity en) {
            if (!TextUtils.isEmpty(en.text)) {
                binding.ioTvOption.setText(en.text);
            }
            if (en.characterInterface != null) {
                binding.ioTvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        en.characterInterface.onClick();
                    }
                });
            }
        }
    }

}