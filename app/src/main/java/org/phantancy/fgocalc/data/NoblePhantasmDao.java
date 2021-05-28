package org.phantancy.fgocalc.data;

import androidx.room.Dao;
import androidx.room.Query;

import org.phantancy.fgocalc.entity.NoblePhantasmEntity;

import java.util.List;

@Dao
public interface NoblePhantasmDao {
    @Query("select * from noble_phantasm where sid=:svtId")
    List<NoblePhantasmEntity> getNoblePhantasmList(int svtId);
}
