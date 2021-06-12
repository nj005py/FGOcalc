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
    @ColumnInfo(name = "oc_buff_lv1")
    public String ocBuffLv1;

    @ColumnInfo(name = "oc_buff_lv2")
    public String ocBuffLv2;

    @ColumnInfo(name = "oc_buff_lv3")
    public String ocBuffLv3;

    @ColumnInfo(name = "oc_buff_lv4")
    public String ocBuffLv4;

    @ColumnInfo(name = "oc_buff_lv5")
    public String ocBuffLv5;

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
        ocBuffLv1 = in.readString();
        ocBuffLv2 = in.readString();
        ocBuffLv3 = in.readString();
        ocBuffLv4 = in.readString();
        ocBuffLv5 = in.readString();
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
        dest.writeString(ocBuffLv1);
        dest.writeString(ocBuffLv2);
        dest.writeString(ocBuffLv3);
        dest.writeString(ocBuffLv4);
        dest.writeString(ocBuffLv5);
        dest.writeString(oc_buff);
        dest.writeString(npDes);
    }
}
