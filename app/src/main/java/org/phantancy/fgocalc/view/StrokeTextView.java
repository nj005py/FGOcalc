package org.phantancy.fgocalc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class StrokeTextView extends AppCompatTextView {
    TextView outlineTextView = null;

    public StrokeTextView(Context context) {
        super(context);
        outlineTextView = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        outlineTextView = new TextView(context,attrs);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        outlineTextView = new TextView(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        TextPaint mPaint = outlineTextView.getPaint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8f);
        outlineTextView.setTextColor(Color.BLACK);
        outlineTextView.setGravity(getGravity());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        CharSequence outText = outlineTextView.getText();

        if (outText == null || !outText.equals(this.getText().toString())) {
            outlineTextView.setText(this.getText().toString());
            postInvalidate();
        }

        outlineTextView.measure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left,top,right,bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }
}
