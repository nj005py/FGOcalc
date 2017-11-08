package com.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phantancy.fgocalc.R;

import butterknife.ButterKnife;

/**
 * Created by PY on 2017/3/3.
 */
public class ZFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_np, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = getActivity();
        init();
    }

    private void init() {
        setListener();
    }

    private void setListener(){

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
