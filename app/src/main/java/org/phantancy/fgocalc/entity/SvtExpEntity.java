package org.phantancy.fgocalc.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "svt_exp")
public class SvtExpEntity {
    @PrimaryKey
    @ColumnInfo
    public int id;

    @ColumnInfo
    public int curve;

    @ColumnInfo
    public int lv;

    @ColumnInfo
    public int type;

    @ColumnInfo
    public int exp;
}
