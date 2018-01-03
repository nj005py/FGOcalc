package org.phantancy.fgocalc.item;

import java.io.Serializable;

/**
 * Created by HATTER on 2017/12/19.
 */

public class UpdateItem implements Serializable {
    private String version;
    private String path;
    private String update;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
