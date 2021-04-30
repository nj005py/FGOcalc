package org.phantancy.fgocalc.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.phantancy.fgocalc.entity.NoblePhantasmEntity;

import java.util.List;

public class NoblePhantasmRepository {
    private NoblePhantasmDao npDao;

    public NoblePhantasmRepository(Application app) {
        CalcDatabase calcDB = CalcDatabase.getDatabase(app);
        npDao = calcDB.noblePhantasmDao();
    }

    /**
     * 查询从者宝具数据
     * @param svtId
     * @return
     */
    public NoblePhantasmEntity getNoblePhantasmEntity(int svtId) {
        return npDao.getNoblePhantasmEntity(svtId);
    }

    public LiveData<List<NoblePhantasmEntity>> getNoblePhantasmEntities(int svtId) {
        return npDao.getNoblePhantasmEntities(svtId);
    }
}
