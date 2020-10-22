package org.phantancy.fgocalc.entity;

import java.util.Map;

public abstract class ShortcutBuffEntity {
    protected String buffDes;
    protected Map<String,Double> buffMap;

    public abstract Map<String,Double> getBuffMap();

    public String getBuffDes() {
        return buffDes;
    }
}
