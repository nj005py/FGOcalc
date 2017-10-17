package com.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.phantancy.fgocalc.R;

/**
 * Created by HATTER on 2017/8/8.
 */

public class AboutDialog extends Dialog{
    private Context mContext;

    public AboutDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_about);
        mContext = context;
    }

}
