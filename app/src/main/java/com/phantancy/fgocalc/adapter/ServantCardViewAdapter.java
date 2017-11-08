package com.phantancy.fgocalc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.calc.CalcActy;
import com.phantancy.fgocalc.item.ServantItem;
import com.phantancy.fgocalc.util.ToolCase;

import java.util.List;

/**
 * Created by HATTER on 2017/10/27.
 */

public class ServantCardViewAdapter extends RecyclerView.Adapter<ServantCardViewAdapter.ViewHolder>{
    private List<ServantItem> mList;
    private Context ctx;
    private TextView tvTip;
    private RelativeLayout rlTip;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvName,tvId,tvClassType,tvStar,tvIdBg;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.isc_cv_servant);
            iv = (ImageView)itemView.findViewById(R.id.isc_iv_portrait);
            tvName = (TextView)itemView.findViewById(R.id.isc_tv_name);
            tvId = (TextView)itemView.findViewById(R.id.isc_tv_id);
            tvIdBg = (TextView)itemView.findViewById(R.id.isc_tv_id_bg);
            tvClassType = (TextView)itemView.findViewById(R.id.isc_tv_classtype);
        }
    }

    public ServantCardViewAdapter(Context ctx,List<ServantItem> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }

    public ServantCardViewAdapter(List<ServantItem> mList, Context ctx, TextView tvTip, RelativeLayout rlTip) {
        this.mList = mList;
        this.ctx = ctx;
        this.tvTip = tvTip;
        this.rlTip = rlTip;
    }

    public void setItems(List<ServantItem> list){
        if (list != null && mList != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }else if (list != null && mList == null){
            mList = list;
            notifyDataSetChanged();
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servant_cardview,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServantItem item = mList.get(position);
        int resId = ctx.getResources().getIdentifier("image" + item.getId(),"mipmap",ctx.getPackageName());
        holder.iv.setImageResource(resId);
//        holder.tvId.setText("No." + item.getId());
//        holder.tvClassType.setText(item.getClass_type());
        holder.tvName.setText(item.getName());
        String num = "No.";
        String sId = "" + item.getId();
        String all = "No." + item.getId();
//        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE //前后都不包括
//        Spannable.SPAN_INCLUSIVE_EXCLUSIVE  //前包括后不包括
//        Spannable.SPAN_EXCLUSIVE_INCLUSIVE  //前不包括后包括
//        Spannable.SPAN_INCLUSIVE_INCLUSIVE  //前后都包括
        SpannableStringBuilder builder = new SpannableStringBuilder(all);
        ForegroundColorSpan goldenSpan = new ForegroundColorSpan(ContextCompat.getColor(ctx,R.color.colorGolden));
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ContextCompat.getColor(ctx,R.color.colorWhite));
//        BackgroundColorSpan blackSpan = new BackgroundColorSpan(ContextCompat.getColor(ctx,R.color.colorBlack));
        builder.setSpan(goldenSpan,0,num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(whiteSpan,num.length(),all.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvId.setText(builder);
        //设置描边
        TextPaint tp = holder.tvIdBg.getPaint();
        tp.setStrokeWidth(10);
        tp.setStyle(Paint.Style.STROKE);
        holder.tvIdBg.setText(all);
        holder.tvIdBg.setTextColor(ContextCompat.getColor(ctx,R.color.colorBlack));
        final int pos = position;
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServantItem item = (ServantItem) mList.get(pos);
                if (item.getId() != 999 && item.getId() != 9999) {
                    Intent sIntent = new Intent(ctx, CalcActy.class);
                    sIntent.putExtra("servants", item);
                    ctx.startActivity(sIntent);
                }else{
                    ToolCase.setViewValue(tvTip,"想了解更多请到百度type-moon吧，空谕吧");
                    rlTip.setVisibility(View.VISIBLE);
                    rlTip.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
