package com.phantancy.fgocalc.calc.buff;

import com.phantancy.fgocalc.base.BasePresenter;
import com.phantancy.fgocalc.base.BaseView;
import com.phantancy.fgocalc.item.BuffItem;
import com.phantancy.fgocalc.item.BuffsItem;
import com.phantancy.fgocalc.item.ServantItem;

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
