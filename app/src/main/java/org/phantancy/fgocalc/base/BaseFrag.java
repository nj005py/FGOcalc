package org.phantancy.fgocalc.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.phantancy.fgocalc.dialog.LoadingDialog;

/**
 * Created by PY on 2017/2/7.
 */
public class BaseFrag extends Fragment{

    public String TAG = getClass().getSimpleName();
    protected Context ctx;
    protected Activity mActy;
    protected View rootView;
    protected LoadingDialog loadingDialog;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActy.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
