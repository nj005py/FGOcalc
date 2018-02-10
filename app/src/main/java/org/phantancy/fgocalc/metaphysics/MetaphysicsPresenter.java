package org.phantancy.fgocalc.metaphysics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.animation.AnimationUtils;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.ImageUtils;
import org.phantancy.fgocalc.util.ToastUtils;

import java.io.FileNotFoundException;
import java.text.NumberFormat;

/**
 * Created by HATTER on 2018/2/10.
 */

public class MetaphysicsPresenter implements MetaphysicsContract.Presenter {
    private Context ctx;
    private MetaphysicsContract.View mView;
    private double blackPercent = -1;
    private double grayPercent = -1;
    private double[] percent = {-1, -1};
    private double[] percentBlack = {-1,-1};

    public MetaphysicsPresenter(Context ctx, MetaphysicsContract.View mView) {
        this.ctx = ctx;
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void pic2Result(Uri imageUri) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(imageUri));
            if (bitmap != null) {
                //压缩图片
                Bitmap photo = ImageUtils.reduce(bitmap, 800, 480, true);
                Bitmap photoBlack = photo;
                //释放bitmap
                bitmap.recycle();
                //黑白化图片
                photo = ImageUtils.convertToBMW(photo, 800, 480, 55);
                photoBlack = ImageUtils.convertToBMW(photoBlack,800,480,105);
                NumberFormat nf = NumberFormat.getPercentInstance();
                nf.setMinimumFractionDigits(2);
                percent = ImageUtils.check(photo);
                percentBlack = ImageUtils.check(photoBlack);
                grayPercent = percent[1];
                blackPercent = percentBlack[0];
                if (blackPercent == 0) {
                    blackPercent = 0.01;
                }
                /**
                 * 1 ssr s 0
                 * 2-5 ssr e 2
                 * 6-45 r s 1
                 * 46-57 sr e 2
                 * 58-97 r e 3
                 * 98-100 sr s 0
                 * */
                //card 0 金从者 1 银从者 2 金礼装 3 银礼装
                //star 0 r,1 sr,2 ssr
                int ssrsNum  = 0;
                int srsNum = 0;
                int rNum = 0;
                for (int i = 0;i < 10;i ++){
                    int extract = (int) (Math.random() * 100 + 1);//抽卡
                    if (extract == 1) {
                        ssrsNum ++;
                    } else if (extract >= 2 && extract <= 5) {

                    } else if (extract >= 6 && extract <= 45) {
                        rNum ++;
                    } else if (extract >= 46 && extract <= 57) {

                    }else if(extract >= 58 && extract <= 97){
                        rNum ++;
                    }else if(extract >= 98 && extract <= 100){
                        srsNum ++;
                    }
                }
                blackPercent = (blackPercent / 2) + (rNum * 0.05);
                grayPercent = (grayPercent / 2) + (ssrsNum * 0.05) + (srsNum * 0.01);
                String black = nf.format(blackPercent);
                String gray = nf.format(grayPercent);
                mView.setResultProgress(gray,black,(int)( grayPercent * 100),(int) (blackPercent * 100));
                check();
            }else{
                mView.setCharacter("欧非ex，无法判断!",R.mipmap.joan_alter_dislike);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mView.setCharacter("欧非ex，无法判断!",R.mipmap.joan_alter_dislike);
        }
    }

    private void check() {
        if (blackPercent == -1) {
            ToastUtils.displayShortToast(ctx, "请进行圣遗物检测");
        } else {
            if (blackPercent >= 0.85) {
                mView.setCharacter("非气浓度都高成这样了还要抽卡？\n难道你是受虐狂吗？",R.mipmap.joan_alter_dislike);
            } else {
                if (blackPercent < 0.1) {
                    mView.setCharacter("欧气足的Master!\n快去迎接新Servant吧!",R.mipmap.joan_alter_smile);
                }
            }
        }
    }
}
