package com.phantancy.fgocalc.servant;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import com.phantancy.fgocalc.base.BasePresenter;
import com.phantancy.fgocalc.base.BaseView;
import com.phantancy.fgocalc.item.ServantItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HATTER on 2017/10/24.
 */

public interface ServantListContract {

    interface View extends BaseView<Presenter> {
        void showCharacter(String content);
        void showMenuLocDialog();
        void showAboutDialog();
        void showUpdateDiag(String update,String downloadUrl,String curVersion);
    }

    interface Presenter extends BasePresenter{
        void setMethod(int method);
        void checkMenuLoc(boolean locLeft);
        void sendEmail(Context ctx);
        void simpleCheck(Context ctx, Activity acty);//检查权限、版本更新、数据库更新
        void reloadDatabase();
        void loadDatabaseExtra();
        void unregisterReceiver(Context ctx);
        List<ServantItem> getAllServants();
        List<ServantItem> searchServantsByKeyword(String value);
        List<ServantItem> searchServantsByCondition(String classType,int star);
        String getVersion();
    }

}
