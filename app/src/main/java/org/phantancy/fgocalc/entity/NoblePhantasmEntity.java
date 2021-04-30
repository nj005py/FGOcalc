package org.phantancy.fgocalc.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "noble_phantasm")
public class NoblePhantasmEntity implements Parcelable {
    //宝具id
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;
    //从者id
    @ColumnInfo(name = "sid")
    public int sid;

    //宝具版本
    @ColumnInfo(name = "np_version")
    public int npVersion;

    //宝具卡色
    @ColumnInfo(name = "np_color")
    public String npColor;

    //宝具倍率随宝具等级
    @ColumnInfo(name = "np_lv1")
    public double npLv1;

    @ColumnInfo(name = "np_lv2")
    public double npLv2;

    @ColumnInfo(name = "np_lv3")
    public double npLv3;

    @ColumnInfo(name = "np_lv4")
    public double npLv4;

    @ColumnInfo(name = "np_lv5")
    public double npLv5;

    //宝具buff随宝具等级
    @ColumnInfo(name = "buff_lv1")
    public String buffLv1;

    @ColumnInfo(name = "buff_lv2")
    public String buffLv2;

    @ColumnInfo(name = "buff_lv3")
    public String buffLv3;

    @ColumnInfo(name = "buff_lv4")
    public String buffLv4;

    @ColumnInfo(name = "buff_lv5")
    public String buffLv5;

    //宝具buff随oc
    @ColumnInfo(name = "oc_buff")
    public String oc_buff;

    //宝具描述
    @ColumnInfo(name = "np_des")
    public String npDes;

    public NoblePhantasmEntity() {
    }

    protected NoblePhantasmEntity(Parcel in) {
        id = in.readInt();
        sid = in.readInt();
        npVersion = in.readInt();
        npColor = in.readString();
        npLv1 = in.readDouble();
        npLv2 = in.readDouble();
        npLv3 = in.readDouble();
        npLv4 = in.readDouble();
        npLv5 = in.readDouble();
        buffLv1 = in.readString();
        buffLv2 = in.readString();
        buffLv3 = in.readString();
        buffLv4 = in.readString();
        buffLv5 = in.readString();
        oc_buff = in.readString();
        npDes = in.readString();
    }

    public static final Creator<NoblePhantasmEntity> CREATOR = new Creator<NoblePhantasmEntity>() {
        @Override
        public NoblePhantasmEntity createFromParcel(Parcel in) {
            return new NoblePhantasmEntity(in);
        }

        @Override
        public NoblePhantasmEntity[] newArray(int size) {
            return new NoblePhantasmEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(sid);
        dest.writeInt(npVersion);
        dest.writeString(npColor);
        dest.writeDouble(npLv1);
        dest.writeDouble(npLv2);
        dest.writeDouble(npLv3);
        dest.writeDouble(npLv4);
        dest.writeDouble(npLv5);
        dest.writeString(buffLv1);
        dest.writeString(buffLv2);
        dest.writeString(buffLv3);
        dest.writeString(buffLv4);
        dest.writeString(buffLv5);
        dest.writeString(oc_buff);
        dest.writeString(npDes);
    }
}
