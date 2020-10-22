package org.phantancy.fgocalc.entity;

import android.graphics.drawable.Drawable;

public class InfoEntity {
    //标题
    private String title;
    //数据
    private String data;

    public InfoEntity(String title, String data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
