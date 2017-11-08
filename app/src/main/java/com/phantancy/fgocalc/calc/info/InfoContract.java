package com.phantancy.fgocalc.calc.info;

import com.phantancy.fgocalc.base.BasePresenter;
import com.phantancy.fgocalc.base.BaseView;
import com.phantancy.fgocalc.item.InfoItem;
import com.phantancy.fgocalc.item.ServantItem;

import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public interface InfoContract {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        List<InfoItem> getInfoList(ServantItem item);
    }
}
