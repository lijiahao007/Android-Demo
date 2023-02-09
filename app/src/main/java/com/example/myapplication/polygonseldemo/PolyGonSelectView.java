package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.util.ArrayList;
import java.util.ListIterator;

public class PolyGonSelectView extends View {
    private String TAG = "PolyGonSelectView";

    private final ArrayList<SelectShape> regionList = new ArrayList<>();

    public PolyGonSelectView(Context context) {
        super(context);
        initView();
    }

    public PolyGonSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PolyGonSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        Polygon polygon = new Polygon();
        Rectangle rectangle = new Rectangle();

        rectangle.setRegionSelect(false);
        polygon.setRegionSelect(true);


        regionList.add(polygon);
        regionList.add(rectangle);
    }

    private SelectShape getSelectRegion() {
        for (SelectShape region : regionList) {
            if (region.isRegionSelect) {
                return region;
            }
        }
        return null;
    }

    boolean isDragging = false;
    int draggingPointIndex = -1;
    float lastX = -1;
    float lastY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        SelectShape selectRegion = getSelectRegion();
        if (selectRegion == null) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int pointIndex = selectRegion.getNearPointFromPos(x, y);
                Log.i(TAG, "ACTION_DOWN " + "pointIndex = " + pointIndex);
                if (pointIndex != -1) {
                    // 不为空则进入拖拽状态
                    isDragging = true;
                    draggingPointIndex = pointIndex;
                    lastX = x;
                    lastY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE 【" + draggingPointIndex + "】");
                if (isDragging && draggingPointIndex != -1) {
                    selectRegion.movePoint(draggingPointIndex, x - lastX, y - lastY);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                isDragging = false;
                draggingPointIndex = -1;
                break;
        }


        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SelectShape shape : regionList) {
            shape.drawShape(canvas);
        }
    }


    abstract class SelectShape {
        protected ArrayList<Point> pointList = new ArrayList<>();
        protected boolean isRegionSelect = false;
        protected Path regionPath = new Path();
        private final RectF regionRectF = new RectF();


        protected int pointRadius = (int) DimenUtil.dp2px(getContext(), 10);
        protected int gravityField = (int) DimenUtil.dp2px(getContext(), 10); // 重力场大小


        abstract void addPoint(float x, float y);

        abstract void removePoint(int index);

        abstract void movePointTo(int index, float dstX, float dstY); // 移动到

        abstract void movePoint(int index, float distanceX, float distanceY); // x移动distanceX, y移动distanceY

        abstract void drawShape(Canvas canvas); // 画出图形

        // 判断某个点是否在该形状中
        boolean isPointInShape(float x, float y) {
            regionPath.computeBounds(regionRectF, true);
            Region region = new Region();
            region.setPath(regionPath, new Region((int) regionRectF.left, (int) regionRectF.top, (int) regionRectF.right, (int) regionRectF.bottom));
            return region.contains((int) x, (int) y);
        }

        public boolean isRegionSelect() {
            return isRegionSelect;
        }

        public void setRegionSelect(boolean regionSelect) {
            isRegionSelect = regionSelect;
        }

        public void moveAllPoint(float distanceX, float distanceY) {
            for (int i = 0; i < pointList.size(); i++) {
                movePoint(i, distanceX, distanceY);
            }
        }

        public int getNearPointFromPos(float x, float y) {
            ListIterator<Point> iterator = pointList.listIterator(pointList.size());
            while (iterator.hasPrevious()) {
                int index = iterator.previousIndex();
                Point point = iterator.previous();
                if (calPointDis(x, y, point.getX(), point.getY()) <= pointRadius + gravityField) {
                    return index;
                }
            }
            return -1;
        }

        private float calPointDis(float x1, float y1, float x2, float y2) {
            return (float) (Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));
        }

        private double calPointLineDis(float x0, float y0, float x1, float y1, float x2, float y2) {
            double space = 0;
            double a, b, c;
            a = calPointDis(x1, y1, x2, y2);// 线段的长度
            b = calPointDis(x1, y1, x0, y0);// (x1,y1)到点的距离
            c = calPointDis(x2, y2, x0, y0);// (x2,y2)到点的距离
            if (c <= 0.000001 || b <= 0.000001) {
                space = 0;
                return space;
            }
            if (a <= 0.000001) {
                space = b;
                return space;
            }
            if (c * c >= a * a + b * b) {
                space = b;
                return space;
            }
            if (b * b >= a * a + c * c) {
                space = c;
                return space;
            }
            double p = (a + b + c) / 2;// 半周长
            double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
            space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
            return space;
        }

        protected boolean isOverLayPoint(int index, float x, float y) {
            Point point = pointList.get(index);
            for (Point p : pointList) {
                if (point == p) {
                    continue;
                }
                if (calPointDis(p.getX(), p.getY(), x, y) <= pointRadius * 2) {
                    return true;
                }
            }
            return false;
        }
    }

    class Polygon extends SelectShape {
        private final Paint textBackgroundPaint;
        private final Paint linePaint;
        private final Paint textPaint;
        private final Paint selectRegionPaint;

        protected int gravityField; // 重力场大小

        private final Rect textRect;

        public Polygon() {
            int lineColor = getResources().getColor(R.color.Red);
            int textColor = getResources().getColor(R.color.white);
            int textBg = getResources().getColor(R.color.Red);
            int selectRegionColor = getResources().getColor(R.color.TrunsRed);
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

            selectRegionPaint = new Paint();
            selectRegionPaint.setColor(selectRegionColor);
            selectRegionPaint.setStyle(Paint.Style.FILL);

            textRect = new Rect();

            pointList.add(new Point(100, 100, 1));
            pointList.add(new Point(100, 400, 2));
            pointList.add(new Point(300, 500, 3));
            pointList.add(new Point(400, 350, 4));
            pointList.add(new Point(400, 200, 5));
            pointList.add(new Point(200, 50, 6));
        }

        @Override
        public void addPoint(float x, float y) {
            int size = pointList.size();
            Point point = new Point(x, y, size + 1);
            pointList.add(point);
        }


        @Override
        public void removePoint(int index) {
            ListIterator<Point> iterator = pointList.listIterator(index);
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            while (iterator.hasNext()) {
                Point point = iterator.next();
                point.index--;
            }
        }

        @Override
        public void movePointTo(int index, float dstX, float dstY) {
            if (isOverLayPoint(index, dstX, dstY)) {
                return;
            }

            Point point = pointList.get(index);
            point.x = dstX;
            point.y = dstY;
        }

        @Override
        public void movePoint(int index, float distanceX, float distanceY) {

            Point point = pointList.get(index);

            float newX = point.getX() + distanceX;
            float newY = point.getY() + distanceY;
            if (isOverLayPoint(index, newX, newY)) {
                return;
            }

            point.x = newX;
            point.y = newY;
        }

        @Override
        public void drawShape(Canvas canvas) {
            // 1. 画Path
            regionPath.reset();
            if (pointList.size() > 1) {
                for (int i = 0; i < pointList.size(); i++) {
                    Point point = pointList.get(i);
                    if (i == 0) {
                        regionPath.moveTo(point.x, point.y);
                    } else {
                        regionPath.lineTo(point.x, point.y);
                    }
                }
            }
            regionPath.close();
            canvas.drawPath(regionPath, linePaint);
            canvas.drawPath(regionPath, selectRegionPaint);

            // 2.  画点
            if (isRegionSelect) {
                for (int i = 0; i < pointList.size(); i++) {
                    Point point = pointList.get(i);
                    drawPoint(canvas, point.x, point.y, point.index);
                }
            }
        }

        private void drawPoint(Canvas canvas, float x, float y, int index) {
            Log.i(TAG, "drawPoint");
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
    }

    class Rectangle extends SelectShape {
        private final Paint linePaint;
        private final Paint textPaint;
        private final Paint selectRegionPaint;
        private Paint cornerPaint;
        private float cornerLength;
        private Path cornerPath;
        private float cornerLineWidth;
        private Drawable deleteDrawable;

        public Rectangle() {
            int lineColor = getResources().getColor(R.color.Red);
            int textColor = getResources().getColor(R.color.white);
            int textBg = getResources().getColor(R.color.Red);
            int selectRegionColor = getResources().getColor(R.color.TrunsRed);
            float lineWidth = DimenUtil.dp2px(getContext(), 1);
            cornerLineWidth = DimenUtil.dp2px(getContext(), 5);


            linePaint = new Paint();
            linePaint.setColor(lineColor);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(lineWidth);

            textPaint = new Paint();
            textPaint.setTextSize(DimenUtil.sp2px(getContext(), 12));
            textPaint.setColor(textColor);
            textPaint.setFakeBoldText(true);


            selectRegionPaint = new Paint();
            selectRegionPaint.setColor(selectRegionColor);
            selectRegionPaint.setStyle(Paint.Style.FILL);

            cornerPaint = new Paint();
            cornerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            cornerPaint.setStrokeWidth(cornerLineWidth);
            cornerPaint.setColor(lineColor);

            cornerLineWidth = DimenUtil.dp2px(getContext(), 5);
            cornerLength = DimenUtil.dp2px(getContext(), 10);
            cornerPath = new Path();

            deleteDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.preview_btn_closeyt_multi, null);

            addPoint(400, 400);
            addPoint(500, 600);
        }

        // 需要分别加入左上角 & 右下角的点
        @Override
        void addPoint(float x, float y) {
            if (pointList.size() == 0) {
                pointList.add(new Point(x, y, 1));
            } else if (pointList.size() == 1) {
                Point leftTop = pointList.get(0);
                Point rightBottom = new Point(x, y, 3);
                // 右上角
                pointList.add(new PointWrapper(rightBottom, leftTop, 2));
                pointList.add(rightBottom);
                pointList.add(new PointWrapper(leftTop, rightBottom, 4));
            }
        }

        @Override
        void removePoint(int index) {
            pointList.clear();
        }

        @Override
        void movePointTo(int index, float dstX, float dstY) {
            if (index < 0 || index >= pointList.size()) {
                throw new IndexOutOfBoundsException("index=" + index + " 越界");
            }

            Point point = pointList.get(index);
            point.setX(dstX);
            point.setY(dstY);
        }

        @Override
        void movePoint(int index, float distanceX, float distanceY) {
            if (index < 0 || index >= pointList.size()) {
                throw new IndexOutOfBoundsException("index=" + index + " 越界");
            }

            Point point = pointList.get(index);
            point.setX(point.getX() + distanceX);
            point.setY(point.getY() + distanceX);
        }

        @Override
        void drawShape(Canvas canvas) {
            Point[] cornerPoints = getCornerPoints();
            if (cornerPoints == null) {
                return;
            }

            regionPath.reset();
            if (cornerPoints.length == 4) {
                for (int i = 0; i < cornerPoints.length; i++) {
                    Point point = cornerPoints[i];
                    if (i == 0) {
                        regionPath.moveTo(point.getX(), point.getY());
                    } else {
                        regionPath.lineTo(point.getX(), point.getY());
                    }
                }
            }
            regionPath.close();
            canvas.drawPath(regionPath, linePaint);
            canvas.drawPath(regionPath, selectRegionPaint);

            if (!isRegionSelect) {
                return;
            }
            // 1. 左上角
            if (cornerPoints[0] != null) {
                Point point = cornerPoints[0];
                cornerPath.reset();
                cornerPath.moveTo(point.getX() + cornerLength, point.getY());
                cornerPath.lineTo(point.getX() - cornerLineWidth / 2, point.getY());
                cornerPath.moveTo(point.getX(), point.getY() - cornerLineWidth / 2);
                cornerPath.lineTo(point.getX(), point.getY() + cornerLength);
                canvas.drawPath(cornerPath, cornerPaint);
            }

            // 2. 右上角
            if (cornerPoints[1] != null) {
                Point point = cornerPoints[1];
                deleteDrawable.setBounds(
                        (int) (point.getX() - pointRadius),
                        (int) (point.getY() - pointRadius),
                        (int) (point.getX() + pointRadius),
                        (int) (point.getY() + pointRadius));
                deleteDrawable.draw(canvas);
            }

            // 3. 右下角
            if (cornerPoints[2] != null) {
                Point point = cornerPoints[2];
                cornerPath.reset();
                cornerPath.moveTo(point.getX() - cornerLength, point.getY());
                cornerPath.lineTo(point.getX() + cornerLineWidth / 2, point.getY());
                cornerPath.moveTo(point.getX(), point.getY() + cornerLineWidth / 2);
                cornerPath.lineTo(point.getX(), point.getY() - cornerLength);
                canvas.drawPath(cornerPath, cornerPaint);
            }

            // 4. 左下角
            if (cornerPoints[3] != null) {
                Point point = cornerPoints[3];
                cornerPath.reset();
                cornerPath.moveTo(point.getX() + cornerLength, point.getY());
                cornerPath.lineTo(point.getX() - cornerLineWidth / 2, point.getY());
                cornerPath.moveTo(point.getX(), point.getY() + cornerLineWidth / 2);
                cornerPath.lineTo(point.getX(), point.getY() - cornerLength);
                canvas.drawPath(cornerPath, cornerPaint);
            }
        }

        /**
         * @return arr[0]-左上角   arr[1]-右上角   arr[2]-左下角   arr[3]-右下角
         */
        private Point[] getCornerPoints() {
            if (pointList.size() != 4) {
                return null;
            }
            float minX = Float.MAX_VALUE;
            float maxX = Float.MIN_VALUE;
            float minY = Float.MAX_VALUE;
            float maxY = Float.MIN_VALUE;

            for (Point point : pointList) {
                if (point.getX() >= maxX) {
                    maxX = point.getX();
                }

                if (point.getX() <= minX) {
                    minX = point.getX();
                }

                if (point.getY() >= maxY) {
                    maxY = point.getY();
                }

                if (point.getY() <= minY) {
                    minY = point.getY();
                }
            }

            Point[] res = new Point[4];

            for (Point point : pointList) {
                if (point.getX() == minX && point.getY() == minY) {
                    res[0] = point;
                } else if (point.getX() == maxX && point.getY() == minY) {
                    res[1] = point;
                } else if (point.getX() == maxX && point.getY() == maxY) {
                    res[2] = point;
                } else if (point.getX() == minX && point.getY() == maxY) {
                    res[3] = point;
                }
            }
            return res;
        }


    }

    static class Point {
        private float x;
        private float y;
        private int index; // 显示的index

        public Point() {
        }

        public Point(float x, float y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    static class PointWrapper extends Point {
        Point xPoint;
        Point yPoint;
        int index;

        public PointWrapper(Point xPoint, Point yPoint, int index) {
            this.xPoint = xPoint;
            this.yPoint = yPoint;
            this.index = index;
        }

        @Override
        public float getX() {
            return xPoint.getX();
        }

        @Override
        public void setX(float x) {
            xPoint.setX(x);
        }

        @Override
        public float getY() {
            return yPoint.getY();
        }

        @Override
        public void setY(float y) {
            yPoint.setY(y);
        }
    }
}
