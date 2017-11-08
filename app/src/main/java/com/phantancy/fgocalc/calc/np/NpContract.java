package com.phantancy.fgocalc.calc.np;

import com.phantancy.fgocalc.base.BasePresenter;
import com.phantancy.fgocalc.base.BaseView;
import com.phantancy.fgocalc.item.BuffsItem;
import com.phantancy.fgocalc.item.ConditionAtk;
import com.phantancy.fgocalc.item.ConditionNp;
import com.phantancy.fgocalc.item.ServantItem;

/**
 * Created by HATTER on 2017/11/7.
 */

public interface NpContract {

    interface View extends BaseView<Presenter>{
        void setResult(String result);
    }

    interface Presenter extends BasePresenter{
        double getRan(int type);
        ConditionNp getCondition(String cardType1,String cardType2,String cardType3,
                                 boolean ifCr1,boolean ifCr2,boolean ifCr3,
                                 boolean ifok1,boolean ifok2,boolean ifok3,
                                 double randomCor,ServantItem servantItem,BuffsItem buffsItem);
        void getReady(ConditionNp conNp);
        void clean();
    }
}
