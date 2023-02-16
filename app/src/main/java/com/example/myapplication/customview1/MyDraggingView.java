package com.example.myapplication.customview1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyDraggingView extends View {

    private String TAG = "MyDraggingView";

    public MyDraggingView(Context context) {
        super(context);
        initView();
    }

    public MyDraggingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyDraggingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    float lastX = 0;
    float lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX(); // 注意要用rawX，即相对于屏幕的距离
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = rawX - lastX;
                float deltaY = rawY - lastY;
                Log.i(TAG, "deltaX=" + deltaX + "  deltaY=" + deltaY);
                float translationX = getTranslationX() + deltaX;
                float translationY = getTranslationY() + deltaY;
                setTranslationX(translationX);
                setTranslationY(translationY);
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }  
        lastX = rawX;
        lastY = rawY;

        return true;
    }
}
