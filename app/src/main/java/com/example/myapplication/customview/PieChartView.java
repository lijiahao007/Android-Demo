package com.example.myapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PieChartView extends View {

    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    // 饼状图初始绘制角度
    private float mStartAngle = 0;

    // 数据
    private ArrayList<PieData> mData;

    // 宽高
    private int mWidth, mHeight;

    private Paint mPaint = new Paint(); // 画笔
    // PieChart大小
    RectF rect = new RectF();
    private Paint textPaint = new Paint();
    private Paint strokePaint = new Paint();


    public PieChartView(Context context) {
        super(context);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mPaint.setStyle(Paint.Style.FILL); //画笔填充模式
        mPaint.setAntiAlias(true); // 抗锯齿
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setStyle(Paint.Style.FILL);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        Log.i("PieChartViews",  "onSizeChanged   w:" + w + "  h: " + h + " oldw:" + oldw + " oldh:" + oldh);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);      //取出宽度的确切数值
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);      //取出宽度的测量模式

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        Log.i("PieChartView", "onMeasure   widthsize:" + widthsize + "  widthmode: " + widthmode + " heightsize:" + heightsize + " heightmode:" + heightmode);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // View绘制
        super.onDraw(canvas);
        if (null == mData)
            return;
        float currentStartAngle = mStartAngle;                    // 当前起始角度
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f);        // 将画布坐标原点移动到中心位置
        float radius = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);  // 饼状图半径
        rect.set(-radius, -radius, radius, radius);                     // 饼状图绘制区域
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rect, currentStartAngle, pie.getAngle(), true, mPaint);

            float halfRadius = (float) (radius * 0.6);
            float degree = (float) (currentStartAngle + pie.getAngle() * 0.5);
            int x = (int) (halfRadius * Math.cos(degree / 180 * Math.PI));
            int y = (int) (halfRadius * Math.sin(degree / 180 * Math.PI));
            mPaint.setColor(Color.BLACK);
            canvas.drawText(pie.getName(), x, y, textPaint);

            currentStartAngle += pie.getAngle();
        }

        // 绘制外部36等分圆环
        canvas.drawCircle(0, 0, radius, strokePaint);
        canvas.drawCircle(0,0, radius + 10, strokePaint);
        for (int i = 0; i < 36; i++) {
            canvas.drawLine(radius, 0, radius + 10, 0, strokePaint);
            canvas.rotate(10); // 旋转canvas
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i("PieChartView", "onLayout   changed:" + changed + "  left: " + left + " top:" + top + " right:" + right + " bottom:" + bottom);

    }

    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();   // 刷新
    }

    // 设置数据
    public void setData(ArrayList<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();   // 刷新
    }

    // 初始化数据
    private void initData(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0)   // 数据有问题 直接返回
            return;

        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            sumValue += pie.getValue();       //计算数值和

            int j = i % mColors.length;       //设置颜色
            pie.setColor(mColors[j]);
        }

        float sumAngle = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pie = mData.get(i);

            float percentage = pie.getValue() / sumValue;   // 百分比
            float angle = percentage * 360;                 // 对应的角度

            pie.setPercentage(percentage);                  // 记录百分比
            pie.setAngle(angle);                            // 记录角度大小
            sumAngle += angle;

            Log.i("angle", "" + pie.getAngle());
        }
    }

}
