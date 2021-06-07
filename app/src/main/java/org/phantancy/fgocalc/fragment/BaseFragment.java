package org.phantancy.fgocalc.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.phantancy.fgocalc.dialog.LoadingDialog;


/**
 * Created by PY on 2017/2/7.new
 */
public class BaseFragment extends Fragment {

    final protected String TAG = getClass().getSimpleName();
    protected Context ctx;
    protected FragmentActivity mActy;
    protected View rootView;
    protected LoadingDialog loadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        mActy = (FragmentActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ctx = activity;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup)rootView.getParent()).removeView(rootView);
        }
    }

    protected void showLoading(){
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ctx);
        }
        loadingDialog.show();
    }

    protected void stopLoading(){
        if (loadingDialog != null) {
            loadingDialog.stopAnim();
        }
    }
}
