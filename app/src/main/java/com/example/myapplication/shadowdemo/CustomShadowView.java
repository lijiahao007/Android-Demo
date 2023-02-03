package com.example.myapplication.shadowdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

public class CustomShadowView extends View {

    private Paint shadowPaint;

    public CustomShadowView(Context context) {
        super(context);
        initView();
    }

    public CustomShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        int shadowColor = getContext().getResources().getColor(R.color.color_33000000);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(Color.BLUE);
        float oneDp = DimenUtil.dp2px(getContext(), 1);
        shadowPaint.setShadowLayer(oneDp * 5, oneDp * 5, oneDp * 5, shadowColor);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(125, 125, 225, 225, shadowPaint);
    }
}
