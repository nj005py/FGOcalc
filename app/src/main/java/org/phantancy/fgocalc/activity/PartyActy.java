package org.phantancy.fgocalc.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.LinearLayout;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.PartyItemTouchHelperCallback;
import org.phantancy.fgocalc.adapter.PartySvtAdapter;
import org.phantancy.fgocalc.base.BaseActy;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item_decoration.GridItemDecoration;
import org.phantancy.fgocalc.util.ToolCase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PartyActy extends BaseActy {

    @BindView(R.id.status_bar)
    LinearLayout statusBar;
    @BindView(R.id.ap_rv_party)
    RecyclerView apRvParty;

    private PartySvtAdapter adapter;
    private List<ServantItem> sList;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.acty_party);
        ButterKnife.bind(this);
        int color = ContextCompat.getColor(ctx, R.color.colorGrayWiki);
//        ToolCase.setStatusBar(statusBar, color, this, true, false);

        sList = (List<ServantItem>) getIntent().getSerializableExtra("servants");

        if (sList != null) {
            List<ServantItem> pList = new ArrayList<>();
            for (int i = 0;i < 6; i ++){
                pList.add(sList.get(i));
            }

            adapter = new PartySvtAdapter(pList,ctx,this);
            GridLayoutManager lm = new GridLayoutManager(ctx,6);
            apRvParty.setAdapter(adapter);
            apRvParty.setLayoutManager(lm);
            apRvParty.addItemDecoration(new GridItemDecoration(ctx, 5, 5));

            //先实例化Callback
            ItemTouchHelper.Callback callback = new PartyItemTouchHelperCallback(adapter);
            //用Callback构造ItemtouchHelper
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            //调用ItemTouchHelper的attachToRecyclerView方法建立联系
            touchHelper.attachToRecyclerView(apRvParty);

        }
    }
}
