package org.phantancy.fgocalc.calc.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.activity.WebviewActy;
import org.phantancy.fgocalc.adapter.InfoAdapter;
import org.phantancy.fgocalc.base.BaseFrag;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.InfoItem;
import org.phantancy.fgocalc.item.ServantItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoFrag extends BaseFrag implements InfoContract.View {

    @BindView(R.id.fim_rv_info)
    RecyclerView fimRvInfo;
    Unbinder unbinder;
    @BindView(R.id.fim_btn_more)
    Button fimBtnMore;
    Unbinder unbinder1;
    private ServantItem servantItem;
    private InfoContract.Presenter mPresenter;
    private List<InfoItem> infoList;
    private InfoAdapter infoAdapter;
    private static final boolean ONEN_LAYOUT = true;
    private Runnable trigger;
    private int id;

    @Override
    public void setPresenter(InfoContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_info_mvp, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle data = getArguments();//获得从activity中传递过来的值
        servantItem = (ServantItem) data.getSerializable("servantItem");
        if (servantItem != null) {
            infoList = mPresenter.getInfoList(servantItem);
            id = servantItem.getId();
            //配置recyclerview
            GridLayoutManager gl = new GridLayoutManager(ctx, 4);
            gl.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    InfoItem item = infoList.get(position);
                    if (item.getType() == Constant.TYPE_LIST) {
                        return 3;
                    } else {
                        if (item.getColumn() > 0) {
                            return item.getColumn();
                        } else {
                            return 1;
                        }
                    }
                }
            });
//            final VirtualLayoutManager layoutManager = new VirtualLayoutManager(ctx);
//            fimRvInfo.setLayoutManager(layoutManager);
//            final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//            fimRvInfo.setRecycledViewPool(viewPool);
//            viewPool.setMaxRecycledViews(0, 20);
//            final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
//            fimRvInfo.setAdapter(delegateAdapter);
//            List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
//            if (ONEN_LAYOUT) {
//                OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
//                helper.setBgColor(0xff876384);
//                helper.setMargin(10, 10, 10, 10);
//                helper.setPadding(10, 10, 10, 10);
//                adapters.add(new InfoAdapter(infoList,ctx,helper,3));
//            }
//            delegateAdapter.setAdapters(adapters);
//
//            final Handler mainHandler = new Handler(Looper.getMainLooper());
//            trigger = new Runnable() {
//                @Override
//                public void run() {
//                    // recyclerView.scrollToPosition(22);
//                    // recyclerView.getAdapter().notifyDataSetChanged();
//                    fimRvInfo.requestLayout();
//                    // mainHandler.postDelayed(trigger, 1000);
//                }
//            };
//            mainHandler.postDelayed(trigger, 1000);
            ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                    //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                    .setChildGravity(Gravity.TOP)
                    //whether RecyclerView can scroll. TRUE by default
                    .setScrollingEnabled(true)
                    //set maximum views count in a particular row
                    .setMaxViewsInRow(2)
                    //set gravity resolver where you can determine gravity for item in position.
                    //This method have priority over previous one
                    .setGravityResolver(new IChildGravityResolver() {
                        @Override
                        public int getItemGravity(int position) {
                            return Gravity.CENTER;
                        }
                    })
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    //you are able to break row due to your conditions. Row breaker should return true for that views
                    .setRowBreaker(new IRowBreaker() {
                        @Override
                        public boolean isItemBreakRow(@IntRange(from = 0) int position) {
                            return position == 0 || position == 1;
                        }
                    })
                    .build();
            fimRvInfo.setLayoutManager(gl);
            fimRvInfo.getRecycledViewPool().setMaxRecycledViews(0, 30);
            fimRvInfo.getRecycledViewPool().setMaxRecycledViews(1, 30);
            infoAdapter = new InfoAdapter(infoList, ctx);
            fimRvInfo.setAdapter(infoAdapter);
            fimBtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = new StringBuilder().append("http://fgowiki.com/guide/petdetail/").append(id).toString();
                    Intent i = new Intent(ctx, WebviewActy.class);
                    i.putExtra("url",url);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
