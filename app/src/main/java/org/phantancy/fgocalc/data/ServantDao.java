package org.phantancy.fgocalc.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import org.phantancy.fgocalc.entity.ServantEntity;

import java.util.List;

@Dao
public interface ServantDao {
    @Query("select * from servants")
    List<ServantEntity> getAllServants();

    @Query("select * from servants where name like '%' || :keyword || '%' or nickname like '%' || :keyword || '%'")
    List<ServantEntity> getServantsByKeyword(String keyword);

    @RawQuery(observedEntities = ServantEntity.class)
    List<ServantEntity> getServantsByFilter(SimpleSQLiteQuery query);
}
