package org.phantancy.fgocalc.entity;

import java.io.Serializable;

public class OptionItem implements Serializable {

    private String option;
    private String url;

    public OptionItem(String content, String url) {
        this.option = content;
        this.url = url;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
