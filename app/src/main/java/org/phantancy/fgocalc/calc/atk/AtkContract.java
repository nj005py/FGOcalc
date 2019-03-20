package org.phantancy.fgocalc.calc.atk;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;
import org.phantancy.fgocalc.item.AtkHpItem;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ConditionAtk;
import org.phantancy.fgocalc.item.ConditionTrump;
import org.phantancy.fgocalc.item.CurveItem;
import org.phantancy.fgocalc.item.ServantItem;

import java.util.List;

/**
 * Created by HATTER on 2017/11/6.
 */

public interface AtkContract {

    interface View extends BaseView<AtkContract.Presenter>{
        void setResult(Object result);
        void setCharacter(String str);
    }

    interface Presenter extends BasePresenter{
        double getRan(int type);
        ConditionAtk getCondition(int atk,String cardType1,String cardType2,String cardType3,boolean ifEx,
                                  boolean ifCr1,boolean ifCr2,boolean ifCr3,int weakType,
                                  double teamCor,double randomCor,ServantItem servantItem,
                                  BuffsItem buffsItem,String npLv);
        ConditionTrump getConditionTrump(int atk, int hpTotal, int hpLeft, String trumpColor,
                                         int weakType, double teamCor, double randomCor, double trumpTimes,
                                         ServantItem servantItem, BuffsItem buffsItem,String npLv);
        void getReady(ConditionAtk conAtk,ConditionTrump conT);
        void clean();
        AtkHpItem getAtkHpByLv(ServantItem sItem,int lv,List<CurveItem> curveList);
    }
}
