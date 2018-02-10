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
//            mList.get(0).setDefaultDouble(30);
//            notifyDataSetChanged();
//
            for (int i = 0;i < mList.size();i ++){
                //加攻
                if (mList.get(i).getBuffName().equals("atk_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + atkBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
                //加暴击
                if (mList.get(i).getBuffName().equals("critical_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + criticalBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
                //加固定伤害
                if (mList.get(i).getBuffName().equals("wake_up")) {
                    //取数据
                    int data = mList.get(i).getDefaultInt() + solidBuff;
                    //用数据
                    mList.get(i).setDefaultInt(data);
                    //填数据
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void merlinBuffs(double atkBuff,double criticalBuff,double busterUp){
        if (mList != null) {
//            mList.get(0).setDefaultDouble(30);
//            notifyDataSetChanged();
//
            for (int i = 0;i < mList.size();i ++){
                //加攻
                if (mList.get(i).getBuffName().equals("atk_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + atkBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
                //加暴击
                if (mList.get(i).getBuffName().equals("critical_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + criticalBuff;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
                //红魔放
                if (mList.get(i).getBuffName().equals("buster_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + busterUp;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void foxBuffs(double artsUp){
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                //蓝魔放
                if (mList.get(i).getBuffName().equals("arts_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + artsUp;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
            }
        }
    }

    public void relationAtk(String cur,String pre){
        double atkUp = 0;
        double atkDown = 0;
        switch (cur) {
            case "小于0":
            case "":
                atkUp = 0;
                break;
            case "5":
                atkUp = 20;
                break;
            case "6":
                atkUp = 40;
                break;
            case "7":
                atkUp = 60;
                break;
            case "8":
                atkUp = 75;
                break;
            case "9":
                atkUp = 90;
                break;
            case "10":
                atkUp = 100;
                break;

        }
        switch (pre) {
            case "小于0":
            case "":
                atkDown = 0;
                break;
            case "5":
                atkDown = 20;
                break;
            case "6":
                atkDown = 40;
                break;
            case "7":
                atkDown = 60;
                break;
            case "8":
                atkDown = 75;
                break;
            case "9":
                atkDown = 90;
                break;
            case "10":
                atkDown = 100;
                break;

        }
        if (mList != null) {
            for (int i = 0;i < mList.size();i ++){
                if (mList.get(i).getBuffName().equals("atk_up")) {
                    //取数据
                    double data = mList.get(i).getDefaultDouble() + atkUp - atkDown;
                    //用数据
                    mList.get(i).setDefaultDouble(data);
                    //填数据
                    notifyDataSetChanged();
                }
            }
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
