package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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
import org.phantancy.fgocalc.view.ClearEditText;

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

    public void kongmingBuffs(double atkBuff,double criticalBuff,int solidBuff){
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                //加攻
                if (mList.get(i).getBuffName().equals("AtkUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + atkBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
                //加暴击
                if (mList.get(i).getBuffName().equals("CriticalUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + criticalBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
                //加固定伤害
                if (mList.get(i).getBuffName().equals("SolidAtk")) {
                    //取数据
                    int data = mList.get(i).getDefaultInt() + solidBuff;
                    //用数据
                    mList.get(i).setDefaultInt(data);
                    //填数据
                }
            }
            notifyDataSetChanged();
        }
    }

    public void merlinBuffs(double atkBuff,double criticalBuff,double busterUp){
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                //加攻
                if (mList.get(i).getBuffName().equals("AtkUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + atkBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
                //加暴击
                if (mList.get(i).getBuffName().equals("CriticalUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + criticalBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
                //红魔放
                if (mList.get(i).getBuffName().equals("BusterUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + busterUp;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
            }
            notifyDataSetChanged();
        }
    }

    public void foxBuffs(double artsUp){
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                //蓝魔放
                if (mList.get(i).getBuffName().equals("ArtsUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + artsUp;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
            }
            notifyDataSetChanged();
        }
    }

    public void scathachBuffs(double quickUp,double criticalQuick,double defenceDown) {
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                //绿魔放
                if (mList.get(i).getBuffName().equals("QuickUp")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + quickUp;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
                //绿卡暴击
                if (mList.get(i).getBuffName().equals("CriticalQuick")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + criticalQuick;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
                //减防
                if (mList.get(i).getBuffName().equals("EnemyDefence")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + defenceDown;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                }
            }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBuff;
        TextView tvPercent;
        ClearEditText etBuff;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBuff = itemView.findViewById(R.id.ib_iv_buff);
            tvPercent = itemView.findViewById(R.id.ib_tv_percent);
            etBuff = itemView.findViewById(R.id.ib_et_buff);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buff,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mList != null) {
            BuffItem item = mList.get(position);
            if (item != null) {
                holder.ivBuff.setImageResource(item.getImg());
                holder.etBuff.setHint(item.getHint());
                //通过设置tag，防止position紊乱
                holder.etBuff.setTag(position);
                holder.etBuff.clearFocus();
                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null) {
                            BuffItem item = mList.get(Integer.parseInt(holder.etBuff.getTag().toString()));
                            if (item.isIfPercent()) {
                                double value = TextUtils.isEmpty(s.toString()) ? 0 : Double.valueOf(s.toString());
                                mList.get(Integer.parseInt(holder.etBuff.getTag().toString())).setDefaultDouble(value);
                            }else{
                                int value = TextUtils.isEmpty(s.toString()) ? 0 : Integer.valueOf(s.toString());
                                mList.get(Integer.parseInt(holder.etBuff.getTag().toString())).setDefaultInt(value);
                            }
                        }
                    }
                };
                holder.etBuff.addTextChangedListener(watcher);
                if (item.isIfPercent()) {
                    ToolCase.setViewValue(holder.etBuff,item.getDefaultDouble() == 0 ? "" : new StringBuilder().append(item.getDefaultDouble()).toString());
                    holder.tvPercent.setVisibility(View.VISIBLE);
                }else{
                    ToolCase.setViewValue(holder.etBuff,item.getDefaultInt() == 0 ? "" : new StringBuilder().append(item.getDefaultInt()).toString());
                    holder.tvPercent.setVisibility(View.GONE);
                }
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
