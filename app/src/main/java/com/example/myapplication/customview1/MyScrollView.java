package com.example.myapplication.customview1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class MyScrollView extends View {
    private String TAG = "MyScrollView";
    private Scroller mScroller;
    private Paint paint;

    public MyScrollView(Context context) {
        super(context);
        initView();
    }

    public MyScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mScroller = new Scroller(getContext());
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
        Log.i(TAG, "smoothMoveTo(" + destX + "," +destY + ")");
        // 获取当前view的左边缘
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        float deltaX = destX - scrollX;
        float deltaY = destY - scrollY;
        mScroller.startScroll(scrollX, scrollY, (int) deltaX, (int) deltaY, 1000);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.i(TAG, "computeScroll(true) " + mScroller.getCurrX() + "," + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            Log.i(TAG, "computeScroll(false)");
        }
    }
}
