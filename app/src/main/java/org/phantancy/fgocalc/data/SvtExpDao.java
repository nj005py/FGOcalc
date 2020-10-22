package org.phantancy.fgocalc.data;

import androidx.room.Dao;
import androidx.room.Query;

import org.phantancy.fgocalc.entity.SvtExpEntity;

import java.util.List;

@Dao
public interface SvtExpDao {
    @Query("SELECT b.* FROM servants a LEFT JOIN svt_exp b on a.exp_type = b.type WHERE a.id = :svtId ORDER BY b.lv")
    List<SvtExpEntity> getSvtExpList(int svtId);
}
