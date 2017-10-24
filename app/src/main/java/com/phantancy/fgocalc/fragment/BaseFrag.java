package com.phantancy.fgocalc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.phantancy.fgocalc.item.Card;
import com.phantancy.fgocalc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2017/2/7.
 */
public class BaseFrag extends Fragment implements View.OnClickListener{

    public String TAG = getClass().getSimpleName();
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    //spinner绑定数据源,simple样式限定
    public void spInitSimple(Context context,String[] str,Spinner sp){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_simple_spinner,str);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
        sp.setAdapter(spAdapter);
    }

    //spinner绑定数据源,simple样式限定
    public void spInitSimple(Context context,int[] i,Spinner sp){
        List<Integer> lv = new ArrayList<>();
        for(int j = 0;j < i.length;j++){
            lv.add(i[j]);
        }
        ArrayAdapter<Integer> spAdapter = new ArrayAdapter<Integer>(context,R.layout.item_simple_spinner,lv);
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
        sp.setAdapter(spAdapter);
    }

    //spinner绑定数据源,特供版
    public void spInitSpecial(Context context,String[] str,Spinner sp){
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context,R.layout.item_simple_spinner,str){
            @Override
            public int getCount() {
                return super.getCount() - 1;
            }
        };
        spAdapter.setDropDownViewResource(R.layout.item_content_spinner);
        sp.setAdapter(spAdapter);
        sp.setSelection(spAdapter.getCount());
    }

    @Override
    public void onClick(View v) {

    }


}
