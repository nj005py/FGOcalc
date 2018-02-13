package org.phantancy.fgocalc.metaphysics;

import android.net.Uri;

import org.phantancy.fgocalc.base.BasePresenter;
import org.phantancy.fgocalc.base.BaseView;

/**
 * Created by HATTER on 2018/2/10.
 */

public interface MetaphysicsContract {

    interface View extends BaseView<Presenter>{
        void setCharacter(String text,int imgRes);
        void setResultProgress(String eu,String af,int euInt,int afInt);
        void setResult(String result);
    }

    interface Presenter extends BasePresenter{
        void pic2Result(Uri imageUri);
    }
}
