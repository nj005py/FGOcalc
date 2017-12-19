package org.phantancy.fgocalc.item;


import org.phantancy.fgocalc.R;

/**
 * Created by PY on 2016/12/1.
 */
public class zItem extends Item{

    private int layoutId = R.layout.z_layout;

    public void setLayoutId(int id){
        layoutId = id;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }
}
