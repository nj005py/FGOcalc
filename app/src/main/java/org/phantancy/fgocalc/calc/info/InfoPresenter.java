package org.phantancy.fgocalc.calc.info;

import android.content.Context;
import android.support.annotation.NonNull;

import org.phantancy.fgocalc.R;

import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.InfoCardsMVPItem;
import org.phantancy.fgocalc.item.InfoItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.ToolCase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoPresenter implements InfoContract.Presenter {
    private Context ctx;

    @NonNull
    private InfoContract.View mView;

    public InfoPresenter(@NonNull InfoContract.View mView,@NonNull Context ctx) {
        this.mView = mView;
        this.ctx = ctx;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public List<InfoItem> getInfoList(ServantItem item) {
        List<InfoItem> list = new ArrayList<>();
        int id = item.getId();
        String name = item.getName();
        String nickname = item.getNickname();
        String class_type = item.getClass_type();
        int star = item.getStar();
        int arts_hit = item.getArts_hit();
        int buster_hit = item.getBuster_hit();
        int quick_hit = item.getQuick_hit();
        int ex_hit = item.getEx_hit();
        double quick_na = item.getQuick_na();
        double arts_na = item.getArts_na();
        double buster_na = item.getBuster_na();
        double ex_na = item.getEx_na();
        double trump_na = item.getTrump_na();
        double nd = item.getNd();
        double arts_buff = item.getArts_buff();
        double buster_buff = item.getBuster_buff();
        double quick_buff = item.getQuick_buff();
        double atk_buff = item.getAtk_buff();
        double special_buff = item.getSpecial_buff();
        double critical_buff = item.getCritical_buff();
        int solid_buff = item.getSolid_buff();
        int busterNum = item.getBuster_num();
        int artNum = item.getArts_num();
        int quickNum = item.getQuick_num();
        int resId = ctx.getResources().getIdentifier("image" + id, "mipmap", ctx.getPackageName());
        double star_occur = item.getStar_occur();
        double star_occur_extra = item.getStar_occur_extra();
        int default_atk = item.getDefault_atk();
        int default_hp = item.getDefault_hp();
        int attribute = item.getAttribute();
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        List<InfoCardsMVPItem> cardList = getCardsNum(busterNum,artNum,quickNum);
        list.add(new InfoItem(id,resId,Constant.TYPE_IMG));
        list.add(new InfoItem(new StringBuilder().append("No.").append(id).append("\n").append(name).append("\n").append(star).append("星\n")
                .append(nickname)
                .toString(), Constant.TYPE_VALUE,3));
//        list.add(new InfoItem(new StringBuilder().append(nickname).toString(), Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.buster_hit) + "\n").append(buster_hit).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.arts_hit) + "\n").append(arts_hit).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.quick_hit) + "\n").append(quick_hit).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.ex_hit) + "\n").append(ex_hit).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.buster_na) + "\n").append(nf.format(buster_na)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.arts_na) + "\n").append(nf.format(arts_na)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.quick_na) + "\n").append(nf.format(quick_na)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.ex_na) + "\n").append(nf.format(ex_na)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.trump_na) + "\n").append(nf.format(trump_na)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.nd) + "\n").append(nf.format(nd)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.buster_buff) + "\n").append(nf.format(buster_buff)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.arts_buff) + "\n").append(nf.format(arts_buff)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.quick_buff) + "\n").append(nf.format(quick_buff)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.atk_buff) + "\n").append(nf.format(atk_buff)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.special_buff) + "\n").append(nf.format(special_buff)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.critical_buff) + "\n").append(nf.format(critical_buff)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.solid_buff) + "\n").append(solid_buff).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(cardList,Constant.TYPE_LIST));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.star_occur) + "\n").append(nf.format(star_occur)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.star_occur_extra) + "\n").append(nf.format(star_occur_extra)).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.default_hp) + "\n").append(default_hp).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.default_atk) + "\n").append(default_atk).toString(),Constant.TYPE_VALUE));
        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.attribute) + "\n").append(ToolCase.getAttributeString(attribute)).toString(),Constant.TYPE_VALUE));
//        list.add(new InfoItem(new StringBuilder().append(ctx.getString(R.string.) + "\n").append().toString(),Constant.TYPE_VALUE));
        return list;
    }

    private List<InfoCardsMVPItem> getCardsNum(int busterNum, int artNum, int quickNum) {
        List<InfoCardsMVPItem> list = new ArrayList<>();
        for (int i = 0; i < busterNum; i++) {
            list.add(new InfoCardsMVPItem("b"));
        }
        for (int i = 0; i < artNum; i++) {
            list.add(new InfoCardsMVPItem("a"));
        }
        for (int i = 0; i < quickNum; i++) {
            list.add(new InfoCardsMVPItem("q"));
        }
        return list;
    }
}
