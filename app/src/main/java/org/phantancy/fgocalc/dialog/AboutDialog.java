package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ToolCase;

/**
 * Created by HATTER on 2017/8/8.
 */

public class AboutDialog extends Dialog{
    private Context mContext;
    private TextView tvAbout;

    public AboutDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_about);
        mContext = context;
        tvAbout = (TextView)findViewById(R.id.da_tv_about);
    }

    public void setAbout(String value){
        ToolCase.setViewValue(tvAbout,value);
    }

}
