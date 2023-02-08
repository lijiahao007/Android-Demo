package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.util.LinkedList;
import java.util.ListIterator;

public class PolygonSelView extends View {

    private String TAG = "PolygonSelView";
    private Paint linePaint;
    private Paint textPaint;
    private Paint textBackgroundPaint;
    private int pointRadius;
    private int gravityField; // 重力场大小
    private Rect textRect;

    private LinkedList<Point> pointList = new LinkedList<>();
    private Paint fillPaint;
    private Path pointPath;

    private boolean isDeleteMode = false;
    private boolean isEditMode = false;

    boolean isDragging = false;
    int draggingIndex = -1;
    private RectF pathRectf;

    boolean isWholeDragging = false;
    float wholeDraggingX = 0f;
    float wholeDraggingY = 0f;

    public PolygonSelView(Context context) {
        super(context);
        initView();
    }

    public PolygonSelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PolygonSelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
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
        pathRectf = new RectF();

        pointList.add(new Point(100, 100, 1));
        pointList.add(new Point(100, 400, 2));
        pointList.add(new Point(300, 500, 3));
        pointList.add(new Point(400, 350, 4));
        pointList.add(new Point(400, 200, 5));
        pointList.add(new Point(200, 50, 6));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. 画Path
        pointPath.reset();
        if (pointList.size() > 1) {
            for (int i = 0; i < pointList.size(); i++) {
                Point point = pointList.get(i);
                if (i == 0) {
                    pointPath.moveTo(point.x, point.y);
                } else {
                    pointPath.lineTo(point.x, point.y);
                }
            }
        }
        pointPath.close();
        canvas.drawPath(pointPath, linePaint);
        canvas.drawPath(pointPath, fillPaint);

        // 2.  画点
        if (isEditMode || isDeleteMode) {
            for (int i = 0; i < pointList.size(); i++) {
                Point point = pointList.get(i);
                drawPoint(canvas, point.x, point.y, point.msg);
            }
        }
    }

    private void drawPoint(Canvas canvas, float x, float y, int index) {
        canvas.drawCircle(x, y, pointRadius, textBackgroundPaint);
        String indexStr = String.valueOf(index);
        textPaint.getTextBounds(indexStr, 0, indexStr.length(), textRect);
        float[] charWidth = new float[indexStr.length()];
        textPaint.getTextWidths(indexStr, charWidth);
        float width = 0;
        for (float ch : charWidth) {
            width += ch;
        }

        float textX = x - width / 1.7f;
        float textY = y + textRect.height() / 2f;
        canvas.drawText(indexStr, textX, textY, textPaint);
    }

    private int getPointFromPos(float x, float y) {
        ListIterator<Point> listIterator = pointList.listIterator(pointList.size());
        while (listIterator.hasPrevious()) {
            int index = listIterator.previousIndex();
            Point point = listIterator.previous();
            float dis = calPointDis(point.x, point.y, x, y);
            if (dis <= pointRadius + gravityField) {
                return index;
            }
        }
        return -1;
    }

    private float calPointDis(float x1, float y1, float x2, float y2) {
        return (float) (Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));
    }


    private void deletePoint(int index) {
        ListIterator<Point> listIterator = pointList.listIterator(index);
        if (listIterator.hasNext()) {
            listIterator.next();
            listIterator.remove();
        }

        while (listIterator.hasNext()) {
            Point point = listIterator.next();
            point.msg--;
        }
    }

    private boolean isPointInPath(Path path, float x, float y) {
        // 1. 计算包括path对应图形的矩形
        path.computeBounds(pathRectf, true);
        Log.i(TAG, "isPointInPath =" + pathRectf);

        // 2. 将区域设置为路径和剪辑所描述的区域。如果结果区域非空，则返回true。这将生成一个与路径绘制的像素相同的区域(没有反锯齿)。
        Region region = new Region();
        region.setPath(path, new Region((int) pathRectf.left, (int) pathRectf.top, (int) pathRectf.right, (int) pathRectf.bottom));
        return region.contains((int) x, (int) y);
    }

    private void moveAllPoint(float disX, float disY) {
        for (Point point : pointList) {
            point.x += disX;
            point.y += disY;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEditMode && !isDeleteMode) return editModeOnTouchEvent(event);
        else if (isDeleteMode && !isEditMode) return deleteModeOnTouchEvent(event);
        else {
            return super.onTouchEvent(event);
        }
    }

    private boolean editModeOnTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int pointIndex = getPointFromPos(x, y);
                Log.i(TAG, "ACTION_DOWN " + "pointIndex = " + pointIndex);
                if (pointIndex == -1) {
                    // 点击空位创建一个新的点
                    pointList.add(new Point(x, y, pointList.size() + 1));
                    invalidate();
                } else {
                    // 不为空则进入拖拽状态
                    isDragging = true;
                    draggingIndex = pointIndex;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE 【" + draggingIndex + "】");
                if (isDragging && draggingIndex != -1) {
                    Point point = pointList.get(draggingIndex);
                    point.x = x;
                    point.y = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                isDragging = false;
                draggingIndex = -1;
                break;
        }

        return true;
    }

    private boolean deleteModeOnTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int pointIndex = getPointFromPos(x, y);
                Log.i(TAG, "ACTION_DOWN " + "pointIndex = " + pointIndex);
                if (pointIndex != -1) {
                    deletePoint(pointIndex);
                    invalidate();
                } else if (isPointInPath(pointPath, x, y)) {
                    isWholeDragging = true;
                    wholeDraggingX = x;
                    wholeDraggingY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isWholeDragging) {
                    moveAllPoint(x - wholeDraggingX, y - wholeDraggingY);
                    wholeDraggingX = x;
                    wholeDraggingY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isWholeDragging = false;
                break;
        }
        return true;
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
        invalidate();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
        invalidate();
    }


    public static class Point {
        float x;
        float y;
        int msg;

        public Point(float x, float y, int msg) {
            this.x = x;
            this.y = y;
            this.msg = msg;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
