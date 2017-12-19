package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.phantancy.fgocalc.R;

import org.phantancy.fgocalc.item.BuffItem;
import org.phantancy.fgocalc.util.ToolCase;

import java.util.List;

/**
 * Created by HATTER on 2017/11/4.
 */

public class BuffAdapter extends RecyclerView.Adapter<BuffAdapter.ViewHolder> {
    private List<BuffItem> mList;
    private Context ctx;

    public BuffAdapter(List<BuffItem> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
        notifyDataSetChanged();
    }

    public void setItems(List<BuffItem> list){
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
    public void clear(){
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public void cleanBuffs() {
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                mList.get(i).setDefaultDouble(0);
                mList.get(i).setDefaultInt(0);
            }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBuff;
        TextView tvPercent;
        EditText etBuff;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBuff = (ImageView)itemView.findViewById(R.id.ib_iv_buff);
            tvPercent = (TextView)itemView.findViewById(R.id.ib_tv_percent);
            etBuff = (EditText)itemView.findViewById(R.id.ib_et_buff);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buff,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList != null) {
            BuffItem item = mList.get(position);
            if (item != null) {
                holder.ivBuff.setImageResource(item.getImg());
                holder.etBuff.setHint(item.getHint());
                if (item.isIfPercent()) {
                    ToolCase.setViewValue(holder.etBuff,item.getDefaultDouble() == 0 ? "" : new StringBuilder().append(item.getDefaultDouble()).toString());
                    holder.tvPercent.setVisibility(View.VISIBLE);
                }else{
                    ToolCase.setViewValue(holder.etBuff,item.getDefaultInt() == 0 ? "" : new StringBuilder().append(item.getDefaultInt()).toString());
                    holder.tvPercent.setVisibility(View.GONE);
                }
                //添加editText的监听事件
                holder.etBuff.addTextChangedListener(new MyTextWatcher(holder));
                //通过设置tag，防止position紊乱
                holder.etBuff.setTag(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public interface SaveEditListener{
        void saveEdit(int position, String string);
    }

    class MyTextWatcher implements TextWatcher{

        private ViewHolder vh;

        public MyTextWatcher(ViewHolder vh) {
            this.vh = vh;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            SaveEditListener listener = (SaveEditListener)ctx;
            if (s != null) {
//                listener.saveEdit(Integer.parseInt(vh.etBuff.getTag().toString()),s.toString());
                BuffItem item = mList.get(Integer.parseInt(vh.etBuff.getTag().toString()));
                if (item.isIfPercent()) {
                    double value = s.toString().isEmpty() ? 0 : Double.valueOf(s.toString());
                    mList.get(Integer.parseInt(vh.etBuff.getTag().toString())).setDefaultDouble(value);
                }else{
                    int value = s.toString().isEmpty() ? 0 : Integer.valueOf(s.toString());
                    mList.get(Integer.parseInt(vh.etBuff.getTag().toString())).setDefaultInt(value);
                }
            }
        }
    }
}
