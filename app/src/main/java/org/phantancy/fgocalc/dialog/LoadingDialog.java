package org.phantancy.fgocalc.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatDialog;
import android.widget.ImageView;

import org.phantancy.fgocalc.R;

public class LoadingDialog extends AppCompatDialog {

    private ImageView ivLoading;
    private AnimationDrawable aDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.diag_loading);
        ivLoading = findViewById(R.id.dl_iv_loading);
        aDrawable = (AnimationDrawable)ivLoading.getDrawable();
        aDrawable.start();
    }

    public void stopAnim(){
        aDrawable.stop();
        dismiss();
    }

}
