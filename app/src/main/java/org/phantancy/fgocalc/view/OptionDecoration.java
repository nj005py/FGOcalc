package org.phantancy.fgocalc.view;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class OptionDecoration extends RecyclerView.ItemDecoration {

    private int divider;

    public OptionDecoration(int divider) {
        this.divider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = divider;
        outRect.top = divider;
    }
}
