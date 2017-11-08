package com.phantancy.fgocalc.item;


import com.phantancy.fgocalc.R;

/**
 * Created by PY on 2016/12/1.
 */
public class CardsItem extends Item{

    private int layoutId = R.layout.item_info_cards;
    private String type;

    public CardsItem(String type) {
        this.type = type;
    }

    public void setLayoutId(int id){
        layoutId = id;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
