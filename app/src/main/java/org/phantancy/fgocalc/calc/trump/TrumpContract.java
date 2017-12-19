package org.phantancy.fgocalc.calc.trump;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ConditionTrump;
import org.phantancy.fgocalc.item.ServantItem;

/**
 * Created by HATTER on 2017/11/7.
 */

public interface TrumpContract {

    interface View extends BaseView<Presenter>{
        void setCharacter(String str);
        void setResult(String result);
    }

    interface Presenter extends BasePresenter{
        double getRan(int type);
        void getReady(ConditionTrump conTrump);
        ConditionTrump getConditionTrump(int atk,int hpTotal,int hpLeft,String trumpColor,
                                int weakType,double teamCor,double randomCor,double trumpTimes,
                               ServantItem servantItem,BuffsItem buffsItem);
        void clear();
    }
}
