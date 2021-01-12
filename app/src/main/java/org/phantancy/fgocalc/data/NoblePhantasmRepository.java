package org.phantancy.fgocalc.data;

import android.app.Application;

import org.phantancy.fgocalc.entity.NoblePhantasmEntity;

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
}
