package org.phantancy.fgocalc.item;

import java.io.Serializable;

public class OptionItem implements Serializable {

    private String content;
    private String url;

    public OptionItem(String content, String url) {
        this.content = content;
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
