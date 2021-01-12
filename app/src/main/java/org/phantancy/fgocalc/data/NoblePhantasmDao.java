package org.phantancy.fgocalc.data;

import androidx.room.Dao;
import androidx.room.Query;

import org.phantancy.fgocalc.entity.NoblePhantasmEntity;

import java.util.List;

@Dao
public interface NoblePhantasmDao {
    @Query("SELECT * FROM noble_phantasm WHERE sid=:sid")
    NoblePhantasmEntity getNoblePhantasmEntity(int sid);
}
