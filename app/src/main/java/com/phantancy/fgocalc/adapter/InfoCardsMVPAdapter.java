package com.phantancy.fgocalc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.item.InfoCardsMVPItem;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by HATTER on 2017/11/4.
 */

public class InfoCardsMVPAdapter extends RecyclerView.Adapter<InfoCardsMVPAdapter.ViewHolder> {
    private List<InfoCardsMVPItem> mList;

    public InfoCardsMVPAdapter(List<InfoCardsMVPItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setItems(List<InfoCardsMVPItem> list){
        if (list != null) {
            if (mList != null) {
                mList.clear();
                mList.addAll(list);
                notifyDataSetChanged();
            }else{
                mList = list;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCard;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCard = (ImageView)itemView.findViewById(R.id.iicm_iv_cards);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_cards_mvp,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList != null) {
            InfoCardsMVPItem item = mList.get(position);
            String type = item.getType();
            switch (type) {
                case "b":
                    holder.ivCard.setImageResource(R.mipmap.buster);
                    break;
                case "a":
                    holder.ivCard.setImageResource(R.mipmap.arts);
                    break;
                case "q":
                    holder.ivCard.setImageResource(R.mipmap.quick);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
