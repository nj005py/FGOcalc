package org.phantancy.fgocalc.item;


import org.phantancy.fgocalc.R;

/**
 * Created by PY on 2016/12/1.
 */
public class CatchItem extends Item{

    private int layoutId = R.layout.item_catch;
    private int imgResource;
    private int starResource;

    public void setLayoutId(int id){
        layoutId = id;
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public int getStarResource() {
        return starResource;
    }

    public void setStarResource(int starResource) {
        this.starResource = starResource;
    }
}
