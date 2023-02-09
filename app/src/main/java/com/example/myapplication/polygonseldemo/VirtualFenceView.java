package com.example.myapplication.polygonseldemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class VirtualFenceView extends View {

    public VirtualFenceView(Context context) {
        super(context);
        init(context);
    }

    public VirtualFenceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VirtualFenceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("NewApi")
    public VirtualFenceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private final int MIN_AMOUNT = 2;
    private final int MAX_AMOUNT = 6;
    private ArrayList<VirtualFenceDotInfo> virtualFenceInfoList = new ArrayList<>();
    private ArrayList<VirtualFenceDotInfo> virtualFenceInfoListCopy = new ArrayList<>();
    private ArrayList<VirtualFenceDotInfo> layoutButtonList = new ArrayList<>();
    private Paint paintEdit = null;
    private int colorBlue;
    private int colorBlue2;
    private int colorRed;
    private int colorWhite;
    private int strokeWidth;
    private float textSize;
    private int padding;
    private int dotRadius;
    private int viewWidth;
    private int viewHeight;
    private boolean isCanClick = false;
    private boolean isModify = false;
    private final int CLICK_TYPE_DEFAULT = 0;
    private final int CLICK_TYPE_START_DOT = 1;

    private VirtualFenceDotInfo clickInfo = null;
    private int clickType = CLICK_TYPE_DEFAULT;

    private float lastMoveXOffset;
    private float lastMoveYOffset;

    private float lastPointX;
    private float lastPointY;
    private int clickPointPosition;

    private OnButtonEnabledListener onButtonEnabledListener;

    public void setIsCanClick(boolean isCanClick){
        this.isCanClick = isCanClick;
        invalidate();
    }
    public void initData(ArrayList<VirtualFenceInfo.PointInfo> dataList, boolean isCanClick) {
        isModify = false;
        this.isCanClick = isCanClick;
        virtualFenceInfoList.clear();
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        for (int i = 0; i < dataList.size(); i++) {
            VirtualFenceDotInfo virtualFenceInfo = new VirtualFenceDotInfo();
            virtualFenceInfo.setEdit(false);
            virtualFenceInfo.setOffsetX(percentageToNumerical(
                    dataList.get(i).getPointX(), true));
            virtualFenceInfo.setOffsetY(percentageToNumerical(
                    dataList.get(i).getPointY(), false));
            virtualFenceInfoList.add(virtualFenceInfo);
        }
        virtualFenceInfoListCopy.clear();
        for (int i = 0; i < dataList.size(); i++) {
            VirtualFenceDotInfo virtualFenceInfo = new VirtualFenceDotInfo();
            virtualFenceInfo.setEdit(false);
            virtualFenceInfo.setOffsetX(percentageToNumerical(
                    dataList.get(i).getPointX(), true));
            virtualFenceInfo.setOffsetY(percentageToNumerical(
                    dataList.get(i).getPointY(), false));
            virtualFenceInfoListCopy.add(virtualFenceInfo);
        }
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(false, false,
                    false, virtualFenceInfoList.size() != 0);
        }
        invalidate();
    }

    public ArrayList<VirtualFenceInfo.PointInfo> getData() {
        ArrayList<VirtualFenceInfo.PointInfo> dataList = new ArrayList<>();
        for (int i = 0; i < virtualFenceInfoList.size(); i++) {
            VirtualFenceInfo.PointInfo pointInfo = new VirtualFenceInfo.PointInfo();
            pointInfo.setPointId(i);
            pointInfo.setPointX(numericalToPercentage(
                    virtualFenceInfoList.get(i).getOffsetX(), true));
            pointInfo.setPointY(numericalToPercentage(
                    virtualFenceInfoList.get(i).getOffsetY(), false));
            dataList.add(pointInfo);
        }
//        virtualFenceInfoList.clear();
        invalidate();
        return dataList;
    }

    public boolean isGreaterThanMinimum() {
        return virtualFenceInfoList.size() > MIN_AMOUNT;
    }

    public void addDot(boolean isInit) {
        isModify = true;
        float x = (float) (Math.random() * viewWidth);
        float y = (float) (Math.random() * viewHeight);
        addDot(x, y, dotRadius, isInit);
    }

    public void addDot(float x, float y, int radius, boolean isInit) {
        isModify = true;
        if (virtualFenceInfoList.size() == MAX_AMOUNT) {
            return;
        }
        if (!isInit && !dotIsValid(x, y, true)) {
            return;
        }
        for (int i = 0; i < virtualFenceInfoList.size(); i++) {
            virtualFenceInfoList.get(i).setEdit(false);
            virtualFenceInfoList.get(i).setRadius(dotRadius);
        }
        VirtualFenceDotInfo virtualFenceInfo = new VirtualFenceDotInfo();
        virtualFenceInfo.setOffsetX(x);
        virtualFenceInfo.setOffsetY(y);
        virtualFenceInfo.setRadius(radius);
        if (!isInit) {
            virtualFenceInfo.setEdit(true);
        }
        virtualFenceInfoList.add(virtualFenceInfo);
        sortDow(virtualFenceInfoList);
        if (!isInit && onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true, true,
                    true, virtualFenceInfoList.size() != 0);
        }
        invalidate();
    }

    public void resetDot() {
        isModify = false;
        virtualFenceInfoList.clear();
        for (int i = 0; i < virtualFenceInfoListCopy.size(); i++) {
            VirtualFenceDotInfo data = new VirtualFenceDotInfo();
            data.setOffsetX(virtualFenceInfoListCopy.get(i).getOffsetX());
            data.setOffsetY(virtualFenceInfoListCopy.get(i).getOffsetY());
            data.setRadius(virtualFenceInfoListCopy.get(i).getRadius());
            data.setEdit(virtualFenceInfoListCopy.get(i).isEdit());
            data.setAngle(virtualFenceInfoListCopy.get(i).getAngle());
            virtualFenceInfoList.add(data);
        }
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(false, false,
                    false, virtualFenceInfoList.size() != 0);
        }
        invalidate();
    }

    public void deleteDot() {
        isModify = true;
        for (int i = 0; i < virtualFenceInfoList.size(); i++) {
            if (virtualFenceInfoList.get(i).isEdit()) {
                virtualFenceInfoList.remove(i);
            }
        }
        sortDow(virtualFenceInfoList);
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true, true,
                    false, virtualFenceInfoList.size() != 0);
        }
        invalidate();
    }

    public void clearDot() {
        isModify = true;
        virtualFenceInfoList.clear();
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true, true,
                    false, virtualFenceInfoList.size() != 0);
        }
        invalidate();
    }

    public void setModify(boolean modify) {
        isModify = modify;
    }

    public boolean isModify() {
        return isModify;
    }

    public void initLayoutButtonList(ArrayList<View> viewList, int topOffset) {
        if (viewList == null) return;
        if (layoutButtonList == null) {
            layoutButtonList = new ArrayList<>();
        } else {
            layoutButtonList.clear();
        }

        for (int i = 0; i < viewList.size(); i++) {
            Point point = new Point();
            Rect rect = new Rect();
            View view = viewList.get(i);
            view.getGlobalVisibleRect(rect, point);
            int radius = view.getWidth() / 2;
            float x = radius + rect.left;
            float y = radius + rect.top - topOffset;
            VirtualFenceDotInfo virtualFenceInfo = new VirtualFenceDotInfo();
            virtualFenceInfo.setRadius(radius);
            virtualFenceInfo.setOffsetX(x);
            virtualFenceInfo.setOffsetY(y);
            layoutButtonList.add(virtualFenceInfo);
        }
    }

    public void setOnButtonEnabledListener(OnButtonEnabledListener buttonEnabledListener) {
        this.onButtonEnabledListener = buttonEnabledListener;
    }

    private void init(Context context) {
        textSize = DimenUtil.sp2px(context, 12);
        strokeWidth = (int) DimenUtil.dp2px(context, 2);
        dotRadius = (int) DimenUtil.dp2px(context, 10);
        padding = strokeWidth;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        colorBlue = context.getResources().getColor(R.color.Red);
        colorBlue2 = context.getResources().getColor(R.color.Red);
        colorWhite = context.getResources().getColor(R.color.color_invariant_ffffff);
        colorRed = context.getResources().getColor(R.color.Red);

        paintEdit = new Paint();
        paintEdit.setAntiAlias(true);
        paintEdit.setColor(colorBlue);
        paintEdit.setStrokeWidth(strokeWidth);
        paintEdit.setTextSize(textSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
//        padding = viewHeight > viewWidth ? viewHeight : viewWidth * 4 / 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanClick) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                CustomScrollView.setIsReturnFalse(false);
                if (clickType == CLICK_TYPE_DEFAULT) {
                    float x = event.getX();
                    float y = event.getY();
                    addDot(x, y, dotRadius, false);
                } else if (clickType == CLICK_TYPE_START_DOT) {
                    if (isIntersection(clickPointPosition)) {
                        if (onButtonEnabledListener != null) {
                            onButtonEnabledListener.onIsIntersection();
                        }
                        clickInfo.setOffsetX(lastPointX);
                        clickInfo.setOffsetY(lastPointY);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                clickType = CLICK_TYPE_DEFAULT;
                if (virtualFenceInfoList != null) {
                    for (int i = 0; i < virtualFenceInfoList.size(); i++) {
                        clickInfo = virtualFenceInfoList.get(i);
                        clickPointPosition = i;
                        if (isCoordinatePoint(event.getX(), event.getY(),
                                clickInfo.getOffsetX(), clickInfo.getOffsetY())) {
                            clickType = CLICK_TYPE_START_DOT;
                            lastPointX = clickInfo.getOffsetX();
                            lastPointY = clickInfo.getOffsetY();
                            setSelect();
                        }
                        CustomScrollView.setIsReturnFalse(clickType != CLICK_TYPE_DEFAULT);
                        if (clickType != CLICK_TYPE_DEFAULT) {
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (clickInfo != null) {
                    isModify = true;
                    float xOffset = event.getX() - lastMoveXOffset;
                    float yOffset = event.getY() - lastMoveYOffset;
                    float x = clickInfo.getOffsetX() + xOffset;
                    float y = clickInfo.getOffsetY() + yOffset;
                    switch (clickType) {
                        case CLICK_TYPE_START_DOT:
                            boolean isInvalidate = false;
                            if (dotIsValid(x, clickInfo.getOffsetY(), false)) {
                                clickInfo.setOffsetX(x);
                                isInvalidate = true;
                            }
                            if (dotIsValid(clickInfo.getOffsetX(), y, false)) {
                                clickInfo.setOffsetY(y);
                                isInvalidate = true;
                            }
                            if (isInvalidate) {
                                invalidate();
                            }
                            break;
                    }
                }
                break;
        }
        lastMoveXOffset = event.getX();
        lastMoveYOffset = event.getY();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < virtualFenceInfoList.size(); i++) {
            int temp = i + 1;
            if (temp >= virtualFenceInfoList.size()) {
                temp = 0;
            }
            paintEdit.setColor(colorBlue);
            canvas.drawLine(virtualFenceInfoList.get(i).getOffsetX(),
                    virtualFenceInfoList.get(i).getOffsetY(),
                    virtualFenceInfoList.get(temp).getOffsetX(),
                    virtualFenceInfoList.get(temp).getOffsetY(), paintEdit);
        }
        if (isCanClick) {
            for (int i = 0; i < virtualFenceInfoList.size(); i++) {
                drawDot(canvas, virtualFenceInfoList.get(i), String.valueOf(i + 1));
            }
        }
    }

    private boolean dotIsValid(float x, float y, boolean isAdd) {
        boolean isValid = false;
        //判断点与边界
        if (x > (dotRadius + padding) && x < (viewWidth - dotRadius - padding)
                && y > (dotRadius + padding) && y < (viewHeight - dotRadius - padding)) {
            isValid = true;
        }
        //判断点与点之间
        if (isValid) {
            isValid = isValid(x, y, virtualFenceInfoList, isAdd);
        }
        //判断点与按钮之间
        if (isValid) {
            isValid = isValid(x, y, layoutButtonList, isAdd);
        }
        return isValid;
    }

    private boolean isValid(float x, float y, ArrayList<VirtualFenceDotInfo> dataList, boolean isAdd) {
        boolean isValid = true;
        for (int i = 0; i < dataList.size(); i++) {
            int temp = dotRadius + dataList.get(i).getRadius();
            if (isAdd) {
                isValid = lineSpace(x, y, dataList.get(i).getOffsetX(),
                        dataList.get(i).getOffsetY()) > temp;
            } else if (!dataList.get(i).isEdit) {
                isValid = lineSpace(x, y, dataList.get(i).getOffsetX(),
                        dataList.get(i).getOffsetY()) > temp;
            }
            if (!isValid) {
                break;
            }
        }
        return isValid;
    }

    private void sortDow(ArrayList<VirtualFenceDotInfo> data) {
        //先找到中心点坐标
        float centreX = 0;
        float centreY = 0;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            centreX += data.get(i).getOffsetX();
            centreY += data.get(i).getOffsetY();
        }
        centreX /= size;
        centreY /= size;

        //然后算每个点偏移的角度
        for (int i = 0; i < size; i++) {
            double angle = getAngle(centreX, centreY, data.get(i).getOffsetX(), data.get(i).getOffsetY());
            data.get(i).setAngle(angle);
        }
        ArrayList<VirtualFenceDotInfo> tempData = new ArrayList<>();
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
        data.clear();

        //以x坐标最小的点作为第一个点
        if (tempData.size() > 0) {
            int minXIndex = 0;
            float minX = tempData.get(minXIndex).getOffsetX();
            for (int i = 1; i < tempData.size(); i++) {
                if (tempData.get(i).getOffsetX() < minX) {
                    minXIndex = i;
                    minX = tempData.get(minXIndex).getOffsetX();
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

    private double getAngle(float contX, float contY, float nowX, float nowY) {
        double tem = Math.atan((nowY - contY) / (nowX - contX));//反切!
        tem = (nowX - contX) < 0 ? tem + 180 : (nowY - contY) > 0 ? tem : tem + 360;//象限补偿!
        return tem;
    }

    private void setSelect() {
        if (!clickInfo.isEdit()) {
            for (int j = 0; j < virtualFenceInfoList.size(); j++) {
                virtualFenceInfoList.get(j).setEdit(false);
                virtualFenceInfoList.get(j).setRadius(dotRadius);
            }
            clickInfo.setEdit(true);
            clickInfo.setRadius(dotRadius);
            invalidate();
        }
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true, true,
                    true, virtualFenceInfoList.size() != 0);
        }
    }

    private void drawDot(Canvas canvas, VirtualFenceDotInfo virtualFenceInfo, String text) {
        paintEdit.setColor(virtualFenceInfo.isEdit ? colorRed : colorBlue);
        canvas.drawCircle(virtualFenceInfo.getOffsetX(),
                virtualFenceInfo.getOffsetY(), dotRadius, paintEdit);
        paintEdit.setColor(colorWhite);
        int textWidth = CustomViewUtil.getTextWidth(paintEdit, text);
        canvas.drawText(text, virtualFenceInfo.getOffsetX() - (textWidth / 2),
                textSize / 3 + virtualFenceInfo.getOffsetY(), paintEdit);

    }

    private boolean isIntersection(int position) {
        int lineAmount = virtualFenceInfoList.size();
        float[][] lineArr = new float[lineAmount][4];
        for (int i = 0; i < lineAmount; i++) {
            lineArr[i][0] = virtualFenceInfoList.get(i).getOffsetX();
            lineArr[i][1] = virtualFenceInfoList.get(i).getOffsetY();
            int tempPosition = i + 1;
            if (tempPosition == lineAmount) {
                tempPosition = 0;
            }
            lineArr[i][2] = virtualFenceInfoList.get(tempPosition).getOffsetX();
            lineArr[i][3] = virtualFenceInfoList.get(tempPosition).getOffsetY();
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
    private boolean isIntersection(float x1, float y1, float x2, float y2,
                                   float x3, float y3, float x4, float y4) {
        //快速排斥实验
        if ((x1 > x2 ? x1 : x2) < (x3 < x4 ? x3 : x4) ||
                (y1 > y2 ? y1 : y2) < (y3 < y4 ? y3 : y4) ||
                (x3 > x4 ? x3 : x4) < (x1 < x2 ? x1 : x2) ||
                (y3 > y4 ? y3 : y4) < (y1 < y2 ? y1 : y2)) {
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

//    public static boolean intersection(double l1x1, double l1y1, double l1x2, double l1y2,
//                                       double l2x1, double l2y1, double l2x2, double l2y2) {
//        // 快速排斥实验 首先判断两条线段在 x 以及 y 坐标的投影是否有重合。 有一个为真，则代表两线段必不可交。
//        if (Math.max(l1x1, l1x2) < Math.min(l2x1, l2x2)
//                || Math.max(l1y1, l1y2) < Math.min(l2y1, l2y2)
//                || Math.max(l2x1, l2x2) < Math.min(l1x1, l1x2)
//                || Math.max(l2y1, l2y2) < Math.min(l1y1, l1y2)) {
//            return false;
//        }
//        // 跨立实验  如果相交则矢量叉积异号或为零，大于零则不相交
//        if ((((l1x1 - l2x1) * (l2y2 - l2y1) - (l1y1 - l2y1) * (l2x2 - l2x1))
//                * ((l1x2 - l2x1) * (l2y2 - l2y1) - (l1y2 - l2y1) * (l2x2 - l2x1))) > 0
//                || (((l2x1 - l1x1) * (l1y2 - l1y1) - (l2y1 - l1y1) * (l1x2 - l1x1))
//                * ((l2x2 - l1x1) * (l1y2 - l1y1) - (l2y2 - l1y1) * (l1x2 - l1x1))) > 0) {
//            return false;
//        }
//        return true;
//    }

    private boolean isCoordinatePoint(float x1, float y1, float x2, float y2) {
        return lineSpace(x1, y1, x2, y2) < (dotRadius * 2);
    }

    // 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
    private double pointToLine(PointF pointF, int x1, int y1, int x2, int y2) {
        int x0 = (int) pointF.x;
        int y0 = (int) pointF.y;
        double space = 0;
        double a, b, c;
        a = lineSpace(x1, y1, x2, y2);// 线段的长度
        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
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

    // 计算两点之间的距离
    private double lineSpace(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private float percentageToNumerical(float percentage, boolean isX) {
        float numerical = (isX ? viewWidth : viewHeight) * percentage / 100 / 10000;
        return new BigDecimal(numerical).setScale(5, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private int numericalToPercentage(float numerical, boolean isX) {
        float percentage = numerical / (isX ? viewWidth : viewHeight) * 100 * 10000;
        return (int) percentage;
    }

    public interface OnButtonEnabledListener {
        void onButtonEnabledChange(boolean confirmEnable, boolean resetEnable,
                                   boolean deleteEnable, boolean clearEnable);

        void onIsIntersection();
    }

    public static class VirtualFenceDotInfo {
        private float offsetX;
        private float offsetY;
        private boolean isEdit = false;
        private double angle;
        private int radius;

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        public float getOffsetX() {
            return offsetX;
        }

        public void setOffsetX(float offsetX) {
            this.offsetX = offsetX;
        }

        public float getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(float offsetY) {
            this.offsetY = offsetY;
        }

        public boolean isEdit() {
            return isEdit;
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        @Override
        public String toString() {
            return "AlarmLineInfo{" +
                    "startX=" + offsetX +
                    ", startY=" + offsetY +
                    ", isEdit=" + isEdit +
                    ", angle=" + angle +
                    ", radius=" + radius +
                    '}';
        }
    }
}