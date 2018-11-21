package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.item.UpdateItem;

/**
 * Created by HATTER on 2017/8/8.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private TextView tvCancel,tvDownload,tvUpdate;
    private WebView wvUpdate;
    private CheckBox cbIgnore;

    public UpdateDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_update);
        mContext = context;

        tvCancel = (TextView)findViewById(R.id.du_tv_cancel);
        tvDownload = (TextView)findViewById(R.id.du_tv_download);
        wvUpdate = findViewById(R.id.du_wv_update);
        cbIgnore = (CheckBox)findViewById(R.id.du_cb_ignore);
        tvUpdate = findViewById(R.id.du_tv_update);
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

    public void setUpdate(UpdateItem item){
        String url = "";
//        WebSettings settings = wvUpdate.getSettings();
//        settings.setUseWideViewPort(true);
//        wvUpdate.loadUrl(url);
        tvUpdate.setText(Html.fromHtml(item.getContent()));
    }

    public void setIgnoreListener(CompoundButton.OnCheckedChangeListener listener){
        cbIgnore.setOnCheckedChangeListener(listener);
    }
}
