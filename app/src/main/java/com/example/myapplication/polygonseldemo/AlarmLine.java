package com.example.myapplication.polygonseldemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AlarmLine extends View {
    private static final String TAG = "AlarmLine";
    private static final int MAX_LINE = 4;
    private ArrayList<AlarmLineInfo> alarmLineInfoList = new ArrayList<>();
    private ArrayList<AlarmLineInfo> alarmLineInfoListCopy = new ArrayList<>();
    private Paint paintEdit = null;
    private Paint paintNoEdit = null;
    private int lineColorEdit;
    private int lineColorNoEdit;
    private int colorWhite;
    private int strokeWidth;
    private int minLenght;
    private int padding;
    private int editDotRadius;
    private int viewWidth;
    private int viewHeight;
    private boolean isCanClick = false;
    private boolean isModify = false;
    private boolean isGetData = false;
    private final int MIN_AMOUNT = 0;
    private final int MAX_AMOUNT = 4;
    private OnButtonEnabledListener onButtonEnabledListener;

    public AlarmLine(Context context) {
        super(context);
        init(context);
    }

    public AlarmLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AlarmLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("NewApi")
    public AlarmLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        padding = viewHeight > viewWidth ? viewHeight : viewWidth * 2 / 100;
        minLenght = viewHeight > viewWidth ? viewHeight : viewWidth * 5 / 100;
        Log.i(TAG, "padding=" + padding + "  viewHeight=" + viewHeight + " viewWidth=" + viewWidth);
    }

    private void init(Context context) {
        strokeWidth = (int) DimenUtil.dp2px(context, 2);
        editDotRadius = strokeWidth * 4;
        padding = strokeWidth;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        lineColorEdit = context.getResources().getColor(R.color.color_invariant_FF3131);
        lineColorNoEdit = context.getResources().getColor(R.color.color_invariant_FF3131);
        colorWhite = context.getResources().getColor(R.color.color_invariant_ffffff);

        paintEdit = new Paint();
        paintEdit.setAntiAlias(true);
        paintEdit.setColor(lineColorEdit);
        paintEdit.setStrokeWidth(strokeWidth);
        paintEdit.setPathEffect(new DashPathEffect(new float[]{strokeWidth * 3, strokeWidth}, 0));

        paintNoEdit = new Paint();
        paintNoEdit.setAntiAlias(true);
        paintNoEdit.setColor(lineColorNoEdit);
        paintNoEdit.setStrokeWidth(strokeWidth);

    }

    public int addLine() {
        Log.e(TAG, "addLine");
        if (alarmLineInfoList == null) {
            alarmLineInfoList = new ArrayList<>();
        }
        Log.d(TAG, "addLine alarmLineInfoList size = " + alarmLineInfoList.size());
        if (alarmLineInfoList.size() >= MAX_LINE) {
            return alarmLineInfoList.size();
        }
        isModify = true;
        for (int i = 0; i < alarmLineInfoList.size(); i++) {
            alarmLineInfoList.get(i).setEdit(false);
        }
        int tempX = viewWidth / 3; //屏蔽宽度的1/3
        int tempY = viewHeight / (MAX_LINE + 1); //屏蔽高度的1/5
        AlarmLineInfo alarmLineInfo = new AlarmLineInfo();
        alarmLineInfo.setStartX(tempX);
        alarmLineInfo.setStartY(tempY * alarmLineInfoList.size() + tempY); //根据当前数量调整Y坐标
        alarmLineInfo.setEndX(tempX * 2); //屏蔽宽度的2/3为结束点X坐标
        alarmLineInfo.setEndY(tempY * alarmLineInfoList.size() + tempY);
        alarmLineInfo.setEdit(true);
        alarmLineInfoList.add(alarmLineInfo);
        Log.d(TAG, "after addLine alarmLineInfoList size = " + alarmLineInfoList.size());
        if (/*!isInit && */onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true,
                    true, true, true,
                    alarmLineInfoList.size() < MAX_LINE);
        }
        invalidate();
        return alarmLineInfoList.size();
    }

    public void setOnButtonEnabledListener(OnButtonEnabledListener buttonEnabledListener) {
        this.onButtonEnabledListener = buttonEnabledListener;
    }

    public void setIsCanClick(boolean isCanClick) {
        this.isCanClick = isCanClick;
        invalidate();
    }

    public void initData(ArrayList<VirtualFenceInfo.LineInfo> dataList, boolean isCanClick) {
        isModify = false;
        this.isCanClick = isCanClick;
        alarmLineInfoList.clear();
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        Log.i(TAG, "initData-" + dataList);

        for (int i = 0; i < dataList.size(); i++) {
            AlarmLineInfo alarmLineInfo = new AlarmLineInfo();
            alarmLineInfo.setEdit(false);
            alarmLineInfo.setOrientation(dataList.get(i).getAlarmtype());
            alarmLineInfo.setStartX(percentageToNumerical(
                    dataList.get(i).getStartX(), true));
            alarmLineInfo.setStartY(percentageToNumerical(
                    dataList.get(i).getStartY(), false));
            alarmLineInfo.setEndX(percentageToNumerical(
                    dataList.get(i).getEndX(), true));
            alarmLineInfo.setEndY(percentageToNumerical(
                    dataList.get(i).getEndY(), false));
            alarmLineInfoList.add(alarmLineInfo);

            Log.i(TAG, "getStartX=" + percentageToNumerical(dataList.get(i).getStartX(), true) +
                    " startY=" + percentageToNumerical(dataList.get(i).getStartY(), false) +
                    " endX=" + percentageToNumerical(dataList.get(i).getEndX(), true) +
                    " endY=" + percentageToNumerical(dataList.get(i).getEndY(), false));

            Log.i(TAG, "getXPrecent" + numericalToPercentage(200, true));
            Log.e("xdt_test_20220427_1", "virtualFenceInfo = " + alarmLineInfo.toString());
        }
        alarmLineInfoListCopy.clear();
        for (int i = 0; i < dataList.size(); i++) {
            AlarmLineInfo alarmLineInfo = new AlarmLineInfo();
            alarmLineInfo.setEdit(false);
            alarmLineInfo.setOrientation(dataList.get(i).getAlarmtype());
            alarmLineInfo.setStartX(percentageToNumerical(
                    dataList.get(i).getStartX(), true));
            alarmLineInfo.setStartY(percentageToNumerical(
                    dataList.get(i).getStartY(), false));
            alarmLineInfo.setEndX(percentageToNumerical(
                    dataList.get(i).getEndX(), true));
            alarmLineInfo.setEndY(percentageToNumerical(
                    dataList.get(i).getEndY(), false));
            alarmLineInfoListCopy.add(alarmLineInfo);
        }
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(false,
                    false, false, false,
                    alarmLineInfoList.size() < MAX_LINE);
        }
        invalidate();
    }

//    public ArrayList<VirtualFenceInfo.LineInfo> getData() {
//        ArrayList<VirtualFenceInfo.LineInfo> dataList = new ArrayList<>();
//        for (int i = 0; i < alarmLineInfoList.size(); i++) {
//            VirtualFenceInfo.LineInfo lineInfo = new VirtualFenceInfo.LineInfo();
//            lineInfo.setAlarmtype(alarmLineInfoList.get(i).getOrientation());
//            lineInfo.setStartX(numericalToPercentage(
//                    alarmLineInfoList.get(i).getStartX(), true));
//            lineInfo.setStartY(numericalToPercentage(
//                    alarmLineInfoList.get(i).getStartY(), false));
//            lineInfo.setEndX(numericalToPercentage(
//                    alarmLineInfoList.get(i).getEndX(), true));
//            lineInfo.setEndY(numericalToPercentage(
//                    alarmLineInfoList.get(i).getEndY(), false));
//            dataList.add(lineInfo);
//            Log.e("xdt_test_20220427_2", "lineInfo = " + lineInfo.toString());
//        }
//        isGetData = true;
////        alarmLineInfoList.clear();
////        invalidate();
//        return dataList;
//    }

    public ArrayList<VirtualFenceInfo.LineInfo> getData() {
        ArrayList<VirtualFenceInfo.LineInfo> dataList = new ArrayList<>();
        for (int i = 0; i < alarmLineInfoList.size(); i++) {
            VirtualFenceInfo.LineInfo lineInfo = new VirtualFenceInfo.LineInfo();
            AlarmLineInfo alarmLineInfo = alarmLineInfoList.get(i);

            int alarmType = alarmLineInfo.getOrientation();
            float startX = alarmLineInfo.getStartX();
            float startY = alarmLineInfo.getStartY();
            float endX = alarmLineInfo.getEndX();
            float endY = alarmLineInfo.getEndY();

            if (alarmType == AlarmLineInfo.ORIENTATION_BOTHWAY) {
                lineInfo.setAlarmtype(alarmType);
                lineInfo.setStartX(numericalToPercentage(startX, true));
                lineInfo.setStartY(numericalToPercentage(startY, false));
                lineInfo.setEndX(numericalToPercentage(endX, true));
                lineInfo.setEndY(numericalToPercentage(endY, false));
            } else {
                double angle = calulateXYAnagle(startX, startY, endX, endY);
                if (angle > 180 && angle < 360) {
                    if (alarmType == AlarmLineInfo.ORIENTATION_RIGHT) {
                        lineInfo.setAlarmtype(AlarmLineInfo.ORIENTATION_LEFT);
                    } else {
                        lineInfo.setAlarmtype(AlarmLineInfo.ORIENTATION_RIGHT);
                    }
                    lineInfo.setStartX(numericalToPercentage(endX, true));
                    lineInfo.setStartY(numericalToPercentage(endY, false));
                    lineInfo.setEndX(numericalToPercentage(startX, true));
                    lineInfo.setEndY(numericalToPercentage(startY, false));
                } else {
                    if (startY == endY && startX > endX) {
                        if (alarmType == AlarmLineInfo.ORIENTATION_RIGHT) {
                            lineInfo.setAlarmtype(AlarmLineInfo.ORIENTATION_LEFT);
                        } else {
                            lineInfo.setAlarmtype(AlarmLineInfo.ORIENTATION_RIGHT);
                        }
                        lineInfo.setStartX(numericalToPercentage(endX, true));
                        lineInfo.setStartY(numericalToPercentage(endY, false));
                        lineInfo.setEndX(numericalToPercentage(startX, true));
                        lineInfo.setEndY(numericalToPercentage(startY, false));
                    } else {
                        lineInfo.setAlarmtype(alarmType);
                        lineInfo.setStartX(numericalToPercentage(startX, true));
                        lineInfo.setStartY(numericalToPercentage(startY, false));
                        lineInfo.setEndX(numericalToPercentage(endX, true));
                        lineInfo.setEndY(numericalToPercentage(endY, false));
                    }
                }
            }

            dataList.add(lineInfo);
            Log.e(TAG, "lineInfo = " + lineInfo.toString());
        }
        isGetData = true;
        return dataList;
    }

    public boolean isGreaterThanMinimum() {
        return alarmLineInfoList.size() > MIN_AMOUNT;
    }

    public void rotationLine() {
        Log.e(TAG, "rotationLine");
        if (alarmLineInfoList == null) {
            alarmLineInfoList = new ArrayList<>();
        }
        Log.d(TAG, "rotationLine alarmLineInfoList size = " + alarmLineInfoList.size());
        if (alarmLineInfoList.size() == 0) {
//            ToastUtils.showToastCenter("请先添加线");
        } else {
            isModify = true;
            for (int i = 0; i < alarmLineInfoList.size(); i++) {
                AlarmLineInfo data = alarmLineInfoList.get(i);
                if (data.isEdit()) {
                    int orientation = (data.getOrientation() + 1) % 3;
                    data.setOrientation(orientation == 0 ? 3 : orientation);
                    break;
                }
            }
        }
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true,
                    true, true, true,
                    alarmLineInfoList.size() < MAX_LINE);
        }
        invalidate();
    }

    public void resetLine() {
        Log.e(TAG, "resetLine");
        if (isGetData) {
            isGetData = false;
            isModify = true;
        } else {
            isModify = false;
        }
        alarmLineInfoList.clear();
        for (int i = 0; i < alarmLineInfoListCopy.size(); i++) {
            AlarmLineInfo data = new AlarmLineInfo();
            data.setStartX(alarmLineInfoListCopy.get(i).getStartX());
            data.setStartY(alarmLineInfoListCopy.get(i).getStartY());
            data.setEndX(alarmLineInfoListCopy.get(i).getEndX());
            data.setEndY(alarmLineInfoListCopy.get(i).getEndY());
            data.setEdit(alarmLineInfoListCopy.get(i).isEdit());
            data.setOrientation(alarmLineInfoListCopy.get(i).getOrientation());
            alarmLineInfoList.add(data);
        }
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(false,
                    false, false, false,
                    alarmLineInfoList.size() < MAX_LINE);
        }
        invalidate();
    }

    public void deleteLine() {
        Log.e(TAG, "deleteLine");
        isModify = true;
        for (int i = 0; i < alarmLineInfoList.size(); i++) {
            if (alarmLineInfoList.get(i).isEdit()) {
                alarmLineInfoList.remove(i);
            }
        }
        if (alarmLineInfoList.size() > 0) {
            alarmLineInfoList.get(alarmLineInfoList.size() - 1).setEdit(true);
        }
        Log.d(TAG, "deleteLine alarmLineInfoList size = " + alarmLineInfoList.size());
        if (onButtonEnabledListener != null) {
            if (alarmLineInfoList.size() > 0) {
                onButtonEnabledListener.onButtonEnabledChange(true,
                        true, true, true,
                        alarmLineInfoList.size() < MAX_LINE);
            } else {
                onButtonEnabledListener.onButtonEnabledChange(false,
                        true, false, false,
                        alarmLineInfoList.size() < MAX_LINE);
            }
        }
        invalidate();
    }

    public void clearLine() {
        Log.e(TAG, "clearLine");
        isModify = true;
        alarmLineInfoList.clear();
        if (onButtonEnabledListener != null) {
            onButtonEnabledListener.onButtonEnabledChange(true,
                    true, false, false,
                    alarmLineInfoList.size() < MAX_LINE);
        }
        invalidate();
    }

    public void setModify(boolean modify) {
        isModify = modify;
    }

    public boolean isModify() {
        return isModify;
    }

    private final int CLICK_TYPE_EDFOUT = 0;
    private final int CLICK_TYPE_START_DOT = 1;
    private final int CLICK_TYPE_STOP_DOT = 2;
    private final int CLICK_TYPE_LINE = 3;

    private AlarmLineInfo clickInfo = null;
    private int clickType = CLICK_TYPE_EDFOUT;

    private float lastMoveXOffset;
    private float lastMoveYOffset;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanClick) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                CustomScrollView.setIsReturnFalse(false);
                break;
            case MotionEvent.ACTION_DOWN:
                clickType = CLICK_TYPE_EDFOUT;
                if (alarmLineInfoList != null) {
                    for (int i = 0; i < alarmLineInfoList.size(); i++) {
                        if (alarmLineInfoList.get(i).isEdit()) {
                            clickInfo = alarmLineInfoList.get(i);
                            if (isCoordinatePoint(event.getX(), event.getY(),
                                    clickInfo.getStartX(), clickInfo.getStartY())) {
                                clickType = CLICK_TYPE_START_DOT;
                                setSelect();
                            } else if (isCoordinatePoint(event.getX(), event.getY(),
                                    clickInfo.getEndX(), clickInfo.getEndY())) {
                                clickType = CLICK_TYPE_STOP_DOT;
                                setSelect();
                            } else if (isSelectLine(new PointF(event.getX(), event.getY()), clickInfo)) {
                                clickType = CLICK_TYPE_LINE;
                                setSelect();
                            }
                            CustomScrollView.setIsReturnFalse(clickType != CLICK_TYPE_EDFOUT);
                            if (clickType != CLICK_TYPE_EDFOUT) {
                                break;
                            }
                        }
                    }
                    if (clickType == CLICK_TYPE_EDFOUT) {
                        for (int i = 0; i < alarmLineInfoList.size(); i++) {
                            if (!alarmLineInfoList.get(i).isEdit()) {
                                clickInfo = alarmLineInfoList.get(i);
                                if (isCoordinatePoint(event.getX(), event.getY(),
                                        clickInfo.getStartX(), clickInfo.getStartY())) {
                                    clickType = CLICK_TYPE_START_DOT;
                                    setSelect();
                                } else if (isCoordinatePoint(event.getX(), event.getY(),
                                        clickInfo.getEndX(), clickInfo.getEndY())) {
                                    clickType = CLICK_TYPE_STOP_DOT;
                                    setSelect();
                                } else if (isSelectLine(new PointF(event.getX(), event.getY()), clickInfo)) {
                                    clickType = CLICK_TYPE_LINE;
                                    setSelect();
                                }
                                CustomScrollView.setIsReturnFalse(clickType != CLICK_TYPE_EDFOUT);
                                if (clickType != CLICK_TYPE_EDFOUT) {
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (clickInfo != null) {
                    Log.i(TAG, "ACTION_MOVE  " + "" + numericalToPercentage(clickInfo.getStartX(), true) + ", " + numericalToPercentage(clickInfo.getStartY(), false));

                    isModify = true;
                    float xOffset = event.getX() - lastMoveXOffset;
                    float yOffset = event.getY() - lastMoveYOffset;
                    float startX = clickInfo.getStartX() + xOffset;
                    float startY = clickInfo.getStartY() + yOffset;
                    float endX = clickInfo.getEndX() + xOffset;
                    float endY = clickInfo.getEndY() + yOffset;
                    switch (clickType) {
                        case CLICK_TYPE_LINE:
                            if (!(startX < padding || endX < padding
                                    || startX > viewWidth - padding || endX > viewWidth - padding)) {
                                clickInfo.setStartX(startX);
                                clickInfo.setEndX(endX);
                            } else {
                                Log.e("xdt_test_20210105_1", "X最大了");
                            }
                            if (!(startY < padding || endY < padding
                                    || startY > viewHeight - padding || endY > viewHeight - padding)) {
                                clickInfo.setStartY(startY);
                                clickInfo.setEndY(endY);
                            } else {
                                Log.e("xdt_test_20210105_1", "Y最大了");
                            }
                            invalidate();
                            break;
                        case CLICK_TYPE_START_DOT:
                            if (!(startX < padding || startX > viewWidth - padding)) {
                                if (lineSpace(startX, startY, clickInfo.getEndX(),
                                        clickInfo.getEndY()) < minLenght) {
                                    Log.e("xdt_test_20210105_1", "最小了");
                                } else {
                                    clickInfo.setStartX(startX);
                                }
                            }
                            if (!(startY < padding || startY > viewHeight - padding)) {
                                if (lineSpace(startX, startY, clickInfo.getEndX(),
                                        clickInfo.getEndY()) < minLenght) {
                                    Log.e("xdt_test_20210105_1", "最小了");
                                } else {
                                    clickInfo.setStartY(startY);
                                }
                            }
                            invalidate();
                            break;
                        case CLICK_TYPE_STOP_DOT:
                            if (!(endX < padding || endX > viewWidth - padding)) {
                                if (lineSpace(clickInfo.getStartX(),
                                        clickInfo.getStartY(), endX, endY) < minLenght) {
                                    Log.e("xdt_test_20210105_1", "最小了");
                                } else {
                                    clickInfo.setEndX(endX);
                                }
                            }
                            if (!(endY < padding || endY > viewHeight - padding)) {
                                if (lineSpace(clickInfo.getStartX(),
                                        clickInfo.getStartY(), endX, endY) < minLenght) {
                                    Log.e("xdt_test_20210105_1", "最小了");
                                } else {
                                    clickInfo.setEndY(endY);
                                }
                            }
                            invalidate();
                            break;
                    }
                }
                break;
        }
        lastMoveXOffset = event.getX();
        lastMoveYOffset = event.getY();
        return true;
    }

    private void setSelect() {
        Log.e(TAG, "setSelect");
        if (!clickInfo.isEdit()) {
            for (int j = 0; j < alarmLineInfoList.size(); j++) {
                alarmLineInfoList.get(j).setEdit(false);
            }
            clickInfo.setEdit(true);

            if (onButtonEnabledListener != null) {
                onButtonEnabledListener.onButtonEnabledChange(true,
                        true, true, true,
                        alarmLineInfoList.size() < MAX_LINE);
            }

            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw");
        if (alarmLineInfoList == null) return;
        for (int i = 0; i < alarmLineInfoList.size(); i++) {
            AlarmLineInfo data = alarmLineInfoList.get(i);
            if (!data.isEdit()) {
                drawContent(data, canvas);
            }
        }

        for (int i = 0; i < alarmLineInfoList.size(); i++) {
            AlarmLineInfo data = alarmLineInfoList.get(i);
            if (data.isEdit()) {
                drawContent(data, canvas);
            }
        }
    }

    private void drawContent(AlarmLineInfo data, Canvas canvas) {
        float midpointX = (data.getStartX() + data.getEndX()) / 2;
        float midpointY = (data.getStartY() + data.getEndY()) / 2;

        float tempX = data.getEndX();
        float tempY = data.getEndY();

        int height = (int) lineSpace(data.getStartX(), data.getStartY(), data.getEndX(), data.getEndY()) / 2;
        int bottom = strokeWidth * 4;

        float juli = (float) lineSpace(midpointX, midpointY, tempX, tempY);// 获取线段距离
        float juliX = tempX - midpointX;// 有正负，不要取绝对值
        float juliY = tempY - midpointY;// 有正负，不要取绝对值

        float dianX = tempX - (height / juli * juliX);
        float dianY = tempY - (height / juli * juliY);

        tempX = bottom / juli * juliX;
        tempY = bottom / juli * juliY;

        drawLine(midpointX, midpointY, dianX + tempY, dianY - tempX, data, canvas);
        drawTria(dianX + tempY, dianY - tempX, dianX - tempY,
                dianY + tempX, data.getOrientation(), canvas);
    }

    private void drawLine(float midpointX, float midpointY, float tempX, float tempY,
                          AlarmLineInfo data, Canvas canvas) {
        int height = strokeWidth * 4;
        int bottom = strokeWidth * 4;
        float juli = (float) lineSpace(midpointX, midpointY, tempX, tempY);// 获取线段距离

        float juliX = tempX - midpointX;// 有正负，不要取绝对值
        float juliY = tempY - midpointY;// 有正负，不要取绝对值

        float dianX = tempX - (height / juli * juliX);
        float dianY = tempY - (height / juli * juliY);

        tempX = bottom / juli * juliX;
        tempY = bottom / juli * juliY;
        if (data.isEdit()) {
            canvas.drawLine(data.getStartX(), data.getStartY(), dianX + tempY, dianY - tempX, paintEdit);
            canvas.drawLine(dianX - tempY, dianY + tempX, data.getEndX(), data.getEndY(), paintEdit);
            paintNoEdit.setColor(colorWhite);
            canvas.drawCircle(data.getStartX(), data.getStartY(), editDotRadius, paintNoEdit);
            canvas.drawCircle(data.getEndX(), data.getEndY(), editDotRadius, paintNoEdit);
            paintNoEdit.setColor(lineColorEdit);
            canvas.drawCircle(data.getStartX(), data.getStartY(), editDotRadius - strokeWidth, paintNoEdit);
            canvas.drawCircle(data.getEndX(), data.getEndY(), editDotRadius - strokeWidth, paintNoEdit);
        } else {
            paintNoEdit.setColor(lineColorNoEdit);
            canvas.drawLine(dianX - tempY, dianY + tempX, data.getEndX(), data.getEndY(), paintNoEdit);
            canvas.drawLine(data.getStartX(), data.getStartY(), dianX + tempY, dianY - tempX, paintNoEdit);
            canvas.drawCircle(data.getStartX(), data.getStartY(), strokeWidth * 1.5f, paintNoEdit);
            canvas.drawCircle(data.getEndX(), data.getEndY(), strokeWidth * 1.5f, paintNoEdit);
        }
    }

    protected void drawTria(float fromX, float fromY, float toX, float toY, int orientation, Canvas canvas) {
        int height = strokeWidth * 2;
        int bottom = strokeWidth * 2;
        // height和bottom分别为三角形的高与底的一半,调节三角形大小
        paintNoEdit.setColor(lineColorEdit);
        float juli = (float) lineSpace(fromX, fromY, toX, toY);// 获取线段距离

        float juliX = toX - fromX;// 有正负，不要取绝对值
        float juliY = toY - fromY;// 有正负，不要取绝对值

        float dianX = toX - (height / juli * juliX);
        float dianY = toY - (height / juli * juliY);

        float dian2X = fromX + (height / juli * juliX);
        float dian2Y = fromY + (height / juli * juliY);

        float tempX = bottom / juli * juliX;
        float tempY = bottom / juli * juliY;

        if (orientation == AlarmLineInfo.ORIENTATION_LEFT || orientation == AlarmLineInfo.ORIENTATION_BOTHWAY) {
            Path path = new Path();
            path.moveTo(toX, toY);// 此点为三边形的起点
            path.lineTo(dianX + tempY, dianY - tempX);
            path.lineTo(dianX - tempY, dianY + tempX);
            path.close(); // 使这些点构成封闭的三边形
            canvas.drawPath(path, paintNoEdit);
        }

        if (orientation == AlarmLineInfo.ORIENTATION_RIGHT || orientation == AlarmLineInfo.ORIENTATION_BOTHWAY) {
            Path path2 = new Path();
            path2.moveTo(fromX, fromY);// 此点为边形的起点
            path2.lineTo(dian2X + tempY, dian2Y - tempX);
            path2.lineTo(dian2X - tempY, dian2Y + tempX);
            path2.close(); // 使这些点构成封闭的三边形
            canvas.drawPath(path2, paintNoEdit);
        }

        fromX = (3 * fromX + toX) / 4;
        fromY = (3 * fromY + toY) / 4;

        toX = (fromX + toX * 3) / 4;
        toY = (fromY + toY * 3) / 4;

        canvas.drawLine(fromX, fromY, toX, toY, paintNoEdit);
    }

    private boolean isSelectLine(PointF pointF, AlarmLineInfo alarmLineInfo) {
        return pointToLine(pointF, alarmLineInfo) < Math.abs(strokeWidth * 5);
    }

    private boolean isCoordinatePoint(float x1, float y1, float x2, float y2) {
        return lineSpace(x1, y1, x2, y2) < strokeWidth * 8;
    }

    // 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
    private double pointToLine(PointF pointF, AlarmLineInfo alarmLineInfo) {
        int x0 = (int) pointF.x;
        int y0 = (int) pointF.y;
        int x1 = (int) alarmLineInfo.getStartX();
        int y1 = (int) alarmLineInfo.getStartY();
        int x2 = (int) alarmLineInfo.getEndX();
        int y2 = (int) alarmLineInfo.getEndY();
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
                                   boolean deleteEnable, boolean directionEnable,
                                   boolean addEnable);

        void onIsIntersection();
    }


//    private double getAngle(float contX, float contY, float nowX, float nowY) {
//        double tem = Math.atan((nowY - contY) / (nowX - contX));//反切!
//        tem = (nowX - contX) < 0 ? tem + 180 : (nowY - contY) > 0 ? tem : tem + 360;//象限补偿!
//        return tem;
//    }
//
//    //两点连线与水平线夹角
//    private int calAngle(double x1, double y1, double x2, double y2) {
//        double x = Math.abs(x1 - x2);
//        double y = Math.abs(y1 - y2);
//        double z = Math.sqrt(x * x + y * y);
//        int angle = Math.round((float) (Math.asin(y / z) / Math.PI * 180));
//        return angle;
//    }

    private double calulateXYAnagle(double startx, double starty, double endx, double endy) {
        //除数不能为0
        double tan = Math.atan(Math.abs((endy - starty) / (endx - startx))) * 180 / Math.PI;

        Log.e(TAG, "tan = " + tan);
        if (endx > startx && endy > starty) {//第一象限
            Log.e(TAG, "第一象限");
            return tan;
        } else if (endx > startx && endy < starty) {//第四象限
            Log.e(TAG, "第四象限");
            return 360 - tan;
        } else if (endx < startx && endy > starty) {//第二象限
            Log.e(TAG, "第二象限");
            return 180 - tan;
        } else {
            Log.e(TAG, "第三象限");
            return 180 + tan;
        }
    }
}
