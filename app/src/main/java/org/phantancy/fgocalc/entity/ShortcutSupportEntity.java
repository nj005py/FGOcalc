package org.phantancy.fgocalc.entity;

import java.util.Map;
//implements IShortcutBuff
public class ShortcutSupportEntity extends ShortcutBuffEntity {
    private int avatar;
//    private String buffDes;
//    private Map<String,Double> buffMap;

    public ShortcutSupportEntity(int avatar, String buffDes) {
        this.avatar = avatar;
        this.buffDes = buffDes;
    }

    public ShortcutSupportEntity(int avatar, String buffDes, Map<String, Double> buffMap) {
        this.avatar = avatar;
        this.buffDes = buffDes;
        this.buffMap = buffMap;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getBuffDes() {
        return buffDes;
    }

    public void setBuffDes(String buffDes) {
        this.buffDes = buffDes;
    }

    @Override
    public Map<String, Double> getBuffMap() {
        return buffMap;
    }

    public void setBuffMap(Map<String, Double> buffMap) {
        this.buffMap = buffMap;
    }
}
