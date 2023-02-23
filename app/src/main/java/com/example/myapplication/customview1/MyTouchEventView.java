package com.example.myapplication.customview1;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyTouchEventView extends View {
    private static final String TAG = "MyTouchEventView";

    public MyTouchEventView(Context context) {
        super(context);
    }

    public MyTouchEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTouchEventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.i(TAG, "ACTION_OUTSIDE");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                break;
        }
        return true;
    }
}
