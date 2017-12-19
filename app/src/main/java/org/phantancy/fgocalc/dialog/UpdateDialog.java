package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.phantancy.fgocalc.R;

/**
 * Created by HATTER on 2017/8/8.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private TextView tvCancel,tvDownload,tvUpdate;
    private CheckBox cbIgnore;

    public UpdateDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_update);
        mContext = context;

        tvCancel = (TextView)findViewById(R.id.du_tv_cancel);
        tvDownload = (TextView)findViewById(R.id.du_tv_download);
        tvUpdate = (TextView)findViewById(R.id.du_tv_update);
        cbIgnore = (CheckBox)findViewById(R.id.du_cb_ignore);
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

    public void setUpdate(String str){
        tvUpdate.setText(str);
    }

    public void setIgnoreListener(CompoundButton.OnCheckedChangeListener listener){
        cbIgnore.setOnCheckedChangeListener(listener);
    }
}
