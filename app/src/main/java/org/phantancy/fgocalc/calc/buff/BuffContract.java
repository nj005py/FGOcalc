package org.phantancy.fgocalc.calc.buff;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;
import org.phantancy.fgocalc.item.BuffItem;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ServantItem;

import java.util.List;

/**
 * Created by HATTER on 2017/11/6.
 */

public interface BuffContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        List<BuffItem> getBuffList(ServantItem sItem,BuffsItem bItem);
        List<BuffItem> cleanBuffs(List<BuffItem> list);
        BuffsItem getBuffsItem(List<BuffItem> list);
    }
}
