package com.phantancy.fgocalc.calc.atk;

import com.phantancy.fgocalc.base.BasePresenter;
import com.phantancy.fgocalc.base.BaseView;
import com.phantancy.fgocalc.calc.info.InfoContract;
import com.phantancy.fgocalc.item.BuffsItem;
import com.phantancy.fgocalc.item.ConditionAtk;
import com.phantancy.fgocalc.item.ServantItem;

/**
 * Created by HATTER on 2017/11/6.
 */

public interface AtkContract {

    interface View extends BaseView<AtkContract.Presenter>{
        void setResult(String result);
    }

    interface Presenter extends BasePresenter{
        double getRan(int type);
        ConditionAtk getCondition(int atk,String cardType1,String cardType2,String cardType3,boolean ifEx,
                                  boolean ifCr1,boolean ifCr2,boolean ifCr3,int weakType,
                                  double teamCor,double randomCor,ServantItem servantItem,
                                  BuffsItem buffsItem);
        void getReady(ConditionAtk conAtk);
        void clean();
    }
}
