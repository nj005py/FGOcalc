package org.phantancy.fgocalc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.calc.CalcActy;
import org.phantancy.fgocalc.common.UrlConstant;
import org.phantancy.fgocalc.database.DBManager;
import org.phantancy.fgocalc.item.OptionItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.item.TipItem;
import org.phantancy.fgocalc.util.GlideApp;
import org.phantancy.fgocalc.util.ToolCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PartySvtAdapter extends RecyclerView.Adapter<PartySvtAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private String TAG = getClass().getSimpleName();
    private List<ServantItem> mList;
    private Context ctx;
    private Activity acty;
    //    private LruCache<Integer, Drawable> imgCache;
    private Drawable defaultDrawable;
    private String avatarUrl = UrlConstant.AVATAR_URL;
    private OnItemDragListener onItemDragListener;//拖拽监听

    public PartySvtAdapter(List<ServantItem> mList, Context ctx, Activity acty) {
        this.mList = mList;
        this.ctx = ctx;
        this.acty = acty;
        defaultDrawable = ContextCompat.getDrawable(ctx, R.drawable.loading);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tvId, tvIdBg, tvNpClassification,tvNpClassificationBg,tvAtkHp;
        ConstraintLayout cv;
        ImageView ivNpColor;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.isc_cl_card);
            iv = itemView.findViewById(R.id.isc_iv_portrait);
            tvId = itemView.findViewById(R.id.isc_tv_id);
            tvIdBg = itemView.findViewById(R.id.isc_tv_id_bg);
            ivNpColor = itemView.findViewById(R.id.isc_iv_np_color);
            tvNpClassification = itemView.findViewById(R.id.isc_tv_np_classification);
            tvNpClassificationBg = itemView.findViewById(R.id.isc_tv_np_classification_bg);
        }
    }

    public void setItems(List<ServantItem> list) {
        if (list != null && mList != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        } else if (list != null && mList == null) {
            mList = list;
            notifyDataSetChanged();
        } else {
            if (mList != null) {
                mList.clear();
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servant_cardview, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ServantItem item = mList.get(position);
        TextView tvId = holder.tvId;
        TextView tvBg = holder.tvIdBg;
        ImageView ivAvatar = holder.iv;
        ImageView ivNpColor = holder.ivNpColor;
        TextView tvNpClassification = holder.tvNpClassification;
        TextView tvNpClassificationBg = holder.tvNpClassificationBg;
        TextView tvAtkHp = holder.tvAtkHp;

        final int id = item.getId();
        final int pos = position;
        String pic = item.getPic();
        String npColor = item.getTrump_color();
        String num = "No.";
        String all = new StringBuilder()
                .append("No.")
                .append(id)
                .toString();
        String npTypeCode = item.getNp_classification();
        String npClassification = "";
        String atkHp = new StringBuilder()
                .append("<font color='#E21918'>atk:")
                .append(item.getDefault_atk())
                .append("</font><br><font color='#7BAF44'>hp:")
                .append(item.getDefault_hp())
                .append("</font>")
                .toString();
        if (!TextUtils.isEmpty(npTypeCode)) {
            switch (npTypeCode) {
                case "one":
                    npClassification = "单体";
                    break;
                case "all":
                    npClassification = "全体";
                    break;
                case "support":
                    npClassification = "辅助";
                    break;
            }
        }
        if (!TextUtils.isEmpty(atkHp)) {
            tvAtkHp.setText(Html.fromHtml(atkHp), TextView.BufferType.SPANNABLE);
        }
        //头像资源id
//        int resId = ctx.getResources().getIdentifier("image" + item.getId(), "drawable", ctx.getPackageName());
        int resId = getResIdByName(new StringBuilder()
                .append("image")
                .append(id)
                .toString());

        //设置头像
        if (resId != 0) {
            GlideApp.with(ctx)
                    .load(resId)
                    .dontAnimate()
                    .placeholder(defaultDrawable)
                    .into(ivAvatar);
        } else {
            ivAvatar.setImageDrawable(defaultDrawable);
            if (!TextUtils.isEmpty(pic)) {
                //如果数据库里有图则从数据库里读图
                //将Base64串转化为位图
                GetDatabasePic getDatabasePic = new GetDatabasePic(pic, ivAvatar);
                getDatabasePic.execute();
            } else {
                //否则从网络获取图片
                String strNum = "";
                if (id > 0 && id < 10) {
                    strNum = new StringBuilder().append("00").append(id).toString();
                } else if (id >= 10 && id < 100) {
                    strNum = new StringBuilder().append("0").append(id).toString();
                } else {
                    strNum = new StringBuilder().append(id).toString();
                }
                //从fgowiki获取头像
                String url = new StringBuilder().append(avatarUrl).append(strNum).append(".jpg").toString();
                GetNetImage getNetImage = new GetNetImage(ctx, id, ivAvatar, url);
                getNetImage.execute();
            }
        }
        //设置宝具卡色
        if (TextUtils.isEmpty(npColor)) {
            ivNpColor.setVisibility(View.GONE);
        } else {
            ivNpColor.setVisibility(View.VISIBLE);
            switch (npColor) {
                case "a":
                    GlideApp.with(ctx)
                            .load(R.drawable.arts)
                            .placeholder(defaultDrawable)
                            .into(ivNpColor);
                    break;
                case "b":
                    GlideApp.with(ctx)
                            .load(R.drawable.buster)
                            .placeholder(defaultDrawable)
                            .into(ivNpColor);
                    break;
                case "q":
                    GlideApp.with(ctx)
                            .load(R.drawable.quick)
                            .placeholder(defaultDrawable)
                            .into(ivNpColor);
                    break;
            }
        }
        //设置编号
        SpannableStringBuilder builder = new SpannableStringBuilder(all);
        ForegroundColorSpan goldenSpan = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.colorGolden));
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.colorWhite));
        builder.setSpan(goldenSpan, 0, num.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(whiteSpan, num.length(), all.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvId.setText(builder);
        //设置描边
        TextPaint tp = tvBg.getPaint();
        tp.setStrokeWidth(10);
        tp.setStyle(Paint.Style.STROKE);
        tvBg.setText(all);
        tvBg.setTextColor(ContextCompat.getColor(ctx, R.color.colorBlack));
        //设置宝具类型
        if (!TextUtils.isEmpty(npClassification)) {
            tvNpClassification.setText(npClassification);
            tvNpClassificationBg.setText(npClassification);
            //设置描边
            TextPaint tpNpC = tvNpClassificationBg.getPaint();
            tpNpC.setStrokeWidth(10);
            tpNpC.setStyle(Paint.Style.STROKE);
            tvNpClassificationBg.setTextColor(ContextCompat.getColor(ctx, R.color.colorBlack));
            tvNpClassification.setVisibility(View.VISIBLE);
            tvNpClassificationBg.setVisibility(View.VISIBLE);
        }else{
            tvNpClassification.setVisibility(View.GONE);
            tvNpClassificationBg.setVisibility(View.GONE);
        }

        //拖拽
        holder.cv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    if (onItemDragListener != null) {
                        onItemDragListener.onDrag(holder);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(mList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    private class GetDatabasePic extends AsyncTask<Void, Void, Bitmap> {

        private String pic;
        private ImageView iv;

        public GetDatabasePic(String pic, ImageView iv) {
            this.pic = pic;
            this.iv = iv;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bmp = ToolCase.base642Bitmap(pic);
            return bmp;
        }
    }

    private class GetNetImage extends AsyncTask<Void, Void, Bitmap> {

        private Context ctx;
        private int id;
        private ImageView iv;
        private String url;

        public GetNetImage(Context ctx, int id, ImageView iv, String url) {
            this.ctx = ctx;
            this.id = id;
            this.iv = iv;
            this.url = url;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                iv.setImageBitmap(bitmap);
            }
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bmp = null;
            try {
                bmp = Glide
                        .with(ctx)
                        .asBitmap()
                        .load(url)
                        .submit()
                        .get();
                if (bmp != null) {
                    String img = ToolCase.bitmap2Base64(bmp);
                    try {
                        DBManager.getInstance().getDatabase();
                        DBManager.getInstance().saveImage(id, img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        DBManager.getInstance().closeDatabase();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return bmp;
        }
    }

    private int getResIdByName(String name) {
        if (TextUtils.isEmpty(name)) {
            return 0;
        } else {
            try {
                Class c = R.drawable.class;
                Field field = c.getField(name);
                int resId = field.getInt(null);
                return resId;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        this.onItemDragListener = onItemDragListener;
    }

    public interface OnItemDragListener {
        void onDrag(ViewHolder holder);
    }
}
