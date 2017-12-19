package org.phantancy.fgocalc.calc.info;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;
import org.phantancy.fgocalc.item.InfoItem;
import org.phantancy.fgocalc.item.ServantItem;

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
