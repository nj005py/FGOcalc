package com.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.phantancy.fgocalc.R;

/**
 * Created by HATTER on 2017/8/8.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private TextView tvCancel,tvDownload;

    public UpdateDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_update);
        mContext = context;

        tvCancel = (TextView)findViewById(R.id.du_tv_cancel);
        tvDownload = (TextView)findViewById(R.id.du_tv_download);
        tvCancel.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.du_tv_cancel:
                dismiss();
                break;
        }
    }

    public void setDownloadListener(View.OnClickListener onClickListener){
        tvDownload.setOnClickListener(onClickListener);
    }
}
