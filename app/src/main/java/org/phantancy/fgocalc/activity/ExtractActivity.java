package org.phantancy.fgocalc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.phantancy.fgocalc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import tyrantgit.explosionfield.ExplosionField;


/**
 * Created by PY on 2016/10/31.
 */
public class ExtractActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.status_bar)
    LinearLayout statusBar;
    @BindView(R.id.ae_iv_card)
    ImageView aeIvCard;
    @BindView(R.id.ae_iv_cover)
    ImageView aeIvCover;
    @BindView(R.id.ae_iv_star)
    ImageView aeIvStar;
    private int type;//1夕学 2冰学
    private ExplosionField mExplosionField;
    private int[] card = {R.mipmap.servant_golden, R.mipmap.servant_silver, R.mipmap.essence_golden, R.mipmap.essence_silver};
    private int[] star = {R.mipmap.star_r,R.mipmap.star_sr,R.mipmap.star_ssr};
    private final int ANIME = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIME:
                    /**
                     * ExplosionField explosionField = new ExplosionField(this,new FallingParticleFactory());
                     ExplosionField explosionField2 = new ExplosionField(this,new FlyawayFactory());
                     ExplosionField explosionField4 = new ExplosionField(this,new ExplodeParticleFactory());
                     ExplosionField explosionField5 = new ExplosionField(this,new VerticalAscentFactory());*/
//        mExplosionField = new ExplosionField(this,new FallingParticleFactory());
//        mExplosionField.addListener(findViewById(R.id.text));
//        mExplosionField.addListener(findViewById(R.id.ae_iv_card));
                    // 开始执行动画...
                    mExplosionField.explode(aeIvCover);
//                 ExplosionField.explode后，父布局中虽看不到ImageView，但ImageView所占据的位置还会响应事件.
//                 如果想屏蔽此ImageView出现，则：
                    aeIvCover.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_extract);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        type = getIntent().getIntExtra("type", 1);
        initBaseStatusBar(mContext);
        llStatusBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBlack));
        int num = (int) (Math.random() * 3 + 0);//封面
        int extract = (int) (Math.random() * 100 + 1);//实际抽到的
//        extract = extract + type;
        Log.d(TAG, "extract->" + extract);
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
        Log.d(TAG, "extract->" + extract);
        int curCard = 3;
        int curStar = 1;
        if (extract == 1) {
            curCard = 0;
            curStar = 2;
        } else if (extract >= 2 && extract <= 5) {
            curCard = 2;
            curStar = 2;
        } else if (extract >= 6 && extract <= 45) {
            curCard = 1;
            curStar = 0;
        } else if (extract >= 46 && extract <= 57) {
            curCard = 2;
            curStar = 1;
        }else if(extract >= 58 && extract <= 97){
            curCard = 3;
            curStar = 0;
        }else if(extract >= 98 && extract <= 100){
            curCard = 0;
            curStar = 1;
        }
//        ToastUtils.displayLongToast(ctx,"extract->" + extract + " curCard->" + curCard + " curStar->" + curStar);
        Log.d(TAG, "extract->" + extract + " curCard->" + curCard + " curStar->" + curStar);
        aeIvCard.setImageResource(card[curCard]);
        aeIvCover.setImageResource(card[num]);
        aeIvStar.setImageResource(star[curStar]);
        // 先初始化载入ExplosionField
        mExplosionField = ExplosionField.attach2Window(this);
        showAnimation();
        setListener();
    }

    private void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void showAnimation() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    Message msg = new Message();
                    msg.what = ANIME;
                    handler.sendMessage(msg); //告诉主线程执行任务

                }

            }).start();
        } catch (Exception e) {

        }
    }
}
