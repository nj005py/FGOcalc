package org.phantancy.fgocalc.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.bumptech.glide.Glide;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.CharacterOptionAdapter;
import org.phantancy.fgocalc.databinding.DialogCharacterBinding;
import org.phantancy.fgocalc.entity.CharacterEntity;
import org.phantancy.fgocalc.util.DisplayUtil;
import org.phantancy.fgocalc.view.OptionDecoration;


//立绘弹窗
public class CharacterDialog extends AppCompatDialog {

    private Context context;
    private DialogCharacterBinding binding;

    public CharacterDialog(Context context) {
        super(context, R.style.dialog);
        binding = DialogCharacterBinding.inflate(getLayoutInflater(),null,false);
        this.context = context;
        Window window = this.getWindow();
        window.setContentView(binding.getRoot());
        window.setLayout((int) (DisplayUtil.INSTANCE.getWidth() * 1), (int) (DisplayUtil.INSTANCE.getHeight() * 0.8));
    }

    public void setEntity(CharacterEntity entity) {
        if (!TextUtils.isEmpty(entity.content)) {
            binding.tvTip.setText(entity.content);
            binding.svTip.setVisibility(View.VISIBLE);
            binding.vBg.setVisibility(View.VISIBLE);
            binding.tvTip.setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
        }
        if (entity.img != null) {
            binding.ivCharacter.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(entity.img)
                    .placeholder(R.drawable.altria_a)
                    .into(binding.ivCharacter);

            binding.ivCharacter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (entity.options != null) {
            CharacterOptionAdapter adapter = new CharacterOptionAdapter();
            binding.rvOptions.setAdapter(adapter);
            OptionDecoration decoration = new OptionDecoration(20);
            binding.rvOptions.addItemDecoration(decoration);
            adapter.submitList(entity.options);
            binding.rvOptions.setVisibility(View.VISIBLE);
        }
    }
}