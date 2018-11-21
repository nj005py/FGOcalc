package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.item.FilterItem;
import org.phantancy.fgocalc.util.ToolCase;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private Context ctx;
    private List<FilterItem> mList;

    public FilterAdapter(Context ctx, List<FilterItem> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    public void setItems(List<FilterItem> list){
        if (list != null && mList != null) {
            mList.clear();
            mList.addAll(list);
        }else if (list != null && mList == null) {
            mList = list;
        }else {
            if (mList != null) {
                mList.clear();
            }
        }
        notifyDataSetChanged();
    }

    public List<FilterItem> getItems(){
        return mList;
    }

    public void clearFilter(){
        if (mList != null) {
            for (int i = 0;i < mList.size(); i ++) {
                mList.get(i).setReset(true);
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvFilter;
        Spinner spFilter;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFilter = itemView.findViewById(R.id.if_tv_filter);
            spFilter = itemView.findViewById(R.id.if_sp_filter);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_filter,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        if (mList != null && mList.size() > 0) {
            FilterItem item = mList.get(position);
            if (item != null) {
                String title = item.getTitle();
                String[] options = item.getOptions();
                final String[] valuesString = item.getValuesString();
                final int[] valuesInt = item.getValuesInt();
                final int type = item.getType();
                boolean reset = item.isReset();
                TextView tvFilter = holder.tvFilter;
                Spinner spFilter = holder.spFilter;
                ToolCase.setViewValue(tvFilter,title);
                ToolCase.spInitSimple(ctx,options,spFilter);
                if (reset) {
                    spFilter.setSelection(0);
                    mList.get(position).setReset(false);
                }
                spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        if (type == 0) {
                            mList.get(position).setValueString(valuesString[pos]);
                        }else if (type == 1){
                            mList.get(position).setValueInt(valuesInt[pos]);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        if (type == 0) {
                            mList.get(position).setValueString(valuesString[0]);
                        }else if (type == 1){
                            mList.get(position).setValueInt(valuesInt[0]);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
