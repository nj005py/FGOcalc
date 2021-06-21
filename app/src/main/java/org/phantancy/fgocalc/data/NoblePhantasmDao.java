package org.phantancy.fgocalc.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import org.phantancy.fgocalc.entity.NoblePhantasmEntity;

import java.util.List;

@Dao
public interface NoblePhantasmDao {
    @Query("SELECT * FROM noble_phantasm WHERE sid=:sid")
    public LiveData<List<NoblePhantasmEntity>> getNoblePhantasmEntities(int sid);

    @Query("SELECT * FROM noble_phantasm WHERE sid=:sid")
    public List<NoblePhantasmEntity> getNoblePhantasmEntitiesList(int sid);
}
