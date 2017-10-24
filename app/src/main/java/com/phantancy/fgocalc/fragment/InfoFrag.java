package com.phantancy.fgocalc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.adapter.ItemAdapter;
import com.phantancy.fgocalc.item.CardsItem;
import com.phantancy.fgocalc.item.Item;
import com.phantancy.fgocalc.item.ServantItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PY on 2017/3/3.
 */
public class InfoFrag extends BaseFrag {

    @BindView(R.id.fi_iv_portrait)
    ImageView fiIvPortrait;
    @BindView(R.id.fi_tv_name_star)
    TextView fiTvNameStar;
    @BindView(R.id.fi_tv_nickname)
    TextView fiTvNickname;
    @BindView(R.id.fi_tv_arts_hit)
    TextView fiTvArtsHit;
    @BindView(R.id.fi_tv_buster_hit)
    TextView fiTvBusterHit;
    @BindView(R.id.fi_tv_quick_hit)
    TextView fiTvQuickHit;
    @BindView(R.id.fi_tv_ex_hit)
    TextView fiTvExHit;
    @BindView(R.id.fi_tv_arts_na)
    TextView fiTvArtsNa;
    @BindView(R.id.fi_tv_buster_na)
    TextView fiTvBusterNa;
    @BindView(R.id.fi_tv_quick_na)
    TextView fiTvQuickNa;
    @BindView(R.id.fi_tv_ex_na)
    TextView fiTvExNa;
    @BindView(R.id.fi_tv_trump_na)
    TextView fiTvTrumpNa;
    @BindView(R.id.fi_tv_nd)
    TextView fiTvNd;
    @BindView(R.id.fi_tv_arts_buff)
    TextView fiTvArtsBuff;
    @BindView(R.id.fi_tv_buster_buff)
    TextView fiTvBusterBuff;
    @BindView(R.id.fi_tv_quick_buff)
    TextView fiTvQuickBuff;
    @BindView(R.id.fi_tv_atk_buff)
    TextView fiTvAtkBuff;
    @BindView(R.id.fi_tv_special_buff)
    TextView fiTvSpecialBuff;
    @BindView(R.id.fi_tv_critical_buff)
    TextView fiTvCriticalBuff;
    @BindView(R.id.fi_tv_solid)
    TextView fiTvSolid;
    @BindView(R.id.fi_gv_cards)
    GridView fiGvCards;

    private View view;
    private ServantItem servantItem;
    private String name, nickname, class_type;
    private int id, star, arts_hit, buster_hit, quick_hit, ex_hit, solid_buff;
    private int busterNum, artNum, quickNum;
    private double quick_na, arts_na, buster_na, ex_na, trump_na, nd, arts_buff, buster_buff, quick_buff,
            atk_buff, special_buff, critical_buff;
    private List<Item> cardsList = new ArrayList<>();
    private ItemAdapter cardsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frag_info, container, false);
            ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
    }

    private void init() {
        Bundle data = getArguments();//获得从activity中传递过来的值
        servantItem = (ServantItem) data.getSerializable("servants");
        if (servantItem != null) {
            id = servantItem.getId();
            name = servantItem.getName();
            nickname = servantItem.getNickname();
            class_type = servantItem.getClass_type();
            star = servantItem.getStar();
            arts_hit = servantItem.getArts_hit();
            buster_hit = servantItem.getBuster_hit();
            quick_hit = servantItem.getQuick_hit();
            ex_hit = servantItem.getEx_hit();
            quick_na = servantItem.getQuick_na();
            arts_na = servantItem.getArts_na();
            buster_na = servantItem.getBuster_na();
            ex_na = servantItem.getEx_na();
            trump_na = servantItem.getTrump_na();
            nd = servantItem.getNd();
            arts_buff = servantItem.getArts_buff();
            buster_buff = servantItem.getBuster_buff();
            quick_buff = servantItem.getQuick_buff();
            atk_buff = servantItem.getAtk_buff();
            special_buff = servantItem.getSpecial_buff();
            critical_buff = servantItem.getCritical_buff();
            solid_buff = servantItem.getSolid_buff();
            busterNum = servantItem.getBuster_num();
            artNum = servantItem.getArts_num();
            quickNum = servantItem.getQuick_num();
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(2);
            int resId = getResources().getIdentifier("image" + id, "mipmap", mContext.getPackageName());
            fiIvPortrait.setImageResource(resId);
            fiTvNameStar.setText(name + "\n" + "No." + id + " " + star + "星 " + class_type);
            fiTvNickname.setText(nickname);
            fiTvArtsHit.setText("蓝卡hit\n" + arts_hit);
            fiTvBusterHit.setText("红卡hit\n" + buster_hit);
            fiTvQuickHit.setText("绿卡hit\n" + quick_hit);
            fiTvExHit.setText("EX卡hit\n" + ex_hit);
            fiTvQuickNa.setText("绿卡NP获取率\n" + nf.format(quick_na));
            fiTvArtsNa.setText("蓝卡NP获取率\n" + nf.format(arts_na));
            fiTvBusterNa.setText("红卡NP获取率\n" + nf.format(buster_na));
            fiTvExNa.setText("EX卡NP获取率\n" + nf.format(ex_na));
            fiTvTrumpNa.setText("宝具NP获取率\n" + nf.format(trump_na));
            fiTvNd.setText("被动NP获取率\n" + nf.format(nd));
            fiTvArtsBuff.setText("蓝卡性能提升\n" + nf.format(arts_buff));
            fiTvBusterBuff.setText("红卡性能提升\n" + nf.format(buster_buff));
            fiTvQuickBuff.setText("绿卡性能提升\n" + nf.format(quick_buff));
            fiTvAtkBuff.setText("攻击提升\n" + nf.format(atk_buff));
            fiTvSpecialBuff.setText("特攻提升提升\n" + nf.format(special_buff));
            fiTvCriticalBuff.setText("暴击威力提升\n" + nf.format(critical_buff));
            fiTvSolid.setText("固定伤害\n" + solid_buff);
            getCardsNum();
        }
        setListener();
    }

    private void setListener() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void getCardsNum() {
        cardsAdapter = new ItemAdapter(mContext);
        fiGvCards.setAdapter(cardsAdapter);
        for (int i = 0; i < busterNum; i++) {
            cardsList.add(new CardsItem("b"));
        }
        for (int i = 0; i < artNum; i++) {
            cardsList.add(new CardsItem("a"));
        }
        for (int i = 0; i < quickNum; i++) {
            cardsList.add(new CardsItem("q"));
        }
        cardsAdapter.setItems(cardsList);;
    }

}
