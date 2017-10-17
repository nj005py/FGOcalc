package com.phantancy.fgocalc.item;

import java.io.Serializable;

/**
 * Created by PY on 2017/7/18.
 */
public class MenuItem implements Serializable{
    private String menuName;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
