package com.phantancy.fgocalc.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by PY on 2017/2/7.
 */
public class BaseFragment extends Fragment implements View.OnClickListener{

    public String TAG = getClass().getSimpleName();
    protected Context ctx;
    protected Activity mActy;
    protected View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActy = activity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }
    }

    /**
     * 写这个方法不是为了装逼，而是为了方便设置颜色
     * */
    public int getColorName(int colorName){
        return getResources().getColor(colorName);
    }

    //只是获取输入框的值
    public String etValue(EditText et){
        String value = et.getText().toString().trim();
        if (notEmpty(value)) {
            return value;
        }else {
            return null;
        }
    }

    public double etPercent(EditText et){
        double value = 0;
        if (notEmpty(etValue(et))) {
            try{
                value = Double.valueOf(etValue(et));
                return value / 100;
            }catch (Exception e){
                return 0;
            }
        }else{
            return 0;
        }
    }

    //只是获取输入框的值int
    public int etInt(EditText et){
        if (notEmpty(etValue(et))) {
            try{
                int value = Integer.parseInt(et.getText().toString().trim());
                return value;
            }catch (Exception e){
                return 0;
            }
        }else{
            return 0;
        }
    }

    //为输入框设置值
    public void etSet(EditText et,String s){
        if (notEmpty(s)) {
            et.setText(s);
        }
    }

    //批量重置EditText
    public void etReset(EditText[] ets){
        for(int k = 0;k < ets.length;k ++){
            ets[k].setText("");
        }
    }

    //判断不为null不为空
    public Boolean notEmpty(String s){
        if (s != null && !s.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }

    public void tvSet(TextView tv,String str){
        if (notEmpty(str)) {
            tv.setText(str);
        }else{
            tv.setText("");
        }
    }

    @Override
    public void onClick(View v) {

    }


}
