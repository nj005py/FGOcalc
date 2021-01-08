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

    @ColumnInfo(name = "np_lv1_updated")
    public double npLv1Updated;

    @ColumnInfo(name = "np_lv2_updated")
    public double npLv2Updated;

    @ColumnInfo(name = "np_lv3_updated")
    public double npLv3Updated;


    @ColumnInfo(name = "np_lv4_updated")
    public double npLv4Updated;

    @ColumnInfo(name = "np_lv5_updated")
    public double npLv5Updated;

    @ColumnInfo(name = "np_upgraded")
    public int npUpgraded;

    @ColumnInfo(name = "np_color")
    public String npColor;

    @ColumnInfo(name = "buff")
    public String npBuff;

    @ColumnInfo(name = "buff_updated")
    public String buffUpdated;

    protected NoblePhantasmEntity(Parcel in) {
        id = in.readInt();
        sid = in.readInt();
        npLv1 = in.readDouble();
        npLv2 = in.readDouble();
        npLv3 = in.readDouble();
        npLv4 = in.readDouble();
        npLv5 = in.readDouble();
        npLv1Updated = in.readDouble();
        npLv2Updated = in.readDouble();
        npLv3Updated = in.readDouble();
        npLv4Updated = in.readDouble();
        npLv5Updated = in.readDouble();
        npUpgraded = in.readInt();
        npColor = in.readString();
        npBuff = in.readString();
        buffUpdated = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(sid);
        dest.writeDouble(npLv1);
        dest.writeDouble(npLv2);
        dest.writeDouble(npLv3);
        dest.writeDouble(npLv4);
        dest.writeDouble(npLv5);
        dest.writeDouble(npLv1Updated);
        dest.writeDouble(npLv2Updated);
        dest.writeDouble(npLv3Updated);
        dest.writeDouble(npLv4Updated);
        dest.writeDouble(npLv5Updated);
        dest.writeInt(npUpgraded);
        dest.writeString(npColor);
        dest.writeString(npBuff);
        dest.writeString(buffUpdated);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
