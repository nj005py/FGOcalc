package org.phantancy.fgocalc.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.phantancy.fgocalc.adapter.ItemAdapter;
import org.phantancy.fgocalc.item.Item;


/**
 * Created by PY on 2016/12/1.
 */
public class zItemView extends RelativeLayout implements ItemView{


    public zItemView(Context context) {
        super(context);
    }

    public zItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void findViewsByIds() {

    }

    @Override
    public void setObject(Item item, int position, ItemAdapter.OnViewClickListener onViewClickListener) {
        if (item instanceof Object) {

        }
    }
}
