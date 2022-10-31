package com.example.myapplication.rubbish;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.Random;


public class MyViewGroup_1 extends RelativeLayout {

    private static Random random = new Random();

    private static final String TAG = "MyViewGroup_1" + String.valueOf(random.nextInt(1000));

    public MyViewGroup_1(Context context) {
        super(context);
    }

    public MyViewGroup_1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup_1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "before super.dispatchTouchEvent() call" + "MotionEvent:" + ev.getAction());
        boolean b = super.dispatchTouchEvent(ev);
        Log.i(TAG, "after super.dispatchTouchEvent() call " + "  super.dispatchTouchEvent(ev):" + b);
        return b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "before super.onInterceptTouchEvent() call" + "MotionEvent:" + ev.getAction());
        boolean b = super.onInterceptTouchEvent(ev);
        Log.i(TAG, "after super.onInterceptTouchEvent() call " + " super.onInterceptTouchEvent:" + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "before super.onTouchEvent() call" + "MotionEvent:" + event.getAction());
        boolean b = super.onTouchEvent(event);
        Log.i(TAG, "after super.onTouchEvent() call " + "MotionEvent:" + event.getAction() + " super.onTouchEvent:" + b);
        return true;
    }
}
