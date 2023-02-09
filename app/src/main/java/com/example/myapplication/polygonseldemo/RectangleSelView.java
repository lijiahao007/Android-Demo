package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

public class RectangleSelView extends PolygonSelView {

    private String TAG = "RectangleSelView";
    private Paint cornerPaint;
    private float cornerLength;
    private Path cornerPath;
    private Drawable deleteDrawable;
    private float cornerLineWidth;

    public RectangleSelView(Context context) {
        super(context);
        initView();
    }

    public RectangleSelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RectangleSelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        pointList.clear();

        // 左上角
        pointList.add(new Point(100, 100, 1));
        pointList.add(new Point(500, 100, 2));
        // 右下角
        pointList.add(new Point(500, 500, 3));
        pointList.add(new Point(100, 500, 4));


        cornerPaint = new Paint();
        cornerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        cornerLineWidth = DimenUtil.dp2px(getContext(), 5);
        cornerPaint.setStrokeWidth(cornerLineWidth);

        cornerLength = DimenUtil.dp2px(getContext(), 10);
        cornerPath = new Path();

        setEditMode(true);

        deleteDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.preview_btn_closeyt_multi, null);
    }


    @Override
    protected void drawPoint(Canvas canvas, float x, float y, int index) {
        Log.i(TAG, "drawPoint");
        Point leftTopPoint = pointList.get(0);
        Point rightBottomPoint = pointList.get(2);

        // 1. 左上角
        cornerPath.reset();
        cornerPath.moveTo(leftTopPoint.x + cornerLength, leftTopPoint.y);
        cornerPath.lineTo(leftTopPoint.x - cornerLineWidth / 2, leftTopPoint.y);
        cornerPath.moveTo(leftTopPoint.x, leftTopPoint.y);
        cornerPath.lineTo(leftTopPoint.x, leftTopPoint.y + cornerLength);
        canvas.drawPath(cornerPath, cornerPaint);

        // 2. 左下角
        cornerPath.reset();
        cornerPath.moveTo(leftTopPoint.x + cornerLength, rightBottomPoint.y);
        cornerPath.lineTo(leftTopPoint.x - cornerLineWidth / 2, rightBottomPoint.y);
        cornerPath.moveTo(leftTopPoint.x, rightBottomPoint.y);
        cornerPath.lineTo(leftTopPoint.x, rightBottomPoint.y - cornerLength);
        canvas.drawPath(cornerPath, cornerPaint);

        // 3. 右下角
        cornerPath.reset();
        cornerPath.moveTo(rightBottomPoint.x - cornerLength, rightBottomPoint.y);
        cornerPath.lineTo(rightBottomPoint.x + cornerLineWidth / 2, rightBottomPoint.y);
        cornerPath.moveTo(rightBottomPoint.x, rightBottomPoint.y);
        cornerPath.lineTo(rightBottomPoint.x, rightBottomPoint.y - cornerLength);
        canvas.drawPath(cornerPath, cornerPaint);


        // 4. 右上角
        deleteDrawable.setBounds(
                (int) (rightBottomPoint.x - pointRadius),
                (int) (leftTopPoint.y - pointRadius),
                (int) (rightBottomPoint.x + pointRadius),
                (int) (leftTopPoint.y + pointRadius));
        deleteDrawable.draw(canvas);
    }

}
