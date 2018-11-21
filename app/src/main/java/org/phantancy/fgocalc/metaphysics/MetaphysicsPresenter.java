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
        mView.setPresenter(this);
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
                double ssrsNum  = 0;
                double ssreNum = 0;
                double srsNum = 0;
                double sreNum = 0;
                double rNum = 0;
                for (int i = 0;i < 73;i ++){
                    int extract = (int) (Math.random() * 100 + 1);//抽卡
                    if (extract == 1) {
                        ssrsNum ++;
                    } else if (extract >= 2 && extract <= 5) {
                        ssreNum ++;
                    } else if (extract >= 6 && extract <= 45) {
                        rNum ++;
                    } else if (extract >= 46 && extract <= 57) {
                        sreNum ++;
                    }else if(extract >= 58 && extract <= 97){
                        rNum ++;
                    }else if(extract >= 98 && extract <= 100){
                        srsNum ++;
                    }
                }
                double all = ssrsNum + ssreNum + srsNum + sreNum + rNum;
                //3星卡比例
                double rPercent = rNum / all;
                //5星从者比例
                double ssrsPercent = ssrsNum / all;
                //5星礼装比例
                double ssrePercent = ssreNum / all;
                //4星从者比例
                double srsPercent = srsNum / all;
                //4星礼装比例
                double srePercent = sreNum / all;
                blackPercent = (blackPercent * 0.4) + (rPercent * 0.6);
                grayPercent = (grayPercent * 0.4) + (ssrsPercent * 15 + srsPercent * 8 + ssrePercent * 3 + srePercent * 1);
                double allpercent = blackPercent + grayPercent;
                if (allpercent > 1) {
                    blackPercent = blackPercent / allpercent;
                    grayPercent = grayPercent / allpercent;
                }
                String black = nf.format(blackPercent);
                String gray = nf.format(grayPercent);
                mView.setResultProgress(gray,black,(int)( grayPercent * 100),(int) (blackPercent * 100));
                mView.setResult("5星从者:" + ssrsNum + "个 5星礼装：" + ssreNum + "张\n4星从者" + srsNum + "个 4星礼装：" + sreNum + "张");
                check(ssrsNum,srsNum);
            }else{
                mView.setCharacter("欧非ex，无法判断!",R.drawable.joan_alter_dislike);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mView.setCharacter("欧非ex，无法判断!",R.drawable.joan_alter_dislike);
        }
    }

    @Override
    public void angle2Result(float angle) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);

        grayPercent = angle * 0.95 / 360;
        blackPercent = (360 - angle) / 360;
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
        double ssrsNum  = 0;
        double ssreNum = 0;
        double srsNum = 0;
        double sreNum = 0;
        double rNum = 0;
        for (int i = 0;i < 73;i ++){
            int extract = (int) (Math.random() * 100 + 1);//抽卡
            if (extract == 1) {
                ssrsNum ++;
            } else if (extract >= 2 && extract <= 5) {
                ssreNum ++;
            } else if (extract >= 6 && extract <= 45) {
                rNum ++;
            } else if (extract >= 46 && extract <= 57) {
                sreNum ++;
            }else if(extract >= 58 && extract <= 97){
                rNum ++;
            }else if(extract >= 98 && extract <= 100){
                srsNum ++;
            }
        }
        double all = ssrsNum + ssreNum + srsNum + sreNum + rNum;
        //3星卡比例
        double rPercent = rNum / all;
        //5星从者比例
        double ssrsPercent = ssrsNum / all;
        //5星礼装比例
        double ssrePercent = ssreNum / all;
        //4星从者比例
        double srsPercent = srsNum / all;
        //4星礼装比例
        double srePercent = sreNum / all;
        blackPercent = (blackPercent * 0.4) + (rPercent * 0.6);
        grayPercent = (grayPercent * 0.4) + (ssrsPercent * 15 + srsPercent * 8 + ssrePercent * 3 + srePercent * 1);
        double allpercent = blackPercent + grayPercent;
        if (allpercent > 1) {
            blackPercent = blackPercent / allpercent;
            grayPercent = grayPercent / allpercent;
        }
        String black = nf.format(blackPercent);
        String gray = nf.format(grayPercent);
        mView.setResultProgress(gray,black,(int)( grayPercent * 100),(int) (blackPercent * 100));
        mView.setResult("5星从者:" + ssrsNum + "个 5星礼装：" + ssreNum + "张\n4星从者" + srsNum + "个 4星礼装：" + sreNum + "张");
        check(ssrsNum,srsNum);
    }

    private void check(double ssrs, double srs) {
        if (blackPercent == -1) {
            ToastUtils.displayShortToast(ctx, "请进行点圣晶石进行检测");
        } else {
            if (blackPercent >= 0.8) {
                mView.setCharacter("这运气也太差了吧？\n传说中的幸运E？",R.drawable.joan_alter_dislike);
            }else if (blackPercent >= 0.7) {
                mView.setCharacter("这情况有些危险呀",R.drawable.joan_alter_dislike);
            }else if (grayPercent >= 0.8) {
                mView.setCharacter("真是个欧气满满的Master!\n难道你幸运EX？！",R.drawable.joan_alter_smile);
            }else if (grayPercent >= 0.7) {
                mView.setCharacter("欧气放出？！",R.drawable.joan_alter_smile);
            }else if (ssrs >= 1 || srs >= 5) {
                mView.setCharacter("这结果还不错\n你不觉得吗？",R.drawable.joan_alter_bored);
            }
        }
    }
}
