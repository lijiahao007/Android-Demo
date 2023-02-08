package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.util.LinkedList;

public class RectangleSelView extends View {

    private String TAG = "PolygonSelView";
    private Paint linePaint;
    private Paint textPaint;
    private Paint textBackgroundPaint;
    private int pointRadius;
    private int gravityField; // 重力场大小
    private Rect textRect;

    private LinkedList<PolygonSelView.Point> pointList = new LinkedList<>();
    private Paint fillPaint;
    private Path pointPath;
    private boolean isDeleteMode = false;
    private boolean isEditMode = false;

    boolean isDragging = false;
    int draggingIndex = -1;


    public RectangleSelView(Context context) {
        super(context);
    }

    public RectangleSelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleSelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        int lineColor = getResources().getColor(R.color.Red);
        int textColor = getResources().getColor(R.color.white);
        int textBg = getResources().getColor(R.color.Red);

        float lineWidth = DimenUtil.dp2px(getContext(), 1);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWidth);

        textPaint = new Paint();
        textPaint.setTextSize(DimenUtil.sp2px(getContext(), 12));
        textPaint.setColor(textColor);
        textPaint.setFakeBoldText(true);

        textBackgroundPaint = new Paint();
        textBackgroundPaint.setColor(textBg);
        textBackgroundPaint.setStyle(Paint.Style.FILL);

        pointRadius = (int) DimenUtil.dp2px(getContext(), 10);
        gravityField = (int) DimenUtil.dp2px(getContext(), 10);
        textRect = new Rect();

        fillPaint = new Paint();
        fillPaint.setColor(getResources().getColor(R.color.TrunsRed));
        fillPaint.setStyle(Paint.Style.FILL);

        pointPath = new Path();

        pointList.add(new PolygonSelView.Point(100, 100, 1));
        pointList.add(new PolygonSelView.Point(100, 400, 2));
        pointList.add(new PolygonSelView.Point(300, 500, 3));
        pointList.add(new PolygonSelView.Point(400, 350, 4));
        pointList.add(new PolygonSelView.Point(400, 200, 5));
        pointList.add(new PolygonSelView.Point(200, 50, 6));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }
}
