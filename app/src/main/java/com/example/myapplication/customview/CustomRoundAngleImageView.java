package com.example.myapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;


/**
 * desc:
 * create by LYQ on 2019/10/19 15:15
 */
public class CustomRoundAngleImageView extends AppCompatImageView {

    private float width;
    private float height;

    private int defaultRadius = 0;
    private int radius;
    private int leftTopRadius;
    private int rightTopRadius;
    private int rightBottomRadius;
    private int leftBottomRadius;

    private Path path;


    public CustomRoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomRoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomRoundAngleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // 读取配置
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundAngleImageView);
        radius = array.getDimensionPixelOffset(R.styleable.CustomRoundAngleImageView_radius, defaultRadius);
        leftTopRadius = array.getDimensionPixelOffset(R.styleable.CustomRoundAngleImageView_left_top_radius, defaultRadius);
        rightTopRadius = array.getDimensionPixelOffset(R.styleable.CustomRoundAngleImageView_right_top_radius, defaultRadius);
        rightBottomRadius = array.getDimensionPixelOffset(R.styleable.CustomRoundAngleImageView_right_bottom_radius, defaultRadius);
        leftBottomRadius = array.getDimensionPixelOffset(R.styleable.CustomRoundAngleImageView_left_bottom_radius, defaultRadius);

        //如果四个角的值没有设置，那么就使用通用的radius的值。
        if (defaultRadius == leftTopRadius) {
            leftTopRadius = radius;
        }
        if (defaultRadius == rightTopRadius) {
            rightTopRadius = radius;
        }
        if (defaultRadius == rightBottomRadius) {
            rightBottomRadius = radius;
        }
        if (defaultRadius == leftBottomRadius) {
            leftBottomRadius = radius;
        }

        array.recycle();

        path = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float total;
        float realLeftTopRadius = 0;
        float realRightTopRadius = 0;
        float realLeftBottomRadius = 0;
        float realRightBottomRadius = 0;

        if (Math.max(leftTopRadius + rightTopRadius, leftBottomRadius + rightBottomRadius) > width ||
                Math.max(leftTopRadius + leftBottomRadius, rightTopRadius + rightBottomRadius) > height) {
            float base = Math.min(width, height);
            total = leftTopRadius + rightTopRadius + leftBottomRadius + rightBottomRadius * 1f;
            if (total != 0) {
                realLeftTopRadius = base * (leftTopRadius / total);
                realRightTopRadius = base * (rightTopRadius / total);
                realLeftBottomRadius = base * (leftBottomRadius / total);
                realRightBottomRadius = base * (rightBottomRadius / total);
            }
        } else {
            realLeftTopRadius = leftTopRadius;
            realRightTopRadius = rightTopRadius;
            realLeftBottomRadius = leftBottomRadius;
            realRightBottomRadius = rightBottomRadius;
        }
        Log.i("RoundAngleImageView", "leftTopRadius:" + leftTopRadius + ",rightTopRadius:" + rightTopRadius + ",rightBottomRadius:" + rightBottomRadius + ",leftBottomRadius:" + leftBottomRadius);
        Log.i("RoundAngleImageView", "realLeftTopRadius:" + realLeftTopRadius + ",realRightTopRadius:" + realRightTopRadius + ",realRightBottomRadius:" + realRightBottomRadius + ",realLeftBottomRadius:" + realLeftBottomRadius);
        Log.i("RoundAngleImageView", "width:" + width + ",height:" + height);

        //四个角：右上，右下，左下，左上
        path.moveTo(realLeftTopRadius, 0);
        if (width - realLeftTopRadius > realRightTopRadius) {
            path.lineTo(width - realLeftTopRadius, 0);
        }
        path.quadTo(width, 0, width, realRightTopRadius);

        if (height-realRightTopRadius > realRightBottomRadius) {
            path.lineTo(width, height - realRightBottomRadius);
        }
        path.quadTo(width, height, width - realRightBottomRadius, height);

        if (realLeftBottomRadius < width - realRightBottomRadius) {
            path.lineTo(realLeftBottomRadius, height);
        }
        path.quadTo(0, height, 0, height - realLeftBottomRadius);

        if (realLeftTopRadius < height - realLeftBottomRadius) {
            path.lineTo(0, realLeftTopRadius);
        }
        path.lineTo(0, realLeftTopRadius);
        path.quadTo(0, 0, realLeftTopRadius, 0);

        canvas.clipPath(path);
        super.onDraw(canvas);
    }

}
