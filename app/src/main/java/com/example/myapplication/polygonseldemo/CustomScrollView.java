package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    static public boolean sIsReturnFalse = false;

    public static void setIsReturnFalse(boolean sIsReturnFalse) {
        CustomScrollView.sIsReturnFalse = sIsReturnFalse;
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return sIsReturnFalse ? false : super.onInterceptTouchEvent(ev);
    }
}