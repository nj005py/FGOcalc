package org.phantancy.fgocalc.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.data.CalcDatabase;
import org.phantancy.fgocalc.data.repository.CalcRepository;
import org.phantancy.fgocalc.data.ServantAvatarData;
import org.phantancy.fgocalc.entity.FilterEntity;
import org.phantancy.fgocalc.entity.ServantEntity;
import org.phantancy.fgocalc.util.SharedPreferencesUtils;

import java.io.File;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    final String TAG = "MainViewModel";

    //数据源
    private CalcRepository calcRepository;
    private MutableLiveData<List<ServantEntity>> mServants = new MutableLiveData<>();

    public LiveData<List<ServantEntity>> getServants() {
        return mServants;
    }

    //搜索关键词
    private MutableLiveData<String> mKeyword = new MutableLiveData<>();

    public LiveData<String> getKeyword() {
        return mKeyword;
    }

    public String getCurrentKeyword() {
        return mKeyword.getValue();
    }

    public void setKeyword(String keyword) {
        mKeyword.setValue(keyword);
    }

    //清理搜索框
    private MutableLiveData<Boolean> clearSearch = new MutableLiveData<>();

    public LiveData<Boolean> getClearSearch() {
        return clearSearch;
    }

    //筛选列表
    public MutableLiveData<List<FilterEntity>> filters = new MutableLiveData<>();

    //当前页
    private MutableLiveData<Integer> mCurrentPage = new MutableLiveData<>();

    public LiveData<Integer> getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int page) {
        mCurrentPage.setValue(page);
    }

    public MainViewModel(Application app) {
        super(app);
        initDatabase(app);
    }

    //初始化数据库
    private void initDatabase(Application app){
        if (isDatabaseUpdated()) {
            deleteDbfile();
        }
        // 更新信号
        Constant.IS_DATABASE_UPDATED = true;
        calcRepository = new CalcRepository(app);
    }

    //删除数据库文件
    private void deleteDbfile(){
        File dbFile = getApplication().getDatabasePath(CalcDatabase.DB_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    //重新加载数据库
    public void reloadDatabase() {
        deleteDbfile();
        calcRepository = null;
        calcRepository = new CalcRepository(getApplication());
    }

    //检查更新数据库
    public boolean isDatabaseUpdated() {
        int installVersion = Constant.DATABASE_VERSION;
        int cachedVersion = SharedPreferencesUtils.getDatabaseVersion();
        if (installVersion > cachedVersion) {
            SharedPreferencesUtils.setDatabaseVersion(installVersion);
            return true;
        } else {
            return false;
        }
    }

    //获取所有从者
    public void getAllServants() {
        Log.d(TAG, "getAllServant");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ServantEntity> svts = calcRepository.getAllServants();
                Log.d(TAG, "size:" + svts.size());
                mServants.postValue(ServantAvatarData.setAvatars(svts));
            }
        }).start();

    }

    /**
     * 搜索
     */
    public void getServantsByKeyword(String keyword) {
        Log.d(TAG, "searchServant");

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ServantEntity> svts = calcRepository.getServantsByKeyWord(keyword);
                mServants.postValue(ServantAvatarData.setAvatars(svts));
            }
        }).start();
    }

    //清空搜索栏
    public void clearKeyword() {
        clearSearch.setValue(true);
    }

    //获取筛选列表
    public void getFilters() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                filters.postValue(calcRepository.getFilters());
            }
        }).start();
    }

    public void getServantByFilter(List<FilterEntity> filters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "SELECT * FROM servants WHERE 1=1";
                String classType = handleClassType(filters.get(0).getValue0());
                String star = handleStar(filters.get(1).getValue1());
                String attribute = handleAttribute(filters.get(2).getValue0());
                String traits = handleTraits(filters.get(4).getValue0());
                String npColor = handleNpColor(filters.get(5).getValue0());
                String npType = handleNpType(filters.get(6).getValue0());
                String cards = handleCards(filters.get(7).getValue0());
                String orderType = handleOrderType(filters.get(3).getValue0());

                Log.d(TAG, classType + " " + star + " " + attribute + " " + orderType + " " + traits + " "
                        + npColor + " " + npType + " " + cards);

                sql += classType + star + attribute + traits + npColor + npType + cards + orderType;
                Log.d(TAG, "sql:" + sql);
                SimpleSQLiteQuery query = new SimpleSQLiteQuery(sql);
                List<ServantEntity> svts = calcRepository.getServantsByFilter(query);
                mServants.postValue(ServantAvatarData.setAvatars(svts));
            }
        }).start();
    }

    //职阶类型
    private String handleClassType(String x) {
        return x.equals("any") ? "" : " AND class_type = '" + x + "'";
    }

    //星
    private String handleStar(int x) {
        return x == -1 ? "" : " AND star = " + x;
    }

    //阵营
    private String handleAttribute(String x) {
        return x.equals("any") ? "" : " AND attribute = '" + x + "'";
    }

    //特性
    private String handleTraits(String x) {
        return x.equals("any") ? "" : " AND traits LIKE '%" + x + "%'";
    }

    //宝具卡色
    // AND id in (SELECT sid FROM noble_phantasm WHERE np_color = 'np_b')
    private String handleNpColor(String x) {
        return x.equals("any") ? "" : " AND id in (SELECT sid FROM noble_phantasm WHERE np_color = '" + x + "')";
    }

    //宝具类型
    private String handleNpType(String x) {
        return x.equals("any") ? "" : " AND np_type = '" + x + "'";
    }

    //卡序
    private String handleCards(String x) {
        return x.equals("any") ? "" : " AND cards = '" + x + "'";
    }

    //排序
    private String handleOrderType(String x) {
        return " ORDER BY " + x;
    }

}
