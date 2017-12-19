package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.phantancy.fgocalc.R;

/**
 * Created by HATTER on 2017/8/8.
 */

public class MenulLocDialog extends Dialog{
    private Context mContext;
    private RadioGroup rg;
    private RadioButton rbL,rbR;
    private LinearLayout ll;

    public MenulLocDialog(@NonNull Context context) {
        super(context,R.style.dialog);
        setContentView(R.layout.diag_menu_loc);
        mContext = context;

        rg = (RadioGroup)findViewById(R.id.dml_rg_menu);
        rbL = (RadioButton)findViewById(R.id.dml_rb_left);
        rbR = (RadioButton)findViewById(R.id.dml_rb_right);
        ll = (LinearLayout)findViewById(R.id.dml_ll_menu);
    }

    public void setLeftListener(View.OnClickListener onClickListener){
        rbL.setOnClickListener(onClickListener);
    }

    public void setRightListener(View.OnClickListener onClickListener){
        rbR.setOnClickListener(onClickListener);
    }

    public void setCheck(boolean locLeft){
        if (locLeft) {
            rbL.setChecked(true);
        }else{
            rbR.setChecked(true);
        }
    }

}
