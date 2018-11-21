package org.phantancy.fgocalc.calc.star;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ConditionStar;
import org.phantancy.fgocalc.item.ServantItem;

/**
 * Created by HATTER on 2017/11/7.
 */

public interface StarContract {

    interface View extends BaseView<Presenter>{
        void setResult(Object result);
    }

    interface Presenter extends BasePresenter{
        void clear();
        double getRan(int type);
        ConditionStar getCondition(String cardType1, String cardType2, String cardType3,
                                   boolean ifCr1, boolean ifCr2, boolean ifCr3,
                                   boolean ifok1, boolean ifok2, boolean ifok3,
                                   double randomCor, ServantItem servantItem, BuffsItem buffsItem,
                                   int enemyAmount);
        void getReady(ConditionStar conS);
    }
}
