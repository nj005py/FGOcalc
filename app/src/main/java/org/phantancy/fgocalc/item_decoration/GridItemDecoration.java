package org.phantancy.fgocalc.item_decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by HATTER on 2017/11/3.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration{
    private int spaceHeight;
    private int spaceWidth;

    public GridItemDecoration(@NonNull Context ctx,int width, int height) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        spaceWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,width,dm);
        spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,height,dm);
        Log.d(getClass().getSimpleName(),"spaceHeight:" + spaceHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //insert space below all items
        // 在所有模型下方添加间距
        outRect.right =spaceWidth;
        outRect.left = spaceWidth;
        outRect.bottom = spaceHeight;
        //if you don't want to insert space below the last item
        //如果不想在最后的模型下添加间距
//        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
//            outRect.bottom = height;
//        }
    }
}
