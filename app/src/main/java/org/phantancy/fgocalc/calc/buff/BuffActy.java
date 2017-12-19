package org.phantancy.fgocalc.calc.buff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.adapter.BuffAdapter;
import org.phantancy.fgocalc.base.BaseActy;
import org.phantancy.fgocalc.item.BuffItem;
import org.phantancy.fgocalc.item.BuffsItem;
import org.phantancy.fgocalc.item.ServantItem;
import org.phantancy.fgocalc.util.BaseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by HATTER on 2017/11/6.
 */

public class BuffActy extends BaseActy implements BuffContract.View {

    @BindView(R.id.ab_rv_buff)
    RecyclerView abRvBuff;
    @BindView(R.id.status_bar)
    LinearLayout statusBar;
    @BindView(R.id.ab_btn_buff)
    Button abBtnBuff;
    @BindView(R.id.ab_btn_clean)
    Button abBtnClean;
    @BindView(R.id.ab_et_close)
    EditText abEtClose;
    private BuffAdapter adapter;
    private List<BuffItem> list = new ArrayList<>();
    private BuffContract.Presenter mPresenter;
    private ServantItem servantItem;
    private BuffsItem buffsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_buff);
        ButterKnife.bind(this);
        servantItem = (ServantItem) getIntent().getSerializableExtra("servantItem");
        buffsItem = (BuffsItem) getIntent().getSerializableExtra("buffsItem");
        //创建presenter
        mPresenter = new BuffPresenter(this, ctx);
        initStatusBar();
        setListener();
        if (servantItem != null) {
            list = mPresenter.getBuffList(servantItem, buffsItem);
            adapter = new BuffAdapter(list, ctx);
            GridLayoutManager gl = new GridLayoutManager(ctx, 3);
            abRvBuff.setLayoutManager(gl);
            abRvBuff.setAdapter(adapter);
        }
    }

    @Override
    public void setPresenter(BuffContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void initStatusBar() {
        int height = BaseUtils.getStatusBarHeight(ctx);
        statusBar.setPadding(0, height, 0, 0);
        if (Build.VERSION.SDK_INT == 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setListener() {
        abBtnBuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buffFinish();
            }
        });
        abBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.cleanBuffs();
            }
        });
    }

    //重载返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            buffFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void buffFinish() {
        buffsItem = mPresenter.getBuffsItem(list);
        Intent i = new Intent();
        i.putExtra("buffsItem", buffsItem);
        setResult(RESULT_OK, i);
        closeKeybord(abEtClose,ctx);
        finish();
        overridePendingTransition(0, R.anim.push_bottom_out);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

}
