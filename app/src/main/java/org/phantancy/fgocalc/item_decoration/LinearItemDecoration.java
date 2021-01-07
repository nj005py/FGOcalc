package org.phantancy.fgocalc.item_decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by HATTER on 2017/11/3.
 * The version 25.0.0 of Android Support Library introduced DividerItemDecoration class:

 DividerItemDecoration is a RecyclerView.ItemDecoration that can be used as a divider between items of a LinearLayoutManager. It supports both HORIZONTAL and VERTICAL orientations.
 Usage:

 DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
 layoutManager.getOrientation());
 recyclerView.addItemDecoration(dividerItemDecoration);
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    int imgWidth = 0;

    public LinearItemDecoration(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int delta = (parent.getWidth() - (imgWidth * 3)) / 6;
        outRect.set(delta, 0, delta, 0);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
