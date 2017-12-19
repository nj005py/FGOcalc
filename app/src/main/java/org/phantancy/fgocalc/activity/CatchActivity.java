package org.phantancy.fgocalc.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.ItemAdapter;
import org.phantancy.fgocalc.item.CatchItem;
import org.phantancy.fgocalc.item.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by PY on 2016/10/31.
 */
public class CatchActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ac_gv_catch)
    GridView acGvCatch;
    @BindView(R.id.ac_btn_catch)
    Button acBtnCatch;
    @BindView(R.id.ac_tv_times)
    TextView acTvTimes;
    private ItemAdapter itemAdapter;
    private int[] card = {R.mipmap.servant_golden, R.mipmap.servant_silver, R.mipmap.essence_golden, R.mipmap.essence_silver};
    private int[] star = {R.mipmap.star_r, R.mipmap.star_sr, R.mipmap.star_ssr};
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_catch);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        itemAdapter = new ItemAdapter(mContext);
        acGvCatch.setAdapter(itemAdapter);
        doExtract();
        setListener();
    }

    private void setListener() {
        acBtnCatch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_btn_catch:
                doExtract();
                break;
        }
    }

    private void doExtract() {
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(extract());
        }
        acTvTimes.setText("抽卡次数：" + count++);
        itemAdapter.clear();
        itemAdapter.addItems(list);
        itemAdapter.notifyDataSetChanged();
    }

    private CatchItem extract() {
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
        } else if (extract >= 58 && extract <= 97) {
            curCard = 3;
            curStar = 0;
        } else if (extract >= 98 && extract <= 100) {
            curCard = 0;
            curStar = 1;
        }
//        ToastUtils.displayLongToast(ctx,"extract->" + extract + " curCard->" + curCard + " curStar->" + curStar);
        Log.d(TAG, "extract->" + extract + " curCard->" + curCard + " curStar->" + curStar);
        CatchItem item = new CatchItem();
        item.setImgResource(card[curCard]);
        item.setStarResource(star[curStar]);
        return item;
    }
}
