package org.phantancy.fgocalc.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BuffInputEntity implements Parcelable {
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


    protected BuffInputEntity(Parcel in) {
        icon = in.readInt();
        key = in.readString();
        value = in.readDouble();
        valueDisplay = in.readString();
        type = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(icon);
        dest.writeString(key);
        dest.writeDouble(value);
        dest.writeString(valueDisplay);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BuffInputEntity> CREATOR = new Creator<BuffInputEntity>() {
        @Override
        public BuffInputEntity createFromParcel(Parcel in) {
            return new BuffInputEntity(in);
        }

        @Override
        public BuffInputEntity[] newArray(int size) {
            return new BuffInputEntity[size];
        }
    };

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
