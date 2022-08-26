package com.example.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView2 extends View {

    private OnClickListener onClickListener;
    private Paint paint;
    private int measuredHeight;
    private int measuredWidth;
    private Rect rect;

    public CustomView2(Context context) {
        this(context, null);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
        Log.i("CustomView2", "initView");

        this.setOnClickListener(view -> {
            if (this.onClickListener != null) {
                this.onClickListener.onClick(this);
            }
        });

        performClick();
    }

    public void setListener(OnClickListener listener) {
        Log.i("CustomView2", "setListener");
        this.onClickListener = listener;
    }
    


}
