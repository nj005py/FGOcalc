package com.phantancy.fgocalc.item;


import com.phantancy.fgocalc.R;

import java.io.Serializable;

/**
 * Created by PY on 2016/12/1.
 */
public class InfoCardsMVPItem implements Serializable{

    private String type;

    public InfoCardsMVPItem(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
