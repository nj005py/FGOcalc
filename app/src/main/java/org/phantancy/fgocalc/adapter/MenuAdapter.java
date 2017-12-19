package org.phantancy.fgocalc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.item.MenuItem;

import java.util.List;

/**
 * Created by PY on 2017/7/18.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<MenuItem> mList;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener = null;

    public MenuAdapter(Context context,List<MenuItem> list) {
        mContext = context;
        mList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_menu,parent,false);
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_menu, parent,
//                false));
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //将数据与item视图进行绑定，如果是MyViewHolder就加载网络图片，如果是MyViewHolder2就显示页数
        if(holder instanceof MyViewHolder){
            final MenuItem item = mList.get(position);
//            ((MyViewHolder) holder).tvMenu.setText(mList.get(position).getMenuName());
            ((MyViewHolder) holder).tvMenu.setText(item.getMenuName());
            //将position保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);
        }

    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvMenu;

        public MyViewHolder(View view) {
            super(view);
            tvMenu = (TextView)view.findViewById(R.id.im_tv_menu);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
