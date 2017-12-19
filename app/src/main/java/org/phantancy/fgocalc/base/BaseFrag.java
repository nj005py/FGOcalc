package org.phantancy.fgocalc.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PY on 2017/2/7.
 */
public class BaseFrag extends Fragment{

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

}
