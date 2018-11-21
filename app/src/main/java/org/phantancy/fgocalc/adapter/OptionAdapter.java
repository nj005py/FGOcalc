package org.phantancy.fgocalc.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.dialog.TipDialog;
import org.phantancy.fgocalc.event.DatabaseEvent;
import org.phantancy.fgocalc.item.OptionItem;
import org.phantancy.fgocalc.util.ToolCase;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ItemViewHolder> {

    private Context ctx;
    private List<OptionItem> mList;
    private TextView tvTip;
    private TipDialog d;

    //不接受临时追加选项
    public OptionAdapter(Context ctx, List<OptionItem> mList, TextView tvTip, TipDialog d) {
        this.ctx = ctx;
        this.mList = mList;
        this.d = d;
        this.tvTip = tvTip;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView tvOption;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvOption = itemView.findViewById(R.id.io_tv_option);
        }

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_option,parent,false);
        ItemViewHolder holder = new ItemViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (holder != null) {
            OptionItem item = mList.get(position);
            if (item != null) {
                TextView tvOption = holder.tvOption;
                //设置选项
                ToolCase.setViewValue(tvOption,item.getContent());
                //如果有链接需要打开，走下面流程
                final String url = item.getUrl();
                tvOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(url)) {
                            d.dismiss();
                        }else if (url.equals("reload_db")) {
                            EventBus.getDefault().post(new DatabaseEvent(true));
                            d.dismiss();
                        }else{
                            int error = ToolCase.openBrowser(ctx,url);
                            switch (error) {
                                case 1:
                                    ToolCase.setViewValue(tvTip,"未检测到网络");
                                    break;
                                case 2:
                                    ToolCase.setViewValue(tvTip,"发生未知错误");
                                    break;
                            }
                            d.dismiss();
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
