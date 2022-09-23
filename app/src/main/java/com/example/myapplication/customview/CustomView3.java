package com.example.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView3 extends View {

    private float lastY;
    private float lastX;

    public CustomView3(Context context) {
        this(context, null);
    }

    public CustomView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 1. 获取点击坐标
        float viewX = event.getX();
        float viewY = event.getY();
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        Log.i("CustomView3", "onTouch 点击位置坐标: viewX = " + viewX + ", viewY = " + viewY + ", rawX = " + rawX + ", rawY = " + rawY);

        // 2. 获取被点击的View的坐标
        int left = getLeft();
        int right = getRight();
        int top = getTop();
        int bottom = getBottom();
        Log.i("CustomView3", "onTouch 点击View的坐标: left = " + left + ", right = " + right + ", top = " + top + ", bottom = " + bottom);


        // 3. View 滑动方法1：在OnTouchEvent中获取坐标偏移，然后调用layout调整View坐标。
        //    缺点：不够丝滑
        Log.i("CustomView3", "action:" + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 记录下点击位置相较于
                lastX = viewX;
                lastY = viewY;
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = viewX - lastX;
                float offsetY = viewY - lastY;
                Log.i("CustomView3", "viewX:" + viewX + ", lastX:" + lastX + ", offsetX:" + offsetX);
                Log.i("CustomView3", "viewY:" + viewY + ", lastY:" + lastY + ", offsetY:" + offsetY);

                // ===== View滑动方法一
//                layout((int) (left + offsetX), (int) (top + offsetY), (int) (right + offsetX), (int) ((bottom + offsetY)));

                // ===== View滑动方法二 (与方法一实际是一样的。)
                offsetLeftAndRight((int) offsetX);
                offsetTopAndBottom((int) offsetY);

                break;
        }

        return true;
    }
}
