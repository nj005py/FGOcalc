package org.phantancy.fgocalc.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import org.phantancy.fgocalc.R;

import static anet.channel.util.Utils.context;

/**
 * Created by HATTER on 2018/4/15.
 */

public class TalkDialog extends PopupWindow {
    private View conentView;
    private Activity mContext;
    private ImageView ivPortrait;

    public TalkDialog(final Activity context,int resId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_talk, null);
        mContext = context;
        ivPortrait = conentView.findViewById(R.id.dt_iv_portrait);
        ivPortrait.setImageDrawable(ContextCompat.getDrawable(context,resId));
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(h / 2);
//        this.setHeight(300);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.anim.slide_in_from_top);
        //popup windows以外透明
//        backgroundAlpha(0.7f);
        findViews();
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent, 0, 0);
//            this.showAtLocation(parent,Gravity.TOP | Gravity.START, 0, 0);
            int windowPos[] = calculatePopWindowPos(parent, conentView);
            int xOff = 5;// 可以自己调整偏移
            windowPos[0] -= xOff;
            showAtLocation(parent, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
        } else {
            this.dismiss();
        }
    }

    private void findViews() {

    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
         // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = mContext.getWindowManager().getDefaultDisplay().getHeight();
        final int screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }
}
