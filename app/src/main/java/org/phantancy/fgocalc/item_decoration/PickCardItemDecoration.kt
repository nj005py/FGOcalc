package org.phantancy.fgocalc.item_decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.phantancy.fgocalc.util.UnitUtil
import kotlin.properties.Delegates

class PickCardItemDecoration(val context:Context, val space:Float): RecyclerView.ItemDecoration(){
    private var _space by Delegates.notNull<Int>()

    init {
        _space = UnitUtil.dip2px(context,space)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) > 5) {
            outRect.top = _space
        }
    }
}