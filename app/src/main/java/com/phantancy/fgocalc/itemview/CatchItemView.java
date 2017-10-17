package com.phantancy.fgocalc.itemview;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.adapter.ItemAdapter;
import com.phantancy.fgocalc.item.CardsItem;
import com.phantancy.fgocalc.item.CatchItem;
import com.phantancy.fgocalc.item.Item;


/**
 * Created by PY on 2016/12/1.
 */
public class CatchItemView extends RelativeLayout implements ItemView{
    private ImageView ivCatch;
    private ImageView ivStar;


    public CatchItemView(Context context) {
        super(context);
    }

    public CatchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void findViewsByIds() {
        ivCatch = (ImageView)findViewById(R.id.ic_iv_catch);
        ivStar = (ImageView)findViewById(R.id.ic_iv_star);
    }

    @Override
    public void setObject(Item item, int position, ItemAdapter.OnViewClickListener onViewClickListener) {
        if (item instanceof CatchItem) {
            if (item != null) {
                CatchItem cItem = (CatchItem)item;
                ivCatch.setImageResource(cItem.getImgResource());
                ivStar.setImageResource(cItem.getStarResource());
            }
        }
    }
}
