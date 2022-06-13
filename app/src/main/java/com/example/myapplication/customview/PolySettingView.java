package com.example.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class PolySettingView extends View {

    private int testPoint = 0;
    private int triggerRadius = 180;    // 触发半径为180px

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix

    private float[] src = new float[8];
    private float[] dst = new float[8];

    private Paint pointPaint;

    public PolySettingView(Context context) {
        super(context);
        initBitmapAndMatrix();
    }

    public PolySettingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBitmapAndMatrix();

    }

    public PolySettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmapAndMatrix();

    }

    public PolySettingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBitmapAndMatrix();
    }


    private void initBitmapAndMatrix() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_pic);

        // bitmap图片的四个点的坐标
        float[] temp = {0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下

        src = temp.clone();
        dst = temp.clone();

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true); // 抗锯齿
        pointPaint.setStrokeWidth(50);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND) ;

        mPolyMatrix = new Matrix();
        mPolyMatrix.setPolyToPoly(src, 0, src, 0, 4);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float tempX = event.getX();
            float tempY = event.getY();

            // 根据触控位置改变dst
            for (int i = 0; i < testPoint * 2; i += 2) {
                if (Math.abs(tempX - dst[i]) <= triggerRadius && Math.abs(tempY - dst[i + 1]) <= triggerRadius) { // 如果两个点比较近，就分开他们，避免重合
                    dst[i] = tempX - 100;
                    dst[i + 1] = tempY - 100;
                    break;  // 防止两个点的位置重合
                }
            }

            resetPolyMatrix(testPoint);
            invalidate();
        }

        return true;
    }

    public void resetPolyMatrix(int pointCount) {
        mPolyMatrix.reset();
        // 核心要点
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(100, 100);


        // 根据Matrix绘制一个变换后的图片
        Log.i("PolySettingView", mPolyMatrix.toShortString());
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);

        mPolyMatrix.mapPoints(dst, src);

        // 绘制触控点
        for (int i = 0; i < testPoint * 2; i += 2) {
            canvas.drawPoint(dst[i], dst[i + 1], pointPaint);
        }
    }

    public void setTestPoint(int testPoint) {
        this.testPoint = testPoint > 4 || testPoint < 0 ? 4 : testPoint;
        dst = src.clone();
        resetPolyMatrix(this.testPoint);
        invalidate();
    }
}
