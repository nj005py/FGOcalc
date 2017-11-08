package com.phantancy.fgocalc.calc.trump;

import com.phantancy.fgocalc.base.BasePresenter;
import com.phantancy.fgocalc.base.BaseView;
import com.phantancy.fgocalc.item.BuffsItem;
import com.phantancy.fgocalc.item.ConditionTrump;
import com.phantancy.fgocalc.item.ServantItem;

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
