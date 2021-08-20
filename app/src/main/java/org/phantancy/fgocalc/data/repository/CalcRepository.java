package org.phantancy.fgocalc.data.repository;

import android.app.Application;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.phantancy.fgocalc.data.CalcDatabase;
import org.phantancy.fgocalc.data.InfoBuilder;
import org.phantancy.fgocalc.data.dao.ServantDao;
import org.phantancy.fgocalc.data.dao.SvtExpDao;
import org.phantancy.fgocalc.entity.FilterEntity;
import org.phantancy.fgocalc.entity.InfoEntity;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.entity.SvtExpEntity;
import org.phantancy.fgocalc.util.ToolCase;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class CalcRepository {

    private final String TAG = "CalcRepository";
    private Application app;
    private ServantDao svtDao;
    private SvtExpDao svtExpDao;

    public CalcRepository(Application app) {
        this.app = app;
        CalcDatabase calcDB = CalcDatabase.getDatabase(app);
        svtDao = calcDB.servantDao();
        svtExpDao = calcDB.svtExpDao();
    }

    public List<ServantEntity> getAllServants() {
        return svtDao.getAllServants();
    }

    //关键词搜索从者
    public List<ServantEntity> getServantsByKeyWord(String keyword) {
        //繁体转简体
        keyword = ToolCase.tc2sc(keyword);
        return svtDao.getServantsByKeyword(keyword);
    }

    //筛选搜索从者
    public List<ServantEntity> getServantsByFilter(SimpleSQLiteQuery query) {
        return svtDao.getServantsByFilter(query);
    }
    /**
     * 有关筛选的操作
     */
    public List<FilterEntity> getFilters() {
        String json = getAssetsJson("filter.json");
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<FilterEntity>>(){}.getType();
            return gson.fromJson(json,type);
        }
    }

    private String getAssetsJson(String jsonName) {
        try(InputStream is = app.getAssets().open(jsonName)) {
          return   IOUtils.toString(is,"UTF-8");
        } catch (IOException e) {
            return null;
        }
    }


    public List<InfoEntity> getServantInfo(ServantEntity servant) {
        return InfoBuilder.buildServantInfo(servant);
    }


    //获取经验列表
    public LiveData<List<SvtExpEntity>> getSvtExpList(int svtId) {
        return svtExpDao.getSvtExpList(svtId);
    }

}
