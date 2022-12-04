package com.example.myapplication.timebardemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.util.Locale;

public class TextTimeLine extends View {

    private Paint textPaint;
    private int textWidth;
    private float textHeight;
    private float dividerHeight;

    public TextTimeLine(Context context) {
        super(context);
        initView();
    }

    public TextTimeLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TextTimeLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        int textColor = getContext().getResources().getColor(R.color.color_333333);
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(DimenUtil.sp2px(getContext(), 12));

        textWidth = 0;
        for (int i = 0; i <= 24; i++) {
            String time = String.format(Locale.getDefault(), "%02d:00", i);
            Rect textBound = new Rect();
            textPaint.getTextBounds(time, 0, time.length(), textBound);
            textWidth = Math.max(textBound.width(), textWidth);
        }

        dividerHeight = DimenUtil.dp2px(getContext(), 4);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textHeight = -fontMetrics.ascent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = widthSize;
        if (widthMode != MeasureSpec.EXACTLY) {
            width = textWidth + Math.max(getPaddingEnd(), getPaddingRight());
        }
        int height = heightSize;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = heightSize + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = (int) (getHeight() - textHeight);

        float chunkHeight = (height - 23 * dividerHeight) / 24;

        float curYOffset = textHeight;

        for (int i = 0; i <= 24; i++) {
            String time = String.format(Locale.getDefault(), "%02d:00", i);
            canvas.drawText(time, 0, curYOffset, textPaint);

            if (i == 0 || i == 24) {
                curYOffset += chunkHeight + dividerHeight / 2;
            } else {
                curYOffset += chunkHeight + dividerHeight;
            }
        }
    }

}
