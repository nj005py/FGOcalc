package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.common.Constant;
import org.phantancy.fgocalc.item.InfoCardsMVPItem;
import org.phantancy.fgocalc.item.InfoItem;
import org.phantancy.fgocalc.util.GlideApp;
import org.phantancy.fgocalc.util.ToolCase;

import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<InfoItem> mList;
    private InfoCardsMVPAdapter adapter;
    private Context ctx;

    public InfoAdapter(List<InfoItem> mList,Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
        notifyDataSetChanged();
    }

    public void setItems(List<InfoItem> list){
        if (list != null) {
            if (mList != null) {
                mList.clear();
                mList.addAll(list);
                notifyDataSetChanged();
            }else{
                mList = list;
                notifyDataSetChanged();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivInfo;
        TextView tvInfo;
        TextView tvInfoTitle;
        RecyclerView rvInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ivInfo = (ImageView)itemView.findViewById(R.id.ii_iv_info);
            tvInfo = (TextView)itemView.findViewById(R.id.ii_tv_info);
            tvInfoTitle = (TextView)itemView.findViewById(R.id.ii_tv_info_title);
            rvInfo = (RecyclerView)itemView.findViewById(R.id.ii_rv_card);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList != null) {
            InfoItem item = mList.get(position);
            ImageView ivInfo = holder.ivInfo;
            TextView tvInfo = holder.tvInfo;
            TextView tvInfoTitle = holder.tvInfoTitle;
            RecyclerView rvInfo = holder.rvInfo;
            switch (item.getType()) {
                case Constant.TYPE_IMG:
                    int resId = item.getPortrait();
                    if (item.getPortrait() != 0) {
                        GlideApp.with(ctx)
                                .load(resId)
                                .placeholder(R.drawable.loading)
                                .into(ivInfo);
                    }else if (!TextUtils.isEmpty(item.getPic())) {
                        //如果数据库里有图则从数据库里读图
                        //将Base64串转化为位图
                        Bitmap bmp = ToolCase.base642Bitmap(item.getPic());
                        ivInfo.setImageBitmap(bmp);
                    }else{
                        String num = "";
                        if (item.getId() > 0 && item.getId() < 10) {
                            num = new StringBuilder().append("00").append(item.getId()).toString();
                        }else if (item.getId() >= 10 && item.getId() < 100) {
                            num = new StringBuilder().append("0").append(item.getId()).toString();
                        }else{
                            num = new StringBuilder().append(item.getId()).toString();
                        }
                        //从fgowiki获取头像
                        String url = new StringBuilder().append("http://file.fgowiki.fgowiki.com/fgo/head/").append(num).append(".jpg").toString();
                        GlideApp.with(ctx)
                                .load(url)
                                .placeholder(R.drawable.loading)
                                .into(ivInfo);
                    }
                    ivInfo.setVisibility(View.VISIBLE);
                    tvInfo.setVisibility(View.GONE);
                    tvInfoTitle.setVisibility(View.GONE);
                    rvInfo.setVisibility(View.GONE);
                    break;
                case Constant.TYPE_VALUE:
                    String txt = item.getInfo();
                    String[] arr = txt.split(">");
                    if (arr.length > 1) {
                        ivInfo.setVisibility(View.GONE);
                        tvInfo.setVisibility(View.VISIBLE);
                        tvInfoTitle.setVisibility(View.VISIBLE);
                        rvInfo.setVisibility(View.GONE);
                        tvInfoTitle.setText(arr[0]);
                        tvInfo.setText(arr[1]);
                    }else{
                        ivInfo.setVisibility(View.GONE);
                        tvInfo.setVisibility(View.VISIBLE);
                        tvInfoTitle.setVisibility(View.GONE);
                        rvInfo.setVisibility(View.GONE);
                        tvInfo.setText(txt);
                    }
                    break;
                case Constant.TYPE_LIST:
                    List<InfoCardsMVPItem> iList = item.getCardsList();
                    if (iList != null) {
                        adapter = new InfoCardsMVPAdapter(iList);
                        LinearLayoutManager ll = new LinearLayoutManager(ctx);
                        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
                        rvInfo.setLayoutManager(ll);
                        rvInfo.setAdapter(adapter);
                        ivInfo.setVisibility(View.GONE);
                        tvInfo.setVisibility(View.GONE);
                        tvInfoTitle.setVisibility(View.GONE);
                        rvInfo.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
