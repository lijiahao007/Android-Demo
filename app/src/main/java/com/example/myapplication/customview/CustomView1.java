package com.example.myapplication.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;

public class CustomView1 extends View {

    private Picture mPicture = new Picture();
    private RectF bound = new RectF();
    Paint paint = new Paint();
    private Bitmap bitmap;

    public CustomView1(Context context) {
        super(context);
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        recording();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pic);
    }

    // 2.录制内容方法
    private void recording() {
        // 开始录制 (接收返回值Canvas)
        Canvas canvas = mPicture.beginRecording(500, 500);
        // 创建一个画笔
        Paint paint = new Paint();

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);


        // 在Canvas中具体操作
        canvas.translate(100, 100);
        canvas.drawCircle(0, 0, 90, paint);

        paint.setColor(Color.BLACK);
        canvas.translate(150, 150);
        canvas.drawCircle(0, 0, 90, paint);

        mPicture.endRecording();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量view大小
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        Log.i("CustomView1", "onMeasure   widthsize:" + widthsize + "  widthmode: " + widthmode + " heightsize:" + heightsize + " heightmode:" + heightmode);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        视图大小发生改变时调用。(可以看作是View真是的宽高)
        Log.i("CustomView1", "onSizeChanged   w:" + w + "  h: " + h + " oldw:" + oldw + " oldh:" + oldh);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 确定自身位置 (这里才可以获取具体的位置。。)
        // 确定子View布局位置
        Log.i("CustomView1", "onLayout   changed:" + changed + "  left: " + left + " top:" + top + " right:" + right + " bottom:" + bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // canvas 默认的原点在当前View的左上角
        canvas.drawPicture(mPicture);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        bound.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(bound, paint);
        Log.i("CustomView1", "onDraw   " + "  left: " + getLeft() + " top:" + getTop() + " right:" + getRight() + " bottom:" + getBottom() + " height:" + getHeight() + " width:" + getWidth());
        canvas.drawBitmap(bitmap, 0, 300, paint);
    }
}
