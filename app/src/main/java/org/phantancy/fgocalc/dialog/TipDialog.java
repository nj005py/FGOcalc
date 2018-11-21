package org.phantancy.fgocalc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.OptionAdapter;
import org.phantancy.fgocalc.item.OptionItem;
import org.phantancy.fgocalc.item.TipItem;
import org.phantancy.fgocalc.util.ToolCase;
import org.phantancy.fgocalc.view.OptionDecoration;

import java.util.List;

public class TipDialog extends Dialog {
    private Context ctx;
    private TextView tvTip;
    private ImageView ivCharacter;
    private RecyclerView rvOption;
    private ScrollView svTip;
    private TipItem item;
    private OptionAdapter adapter;

    //私有构造函数
    public TipDialog(@NonNull Context context, TipItem i) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_tip);
        ctx = context;
        item = i;
        tvTip = findViewById(R.id.dt_tv_tip);
        ivCharacter = findViewById(R.id.dt_iv_character);
        rvOption = findViewById(R.id.dt_rv_options);
        svTip = findViewById(R.id.dt_sv_tip);
        init();
    }

    private void init() {
        if (item != null) {
            //设置提示与人物立绘
            if (item.isHasTip()) {
                String tip = item.getTip();
                int imgId = item.getImgId();
                if (!TextUtils.isEmpty(tip) && imgId != 0) {
                    ToolCase.setViewValue(tvTip, tip);
                    ivCharacter.setImageDrawable(ContextCompat.getDrawable(ctx, imgId));
                    ivCharacter.setVisibility(View.VISIBLE);
                    svTip.setVisibility(View.VISIBLE);
                    tvTip.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.push_left_in));
                    ivCharacter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                }
            }
            //设置选项
            if (item.isHasOption()) {
                List<OptionItem> list = item.getOptionList();
                if (list != null && 0 < list.size()) {
                    adapter = new OptionAdapter(ctx, list, tvTip,this);
                    LinearLayoutManager lm = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
                    OptionDecoration decoration = new OptionDecoration(20);
                    rvOption.setLayoutManager(lm);
                    rvOption.addItemDecoration(decoration);
                    rvOption.setAdapter(adapter);
                    rvOption.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
