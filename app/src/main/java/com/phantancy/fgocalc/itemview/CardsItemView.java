package com.phantancy.fgocalc.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.adapter.ItemAdapter;
import com.phantancy.fgocalc.item.CardsItem;
import com.phantancy.fgocalc.item.Item;


/**
 * Created by PY on 2016/12/1.
 */
public class CardsItemView extends RelativeLayout implements ItemView{
    private ImageView ivCards;


    public CardsItemView(Context context) {
        super(context);
    }

    public CardsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void findViewsByIds() {
        ivCards = (ImageView)findViewById(R.id.iic_iv_cards);
    }

    @Override
    public void setObject(Item item, int position, ItemAdapter.OnViewClickListener onViewClickListener) {
        if (item instanceof CardsItem) {
            CardsItem cItem = (CardsItem)item;
            String type = cItem.getType();
            switch (type) {
                case "b":
                    ivCards.setImageResource(R.mipmap.buster);
                    break;
                case "a":
                    ivCards.setImageResource(R.mipmap.arts);
                    break;
                case "q":
                    ivCards.setImageResource(R.mipmap.quick);
                    break;
            }
        }
    }
}
