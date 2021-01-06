package org.phantancy.fgocalc.fragment;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

public class LazyFrag extends Fragment {
    private boolean isVisible = false;

    @CallSuper
    protected  void onFirstVisible() {
        isVisible = true;
    }

    protected void onVisibilityChange(boolean isVisible) {
        throw new UnsupportedOperationException("method not overridden");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
