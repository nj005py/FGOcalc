package com.phantancy.fgocalc.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phantancy.fgocalc.R;
import com.phantancy.fgocalc.adapter.ItemAdapter;
import com.phantancy.fgocalc.item.Item;
import com.phantancy.fgocalc.item.ServantItem;


/**
 * Created by PY on 2016/12/1.
 */
public class ServantItemView extends LinearLayout implements ItemView{
    private TextView tvId,tvName,tvClassType,tvStar;
    private ImageView ivPortrait;
    private Context mContext;


    public ServantItemView(Context context) {
        super(context);
        mContext = context;
    }

    public ServantItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public void findViewsByIds() {
        tvId = (TextView)findViewById(R.id.is_tv_id);
        tvName = (TextView)findViewById(R.id.is_tv_name);
        tvClassType = (TextView)findViewById(R.id.is_tv_classtype);
        tvStar = (TextView)findViewById(R.id.is_tv_star);
        ivPortrait = (ImageView)findViewById(R.id.is_iv_portrait);
    }

    @Override
    public void setObject(Item item, int position, ItemAdapter.OnViewClickListener onViewClickListener) {
        if (item instanceof ServantItem) {
            ServantItem sItem = (ServantItem)item;
            if (sItem != null) {
                int sId = sItem.getId();
                String sName = sItem.getName();
                String sClassType = sItem.getClass_type();
                int sStar = sItem.getStar();
                tvId.setText("No.\n" + sId);
                tvName.setText(sName);
                tvClassType.setText(sClassType);
                tvStar.setText(sStar + "星");
                int resId = getResources().getIdentifier("image" + sId,"mipmap",mContext.getPackageName());
                ivPortrait.setImageResource(resId);
            }
        }
    }
}
