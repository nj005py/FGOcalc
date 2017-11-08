package com.phantancy.fgocalc.item;

import java.io.Serializable;

/**
 * Created by HATTER on 2017/11/5.
 * 单个buff的模型
 */

public class BuffItem implements Serializable {
    private int type;
    private int img;
    private boolean ifPercent;
    private int defaultInt;
    private double defaultDouble;
    private String hint;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getDefaultInt() {
        return defaultInt;
    }

    public void setDefaultInt(int defaultInt) {
        this.defaultInt = defaultInt;
    }

    public double getDefaultDouble() {
        return defaultDouble;
    }

    public void setDefaultDouble(double defaultDouble) {
        this.defaultDouble = defaultDouble;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isIfPercent() {
        return ifPercent;
    }

    public void setIfPercent(boolean ifPercent) {
        this.ifPercent = ifPercent;
    }
}
