package com.phantancy.fgocalc.activity;

import android.os.Bundle;
import android.view.View;

import com.phantancy.fgocalc.R;


/**
 * Created by PY on 2016/10/31.
 */
public class ZActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_layout);
        mContext = this;
        init();
    }

    private void init(){
        setListener();
    }

    private void setListener(){

    }

    @Override
    public void onClick(View v) {

    }
}
