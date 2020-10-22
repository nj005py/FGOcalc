package org.phantancy.fgocalc.entity;

import java.util.Map;
//implements IShortcutBuff
public class ShortcutMstEquipmentEntity extends ShortcutBuffEntity {
    private int avatar1;
    private int avatar2;
//    private String buffDes;
//    private Map<String,Double> buffMap;

    public ShortcutMstEquipmentEntity(int avatar1, int avatar2, String buffDes) {
        this.avatar1 = avatar1;
        this.avatar2 = avatar2;
        this.buffDes = buffDes;
    }

    public ShortcutMstEquipmentEntity(int avatar1, int avatar2, String buffDes, Map<String, Double> buffMap) {
        this.avatar1 = avatar1;
        this.avatar2 = avatar2;
        this.buffDes = buffDes;
        this.buffMap = buffMap;
    }

    public int getAvatar1() {
        return avatar1;
    }

    public void setAvatar1(int avatar1) {
        this.avatar1 = avatar1;
    }

    public int getAvatar2() {
        return avatar2;
    }

    public void setAvatar2(int avatar2) {
        this.avatar2 = avatar2;
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
