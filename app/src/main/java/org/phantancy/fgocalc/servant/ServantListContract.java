package org.phantancy.fgocalc.servant;

import android.app.Activity;
import android.content.Context;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;
import org.phantancy.fgocalc.item.FilterItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item.UpdateItem;

import java.util.List;

/**
 * Created by HATTER on 2017/10/24.
 */

public interface ServantListContract {

    interface View extends BaseView<Presenter> {
        void showMenuLocDialog();
        void showAboutDialog();
        void showUpdateDiag(UpdateItem updateItem);
        void setServantList(List<ServantItem> list);
    }

    interface Presenter extends BasePresenter{
        void checkMenuLoc(boolean locLeft);
        void fgotool();
        void fgosimulator();
        void sendEmail(Context ctx);
        void simpleCheck(Context ctx, Activity acty);//检查权限、版本更新、数据库更新
        void reloadDatabase();
        void loadDatabaseExtra();
        void downloadDatabaseExtra();
        void unregisterReceiver(Context ctx);
        void getAllServants();
        void searchServantsByKeyword(String value);
        void searchServantsByCondition(List<FilterItem> filterItems);
        String getVersion();
        void feedback();
        void follow();
        void qq();
        List<FilterItem> getFilterItems();
        void checkAppUpdate(boolean isManual);
    }

}
