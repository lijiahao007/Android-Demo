package com.example.myapplication.rubbish;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {
    private static final String TAG = "MyView";

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "before  super.dispatchTouchEvent(ev):");
        boolean b = super.dispatchTouchEvent(event);
        Log.i(TAG, "after super.dispatchTouchEvent() call " + "MotionEvent:" + event.getAction() + "  super.dispatchTouchEvent(ev):" + b);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "before  super.onTouchEvent(ev):");
        boolean b = super.onTouchEvent(event);
        Log.i(TAG, "after super.onTouchEvent() call " + "MotionEvent:" + event.getAction() + " super.onTouchEvent:" + b);

        if (!this.isEnabled() && onDisableClick != null) {
            onDisableClick.onDisableClick(this);
        }
        return b;
    }

    public OnDisableClick onDisableClick;

    public interface OnDisableClick {
        public void onDisableClick(View view);
    }

}
