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

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.entity.CharacterEntity;
import org.phantancy.fgocalc.view.OptionDecoration;


//立绘弹窗
public class CharacterDialog extends AppCompatDialog {

    private Context context;

    public CharacterDialog(Context context) {
        super(context, R.style.dialog);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_character, null, false);
        Window window = this.getWindow();
        window.setContentView(v);
        this.context = context;
    }

    public void setEntity(CharacterEntity entity) {
//        if (!TextUtils.isEmpty(entity.content)) {
//            tvTip.setText(entity.content);
//            svTip.setVisibility(View.VISIBLE);
//            vBg.setVisibility(View.VISIBLE);
//            tvTip.setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
//        }
//        if (entity.img != null) {
//            ivCharacter.setVisibility(View.VISIBLE);
//
//            GlideApp.with(context)
//                    .load(entity.img)
//                    .placeholder(R.drawable.altria_a)
//                    .into(ivCharacter);
//
//            ivCharacter.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
//        }
//        if (entity.options != null) {
//            CharacterOptionAdapter adapter = new CharacterOptionAdapter();
//            rvOptions.setAdapter(adapter);
//            OptionDecoration decoration = new OptionDecoration(20);
//            rvOptions.addItemDecoration(decoration);
//            adapter.submitList(entity.options);
//            rvOptions.setVisibility(View.VISIBLE);
//        }
    }
}