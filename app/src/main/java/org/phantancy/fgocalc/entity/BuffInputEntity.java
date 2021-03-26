package org.phantancy.fgocalc.entity;

public class BuffInputEntity {
    //buff图标
    private int icon;
    //buff名称
    private String key;
    //buff实际值
    private double value;
    //buff演示值
    private String valueDisplay;
    //buff类型 0:整数 1:百分号数 2:分类
    private int type;

    //buff输入框
    public BuffInputEntity(int icon, String key, double value, int type) {
        this.icon = icon;
        this.key = key;
        this.value = value;
        this.type = type;
    }

    //buff分类提示
    public BuffInputEntity(String key, int type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        if (value == 0) {
            valueDisplay = "";
        } else {
            valueDisplay = String.valueOf(value);
            //判断是否是百分数
            if (type == 1) {
                this.value = value / 100;
            }
        }
    }

    public String getValueDisplay() {
        return valueDisplay;
    }

    public void setValueDisplay(String valueDisplay) {
        this.valueDisplay = valueDisplay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
