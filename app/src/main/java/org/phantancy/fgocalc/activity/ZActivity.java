package org.phantancy.fgocalc.activity;

import android.os.Bundle;
import android.view.View;

import org.phantancy.fgocalc.R;


/**
 * Created by PY on 2016/10/31.
 */
public class ZActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_layout);
        ctx = this;
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
