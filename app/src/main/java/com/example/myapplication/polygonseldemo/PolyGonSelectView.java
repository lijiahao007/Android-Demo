package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        // add for test
//        Polygon polygon = new Polygon();
//        polygon.addPoint(100, 100);
//        polygon.addPoint(100, 400);
//        polygon.addPoint(300, 500);
//        polygon.addPoint(400, 350);
//        polygon.addPoint(400, 200);
//        polygon.addPoint(200, 50);
//        polygon.setRegionSelect(true);
//
//        Polygon polygon1 = new Polygon();
//        polygon1.setRegionSelect(false);
//
//
//        Rectangle rectangle = new Rectangle();
//        rectangle.setRegionSelect(false);
//        rectangle.addPoint(400, 400);
//        rectangle.addPoint(500, 600);
//
//        Rectangle rectangle1 = new Rectangle();
//        rectangle1.setRegionSelect(false);
//
//
//        regionList.add(polygon);
//        regionList.add(polygon1);
//        regionList.add(rectangle);
//        regionList.add(rectangle1);
    }


    private OnButtonEnabledListener onButtonEnabledListener;


    public SelectShape getSelectRegion() {
        for (SelectShape region : regionList) {
            if (region.isRegionSelect) {
                return region;
            }
        }
        return null;
    }


    /**
     * 调用后，可以通过点击View添加点
     */
    public void addPolygonRegionByUser() {
        Polygon polygonRegion = new Polygon();
        regionList.add(polygonRegion);
        selectRegion(polygonRegion);
        invalidate();
    }

    /**
     * 调用后，可以通过该View中，通过滑动添加矩形框。
     */
    public void addRectangleRegionByUser() {
        Rectangle rectangleRegion = new Rectangle();
        regionList.add(rectangleRegion);
        selectRegion(rectangleRegion);
        invalidate();
    }


    public void addPolyRegion(List<Point> points) {
        Polygon polygon = new Polygon();
        for (Point point : points) {
            polygon.addPoint(point.getX(), point.getY());
        }
        selectRegion(polygon);
        invalidate();
    }

    public void addRectangleRegion(float leftTopX, float leftTopY, float rightBottomX, float rightBottomY) {
        Rectangle rectangleRegion = new Rectangle();
        rectangleRegion.addPoint(leftTopX, leftTopY);
        rectangleRegion.addPoint(rightBottomX, rightBottomY);
        regionList.add(rectangleRegion);
        selectRegion(rectangleRegion);
        invalidate();
    }

    public void deletePolygonRegionPoint() {
        SelectShape selectRegion = getSelectRegion();
        if (!(selectRegion instanceof Polygon)) {
            return;
        }

        Point editPoint = selectRegion.getEditPoint();
        if (editPoint == null) {
            return;
        }

        boolean remove = selectRegion.removePoint(editPoint);
        if (remove) {
            invalidate();
        }
    }

    public void deleteRegion(int index) {
        if (index < 0 || index >= regionList.size()) {
            throw new IndexOutOfBoundsException("index=" + index + "  regionList.size=" + regionList.size() + " regionList=" + regionList);
        }

        regionList.remove(index);
        invalidate();
    }

    public void deleteCurRegion() {
        SelectShape selectRegion = getSelectRegion();
        if (selectRegion == null) {
            return;
        }
        if (regionList.remove(selectRegion)) {
            invalidate();
        }
    }

    public void selectNextRegion() {
        for (int i = 0; i < regionList.size(); i++) {
            SelectShape region = regionList.get(i);
            if (region.isRegionSelect()) {
                region.setRegionSelect(false);
                SelectShape nextRegion = regionList.get(i == regionList.size() - 1 ? 0 : i + 1);
                nextRegion.setRegionSelect(true);
                invalidate();
                break;
            }
        }

    }


    private void selectRegion(SelectShape region) {
        for (int i = 0; i < regionList.size(); i++) {
            SelectShape shape = regionList.get(i);
            shape.setRegionSelect(shape == region);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        SelectShape selectRegion = getSelectRegion();
        if (selectRegion == null) {
            return false;
        }

        return selectRegion.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (SelectShape shape : regionList) {
            shape.drawShape(canvas);
        }
    }

    public void setOnButtonEnabledListener(OnButtonEnabledListener onButtonEnabledListener) {
        this.onButtonEnabledListener = onButtonEnabledListener;
    }


    abstract class SelectShape {
        protected ArrayList<Point> pointList = new ArrayList<>();
        protected boolean isRegionSelect = false;
        int index = -1;

        boolean isDragging = false;
        float lastX = -1;
        float lastY = -1;
        float beforeDraggingX = -1;
        float beforeDraggingY = -1;
        protected int pointRadius = (int) DimenUtil.dp2px(getContext(), 10);
        protected int gravityField = (int) DimenUtil.dp2px(getContext(), 10); // 重力场大小


        abstract Point addPoint(float x, float y);

        abstract boolean removePoint(Point point);

        abstract void movePointTo(int index, float dstX, float dstY); // 移动到

        abstract void movePoint(int index, float distanceX, float distanceY); // x移动distanceX, y移动distanceY

        abstract void drawShape(Canvas canvas); // 画出图形

        abstract boolean onTouchEvent(MotionEvent event); // 点击事件

        public List<Point> getPointList() {
            return pointList;
        }

        public boolean isRegionSelect() {
            return isRegionSelect;
        }

        public void setRegionSelect(boolean regionSelect) {
            isRegionSelect = regionSelect;
        }

        public Point getEditPoint() {
            for (Point point : pointList) {
                if (point.isEdit()) {
                    return point;
                }
            }
            return null;
        }

        protected int getNearPointIndex(float x, float y) {
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

        protected Point getNearPoint(float x, float y) {
            ListIterator<Point> iterator = pointList.listIterator(pointList.size());
            while (iterator.hasPrevious()) {
                Point point = iterator.previous();
                if (calPointDis(x, y, point.getX(), point.getY()) <= pointRadius + gravityField) {
                    return point;
                }
            }
            return null;
        }

        protected float calPointDis(float x1, float y1, float x2, float y2) {
            return (float) (Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));
        }

        protected boolean isOverLayPoint(int index, float newX, float newY, float oldX, float oldY) {
            Point point = pointList.get(index);
            for (Point p : pointList) {
                if (point == p) {
                    continue;
                }
                float newDis = calPointDis(p.getX(), p.getY(), newX, newY);
                float oldDis = calPointDis(p.getX(), p.getY(), oldX, oldY);
                if (newDis <= pointRadius * 2 && newDis <= oldDis) {
                    return true;
                }
            }
            return false;
        }


        protected boolean canMoveHorizon(float newX, float oldX) {
            int width = getWidth();
            if (newX > pointRadius && newX < (width - pointRadius)) {
                return true;
            }
            Log.i(TAG, "canMoveHorizon newX=" + newX + " oldX=" + oldX + " radius=" + pointRadius + " width=" + width);
            if ((newX <= pointRadius && newX >= oldX)
                    || (newX >= width - pointRadius && newX <= oldX)) {
                return true;
            }
            return false;
        }

        protected boolean canMoveVertical(float newY, float oldY) {
            int height = getHeight();
            if (newY > pointRadius && newY < (height - pointRadius)) {
                return true;
            }

            if ((newY <= pointRadius && newY >= oldY)
                    || (newY >= height - pointRadius && newY <= oldY)) {
                return true;
            }
            return false;
        }


        protected boolean isIntersection(int position) {
            int lineAmount = pointList.size();
            float[][] lineArr = new float[lineAmount][4];
            for (int i = 0; i < lineAmount; i++) {
                lineArr[i][0] = pointList.get(i).getX();
                lineArr[i][1] = pointList.get(i).getY();
                int tempPosition = i + 1;
                if (tempPosition == lineAmount) {
                    tempPosition = 0;
                }
                lineArr[i][2] = pointList.get(tempPosition).getX();
                lineArr[i][3] = pointList.get(tempPosition).getY();
            }

            int tempPosition = position - 1;
            if (tempPosition < 0) {
                tempPosition = lineAmount - 1;
            }
            float[] line1 = lineArr[tempPosition];

            for (int i = 0; i < lineAmount; i++) {
                if (position == 1) {
                    if (position != i && position - 1 != i && lineAmount - 1 != i) {
                        if (isIntersection(line1[0], line1[1], line1[2], line1[3],
                                lineArr[i][0], lineArr[i][1], lineArr[i][2], lineArr[i][3])) {
                            return true;
                        }
                    }
                } else if (position == 0) {
                    if (position != i && lineAmount - 1 != i && lineAmount - 2 != i) {
                        if (isIntersection(line1[0], line1[1], line1[2], line1[3],
                                lineArr[i][0], lineArr[i][1], lineArr[i][2], lineArr[i][3])) {
                            return true;
                        }
                    }
                } else {
                    if (position != i && position - 1 != i && position - 2 != i) {
                        if (isIntersection(line1[0], line1[1], line1[2], line1[3],
                                lineArr[i][0], lineArr[i][1], lineArr[i][2], lineArr[i][3])) {
                            return true;
                        }
                    }
                }

            }

            float[] line2 = lineArr[position];

            for (int i = 0; i < lineAmount; i++) {
                if (position == 0) {
                    if (position != i && lineAmount - 1 != i && position + 1 != i) {
                        if (isIntersection(line2[0], line2[1], line2[2], line2[3],
                                lineArr[i][0], lineArr[i][1], lineArr[i][2], lineArr[i][3])) {
                            return true;
                        }
                    }
                } else if (position == lineAmount - 1) {
                    if (position != i && position - 1 != i && 0 != i) {
                        if (isIntersection(line2[0], line2[1], line2[2], line2[3],
                                lineArr[i][0], lineArr[i][1], lineArr[i][2], lineArr[i][3])) {
                            return true;
                        }
                    }
                } else {
                    if (position != i && position - 1 != i && position + 1 != i) {
                        if (isIntersection(line2[0], line2[1], line2[2], line2[3],
                                lineArr[i][0], lineArr[i][1], lineArr[i][2], lineArr[i][3])) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        //参考ios端算法
        protected boolean isIntersection(float x1, float y1, float x2, float y2,
                                         float x3, float y3, float x4, float y4) {
            //快速排斥实验
            if ((Math.max(x1, x2)) < (Math.min(x3, x4)) ||
                    (Math.max(y1, y2)) < (Math.min(y3, y4)) ||
                    (Math.max(x3, x4)) < (Math.min(x1, x2)) ||
                    (Math.max(y3, y4)) < (Math.min(y1, y2))) {
                return false;
            }
            //跨立实验
            if ((((x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3)) *
                    ((x2 - x3) * (y4 - y3) - (y2 - y3) * (x4 - x3))) > 0 ||
                    (((x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1)) *
                            ((x4 - x1) * (y2 - y1) - (y4 - y1) * (x2 - x1))) > 0) {
                return false;
            }
            return true;
        }

        @NonNull
        @Override
        public String toString() {
            return "SelectShape{" +
                    "isRegionSelect=" + isRegionSelect +
                    ", pointList=" + pointList +
                    '}';
        }
    }

    class Polygon extends SelectShape {
        private final Paint textBackgroundPaint;
        private final Paint linePaint;
        private final Paint textPaint;
        private final Paint selectRegionPaint;
        private final Rect textRect;
        int draggingPointIndex = -1;
        protected Path regionPath = new Path();


        public Polygon() {
            int lineColor = getResources().getColor(R.color.Red);
            int textColor = getResources().getColor(R.color.white);
            int textBg = getResources().getColor(R.color.Red);
            int selectRegionColor = getResources().getColor(R.color.TrunsRed);
            float lineWidth = DimenUtil.dp2px(getContext(), 2);

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
        }

        @Override
        public Point addPoint(float x, float y) {
            Point point = new Point(x, y);
            pointList.add(point);
            sortPoint(pointList);
            return point;
        }

        @Override
        public boolean removePoint(Point point) {
            boolean remove = pointList.remove(point);
            if (remove) {
                sortPoint(pointList);
            }
            return remove;
        }

        @Override
        public void movePointTo(int index, float dstX, float dstY) {
            Point point = pointList.get(index);

            if (isOverLayPoint(index, dstX, dstY, point.getX(), point.getY())) {
                return;
            }

            if (canMoveHorizon(dstX, point.getX())) {
                point.setX(dstX);
            }

            if (canMoveVertical(dstY, point.getY())) {
                point.setY(dstY);
            }
        }

        @Override
        public void movePoint(int index, float distanceX, float distanceY) {

            Point point = pointList.get(index);

            float newX = point.getX() + distanceX;
            float newY = point.getY() + distanceY;
            if (isOverLayPoint(index, newX, newY, point.getX(), point.getY())) {
                return;
            }


            if (canMoveHorizon(newX, point.getX())) {
                point.setX(newX);
            }

            if (canMoveVertical(newY, point.getY())) {
                point.setY(newY);
            }
        }

        @Override
        public void drawShape(Canvas canvas) {
            // TODO: 绘制未选中状态下的UI
            // 1. 画Path
            regionPath.reset();
            if (pointList.size() > 1) {
                for (int i = 0; i < pointList.size(); i++) {
                    Point point = pointList.get(i);
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

            // 2.  画点
            if (isRegionSelect) {
                for (int i = 0; i < pointList.size(); i++) {
                    Point point = pointList.get(i);
                    drawPoint(canvas, point.getX(), point.getY(), i + 1);
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

            // TODO: 增加编辑状态点的绘画。
        }

        @Override
        boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int pointIndex = getNearPointIndex(x, y);
                    Log.i(TAG, "ACTION_DOWN " + "pointIndex = " + pointIndex);
                    if (pointIndex != -1) {
                        selectPoint(pointList.get(pointIndex));
                        // 开始拖拽
                        isDragging = true;
                        draggingPointIndex = pointIndex;
                        lastX = x;
                        lastY = y;
                        beforeDraggingX = x;
                        beforeDraggingY = y;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "ACTION_MOVE 【" + draggingPointIndex + "】");
                    if (isDragging && draggingPointIndex != -1) {
                        // 拖拽中
                        movePoint(draggingPointIndex, x - lastX, y - lastY);
                        lastX = x;
                        lastY = y;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP");
                    if (draggingPointIndex == -1) {
                        // 增加点
                        Point point = addPoint(x, y);
                        selectPoint(point);
                        invalidate();
                    } else {
                        // 拖拽结束
                        isDragging = false;
                        if (isIntersection(draggingPointIndex)) {
                            Point point = pointList.get(draggingPointIndex);
                            point.setX(beforeDraggingX);
                            point.setY(beforeDraggingY);
                            invalidate();
                        }
                        draggingPointIndex = -1;
                        beforeDraggingX = -1;
                        beforeDraggingY = -1;
                    }
                    break;
            }
            return true;
        }

        private double getAngle(float contX, float contY, float nowX, float nowY) {
            double tem = Math.atan((nowY - contY) / (nowX - contX));//反切!
            tem = (nowX - contX) < 0 ? tem + 180 : (nowY - contY) > 0 ? tem : tem + 360;//象限补偿!
            return tem;
        }

        private void sortPoint(ArrayList<Point> data) {
            //先找到中心点坐标
            float centreX = 0;
            float centreY = 0;
            int size = data.size();
            for (int i = 0; i < size; i++) {
                centreX += data.get(i).getX();
                centreY += data.get(i).getY();
            }

            centreX /= size;
            centreY /= size;

            //然后算每个点偏移的角度
            for (int i = 0; i < size; i++) {
                double angle = getAngle(centreX, centreY, data.get(i).getX(), data.get(i).getY());
                data.get(i).setAngle(angle);
            }
            ArrayList<Point> tempData = new ArrayList<>();
            while (data.size() > 0) {
                int minAngleIndex = 0;
                double minAngle = data.get(minAngleIndex).getAngle();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getAngle() < minAngle) {
                        minAngle = data.get(i).getAngle();
                        minAngleIndex = i;
                    }
                }
                tempData.add(data.get(minAngleIndex));
                data.remove(minAngleIndex);
            }

            //以x坐标最小的点作为第一个点
            if (tempData.size() > 0) {
                int minXIndex = 0;
                float minX = tempData.get(minXIndex).getX();
                for (int i = 1; i < tempData.size(); i++) {
                    if (tempData.get(i).getX() < minX) {
                        minXIndex = i;
                        minX = tempData.get(minXIndex).getX();
                    }
                }
                for (int i = minXIndex; i < tempData.size(); i++) {
                    data.add(tempData.get(i));
                }
                for (int i = 0; i < minXIndex; i++) {
                    data.add(tempData.get(i));
                }
            }
        }

        private void selectPoint(Point point) {
            for (Point p : pointList) {
                p.setEdit(p == point);
            }
        }
    }

    class Rectangle extends SelectShape {
        private final Paint linePaint;
        private final Paint textPaint;
        private final Paint selectRegionPaint;
        private final Paint cornerPaint;
        private final float cornerLength;
        private final Path cornerPath;
        private float cornerLineWidth;
        private final Drawable deleteDrawable;
        private final int deleteDrawableSize = (int) DimenUtil.dp2px(getContext(), 10);
        private boolean isAddPoint = false;
        Point draggingPoint = null;
        boolean isDragWholeRegion = false;


        public Rectangle() {
            int lineColor = getResources().getColor(R.color.Red);
            int textColor = getResources().getColor(R.color.white);
            int selectRegionColor = getResources().getColor(R.color.TrunsRed);
            float lineWidth = DimenUtil.dp2px(getContext(), 2);
            cornerLineWidth = DimenUtil.dp2px(getContext(), 5);
            pointRadius = (int) DimenUtil.dp2px(getContext(), 3);

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
        }

        // 需要分别加入左上角 & 右下角的点
        @Override
        Point addPoint(float x, float y) {
            Point point = new Point(x, y);
            if (pointList.size() == 0) {
                pointList.add(point);
            } else if (pointList.size() == 1) {
                Point leftTop = pointList.get(0);
                // 右上角
                pointList.add(new PointWrapper(point, leftTop));
                pointList.add(point);
                pointList.add(new PointWrapper(leftTop, point));
                return point;
            }
            return null;
        }


        @Override
        boolean removePoint(Point point) {
            return false;
        }

        @Override
        void movePointTo(int index, float dstX, float dstY) {
            if (index < 0 || index >= pointList.size()) {
                throw new IndexOutOfBoundsException("index=" + index + " 越界");
            }

            Point point = pointList.get(index);
            point.setX(dstX);
            point.setY(dstY);
            sortPoint(pointList);
        }

        @Override
        void movePoint(int index, float distanceX, float distanceY) {
            if (index < 0 || index >= pointList.size()) {
                throw new IndexOutOfBoundsException("index=" + index + " 越界");
            }

            movePoint(pointList.get(index), distanceX, distanceY);
        }

        void movePoint(Point point, float distanceX, float distanceY) {
            if (!pointList.contains(point)) {
                throw new IllegalArgumentException("pointList=" + pointList + " 不存在" + point);
            }

            float newX = point.getX() + distanceX;
            float newY = point.getY() + distanceY;

            boolean isChange = false;
            if (canMoveHorizon(newX, point.getX())) {
                point.setX(newX);
                isChange = true;
            }

            if (canMoveVertical(newY, point.getY())) {
                point.setY(newY);
                isChange = true;
            }

            if (isChange) {
                sortPoint(pointList);
            }
        }

        @Override
        void drawShape(Canvas canvas) {
            if (pointList.size() != 4) {
                return;
            }

            canvas.drawRect(
                    pointList.get(0).getX(),
                    pointList.get(0).getY(),
                    pointList.get(2).getX(),
                    pointList.get(2).getY(),
                    linePaint
            );

            canvas.drawRect(
                    pointList.get(0).getX(),
                    pointList.get(0).getY(),
                    pointList.get(2).getX(),
                    pointList.get(2).getY(),
                    selectRegionPaint
            );


            if (!isRegionSelect) {
                return;
            }
            // 1. 左上角
            if (pointList.get(0) != null) {
                Point point = pointList.get(0);
                cornerPath.reset();
                cornerPath.moveTo(point.getX() + cornerLength, point.getY());
                cornerPath.lineTo(point.getX() - cornerLineWidth / 2, point.getY());
                cornerPath.moveTo(point.getX(), point.getY() - cornerLineWidth / 2);
                cornerPath.lineTo(point.getX(), point.getY() + cornerLength);
                canvas.drawPath(cornerPath, cornerPaint);
            }

            // 2. 右上角
            if (pointList.get(1) != null) {
                Point point = pointList.get(1);
                deleteDrawable.setBounds(
                        (int) (point.getX() - deleteDrawableSize),
                        (int) (point.getY() - deleteDrawableSize),
                        (int) (point.getX() + deleteDrawableSize),
                        (int) (point.getY() + deleteDrawableSize));
                deleteDrawable.draw(canvas);
            }

            // 3. 右下角
            if (pointList.get(2) != null) {
                Point point = pointList.get(2);
                cornerPath.reset();
                cornerPath.moveTo(point.getX() - cornerLength, point.getY());
                cornerPath.lineTo(point.getX() + cornerLineWidth / 2, point.getY());
                cornerPath.moveTo(point.getX(), point.getY() + cornerLineWidth / 2);
                cornerPath.lineTo(point.getX(), point.getY() - cornerLength);
                canvas.drawPath(cornerPath, cornerPaint);
            }

            // 4. 左下角
            if (pointList.get(3) != null) {
                Point point = pointList.get(3);
                cornerPath.reset();
                cornerPath.moveTo(point.getX() + cornerLength, point.getY());
                cornerPath.lineTo(point.getX() - cornerLineWidth / 2, point.getY());
                cornerPath.moveTo(point.getX(), point.getY() + cornerLineWidth / 2);
                cornerPath.lineTo(point.getX(), point.getY() - cornerLength);
                canvas.drawPath(cornerPath, cornerPaint);
            }
        }

        /**
         * @param data data[0]-左上角   data[1]-右上角   data[2]-左下角   data[3]-右下角
         */
        private void sortPoint(ArrayList<Point> data) {
            if (data.size() != 4) {
                return;
            }

            // 对角点
            Point leftTop = data.get(0);
            Point rightBottom = data.get(2);

            if (leftTop.getX() < rightBottom.getX() && leftTop.getY() < rightBottom.getY()) {
                return;
            }


            if (leftTop.getX() < rightBottom.getX() && leftTop.getY() > rightBottom.getY()) {
                Collections.reverse(data);
                return;
            }

            ArrayList<Point> tmp = new ArrayList<>();
            if (leftTop.getX() > rightBottom.getX() && leftTop.getY() < rightBottom.getY()) {
                tmp.add(data.get(1));
                tmp.add(data.get(0));
                tmp.add(data.get(3));
                tmp.add(data.get(2));
                data.clear();
                data.addAll(tmp);
                return;
            }

            if (leftTop.getX() > rightBottom.getX() && leftTop.getY() > rightBottom.getY()) {
                tmp.add(data.get(2));
                tmp.add(data.get(3));
                tmp.add(data.get(0));
                tmp.add(data.get(1));
                data.clear();
                data.addAll(tmp);
                return;
            }
        }

        // 判断某个点是否在该形状中
        protected boolean isPointInShape(float x, float y) {
            if (pointList.size() != 4) {
                return false;
            }

            return x >= pointList.get(0).getX() && x <= pointList.get(2).getX()
                    && y >= pointList.get(0).getY() && y <= pointList.get(2).getY();
        }

        public void moveAllPoint(float distanceX, float distanceY) {
            // 只用整对角
            if (pointList.size() != 4) {
                return;
            }

            Point leftTop = pointList.get(0);
            Point rightBottom = pointList.get(2);

            float newLeftTopX = leftTop.getX() + distanceX;
            float newLeftTopY = leftTop.getY() + distanceY;
            float newRightBottomX = rightBottom.getX() + distanceX;
            float newRightBottomY = rightBottom.getY() + distanceY;

            if (canMoveHorizon(newLeftTopX, leftTop.getX()) && canMoveHorizon(newRightBottomX, rightBottom.getX())) {
                Log.i(TAG, "canMoveHorizon(newLeftTopX, leftTop.getX())=" + canMoveHorizon(newLeftTopX, leftTop.getX()));
                Log.i(TAG, "canMoveHorizon(newRightBottomX, rightBottom.getX())" + canMoveHorizon(newRightBottomX, rightBottom.getX()));

                leftTop.setX(newLeftTopX);
                rightBottom.setX(newRightBottomX);
            }

            if (canMoveVertical(newLeftTopY, leftTop.getY()) && canMoveVertical(newRightBottomY, rightBottom.getY())) {
                leftTop.setY(newLeftTopY);
                rightBottom.setY(newRightBottomY);
            }
        }


        @Override
        boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (pointList.size() == 1) {
                        pointList.clear();
                    }
                    Point nearPoint = getNearPoint(x, y);

                    Log.i(TAG, "ACTION_DOWN " + "pointIndex = " + nearPoint + " size=" + pointList.size());
                    if (nearPoint != null) {
                        // 开始拖拽
                        isDragging = true;
                        draggingPoint = nearPoint;
                        lastX = x;
                        lastY = y;
                        beforeDraggingX = nearPoint.getX();
                        beforeDraggingY = nearPoint.getY();
                    } else if (pointList.size() == 0) {
                        // 添加点
                        addPoint(x, y);
                        isAddPoint = true;
                    } else if (isPointInShape(x, y)) {
                        // 整体拖动
                        isDragWholeRegion = true;
                        lastX = x;
                        lastY = y;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i(TAG, "ACTION_MOVE 【" + draggingPoint + "】  isAddPoint=" + isAddPoint + " isDragging=" + isDragging + " isDragWholeRegion=" + isDragWholeRegion);
                    if (isDragging && draggingPoint != null) {
                        // 拖拽中
                        movePoint(draggingPoint, x - lastX, y - lastY);
                        lastX = x;
                        lastY = y;
                        invalidate();
                    } else if (isAddPoint && pointList.size() == 1) {
                        Point firstPoint = pointList.get(0);
                        if (calPointDis(x, y, firstPoint.getX(), firstPoint.getY()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                            // 设置第二个点，并开始拖拽
                            Point point = addPoint(x, y);
                            if (point == null) {
                                break;
                            }
                            isAddPoint = false;
                            isDragging = true;
                            draggingPoint = point;
                            lastX = x;
                            lastY = y;
                            beforeDraggingX = x;
                            beforeDraggingY = y;
                            invalidate();
                        }
                    } else if (isDragWholeRegion) {
                        // 整体拖动
                        moveAllPoint(x - lastX, y - lastY);
                        lastX = x;
                        lastY = y;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP" + "  size= " + pointList.size());
                    isDragging = false;
                    isAddPoint = false;
                    draggingPoint = null;
                    isDragWholeRegion = false;
                    break;
            }
            return true;
        }
    }

    public static class Point {
        private float x;
        private float y;
        private double angle;
        private boolean isEdit = false;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
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


        private double getAngle() {
            return angle;
        }

        private void setAngle(double angle) {
            this.angle = angle;
        }

        private boolean isEdit() {
            return isEdit;
        }

        private void setEdit(boolean edit) {
            isEdit = edit;
        }

        @NonNull
        @Override
        public String toString() {
            return "Point{" +
                    "x=" + getX() +
                    ", y=" + getY() +
                    ", angle=" + angle +
                    ", isSelect=" + isEdit +
                    '}';
        }
    }

    static class PointWrapper extends Point {
        Point xPoint;
        Point yPoint;

        public PointWrapper(Point xPoint, Point yPoint) {
            this.xPoint = xPoint;
            this.yPoint = yPoint;
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


    public interface OnButtonEnabledListener {
        void onButtonEnabledChange(boolean confirmEnable, boolean resetEnable,
                                   boolean deleteEnable, boolean clearEnable);

        void onIsIntersection();
    }


}
