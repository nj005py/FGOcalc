package com.phantancy.fgocalc.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.common.Constant;
import com.phantancy.fgocalc.item.InfoCardsMVPItem;
import com.phantancy.fgocalc.item.InfoItem;

import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<InfoItem> mList;
    private InfoCardsMVPAdapter adapter;
    private Context ctx;
    //test
//    private LayoutHelper mLayoutHelper;
//    private VirtualLayoutManager.LayoutParams mLayoutParams;
//    private int mCount = 0;

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
        RecyclerView rvInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ivInfo = (ImageView)itemView.findViewById(R.id.ii_iv_info);
            tvInfo = (TextView)itemView.findViewById(R.id.ii_tv_info);
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
            switch (item.getType()) {
                case Constant.TYPE_IMG:
                    if (item.getPortrait() != 0) {
                        holder.ivInfo.setImageResource(item.getPortrait());
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
                        ImageLoader.getInstance().displayImage(url,holder.ivInfo);
                    }
                    holder.ivInfo.setVisibility(View.VISIBLE);
                    break;
                case Constant.TYPE_VALUE:
                    holder.tvInfo.setText(item.getInfo());
                    holder.tvInfo.setVisibility(View.VISIBLE);
                    break;
                case Constant.TYPE_LIST:
                    List<InfoCardsMVPItem> iList = item.getCardsList();
                    if (iList != null) {
                        adapter = new InfoCardsMVPAdapter(iList);
                        LinearLayoutManager ll = new LinearLayoutManager(ctx);
                        ll.setOrientation(LinearLayoutManager.HORIZONTAL);
                        holder.rvInfo.setLayoutManager(ll);
                        holder.rvInfo.setAdapter(adapter);
                        holder.rvInfo.setVisibility(View.VISIBLE);
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
