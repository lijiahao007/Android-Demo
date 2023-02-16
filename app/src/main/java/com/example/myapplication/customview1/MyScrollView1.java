package com.example.myapplication.customview1;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class MyScrollView1 extends View {
    private Paint paint;


    public MyScrollView1(Context context) {
        super(context);
        initView();
    }

    public MyScrollView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyScrollView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.Red));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        canvas.drawRect(width / 2f - 50, height / 2f - 50, width / 2f + 50, height / 2f + 50, paint);
    }

    public void smoothMoveTo(float destX, float destY) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(this, "translationX", 0, 100);
        translationX.setDuration(1000);
        translationX.start();
    }
}
