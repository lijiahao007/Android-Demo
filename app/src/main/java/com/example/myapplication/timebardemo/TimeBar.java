package com.example.myapplication.timebardemo;

import static com.example.myapplication.timebardemo.TimeBar.TimeBarInfo.STATE_BOUNDER_MASK;
import static com.example.myapplication.timebardemo.TimeBar.TimeBarInfo.STATE_CRY_MASK;
import static com.example.myapplication.timebardemo.TimeBar.TimeBarInfo.STATE_HUMAN_MASK;
import static com.example.myapplication.timebardemo.TimeBar.TimeBarInfo.STATE_MOVE_MASK;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.example.myapplication.utils.DimenUtil;

import org.joda.time.LocalTime;

import java.util.ArrayList;

public class TimeBar extends View {

    private float chunkDividerHeight; // 块与块之间的间隙高度
    private int chunkNumber; // 块的数量
    private float singleChunkHeight; // 块的高度，每次onDraw时计算
    private Paint disablePaint;
    private Paint enablePaint;
    private int firstChooseChunkIndex;
    private int lastChooseChunkIndex;
    private boolean isChoosingNow;
    private float chooseBoxStrokeWidth;
    private final static String TAG = "TimeBar";


    private ArrayList<TimeBarInfo> enableTimeBarInfoList = new ArrayList<TimeBarInfo>() {{
        add(new TimeBarInfo(new LocalTime(4, 0), new LocalTime(8, 0), 15));
        add(new TimeBarInfo(new LocalTime(12, 0), new LocalTime(15, 0), 7));
        add(new TimeBarInfo(new LocalTime(18, 0), new LocalTime(21, 0), 3));
        add(new TimeBarInfo(new LocalTime(22, 0), new LocalTime(23, 0), 1));
    }};

    private Drawable moveAlarmDrawable;

    private int drawableIntrinsicHeight; // 每个图标的高度（理论上都要一样）
    private int drawableIntrinsicWidth; // 每个图标的宽度（理论上都要一样）
    private int drawableNumPerLine = 4; // 每行图标的数量
    private int drawableDividerWidth; // 一行图标中图标的间距
    private int drawableDividerHeight; // 两行图标之间的间距
    private float textHeight;
    private Paint chooseBoxPaint;
    private float oneDb;

    public TimeBar(Context context) {
        super(context);
        initView();
    }

    public TimeBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        int enableColor = getResources().getColor(R.color.color_80acff);
        int disableColor = getResources().getColor(R.color.color_e5e5e5);
        int shadowColor = getResources().getColor(R.color.color_33000000);

        chunkDividerHeight = DimenUtil.dp2px(getContext(), 2);
        chunkNumber = 24;

        enablePaint = new Paint();
        enablePaint.setColor(enableColor);
        enablePaint.setShadowLayer(5, 5, 5, shadowColor);
        enablePaint.setStyle(Paint.Style.FILL);

        disablePaint = new Paint();
        disablePaint.setColor(disableColor);

        moveAlarmDrawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.alarmset_icon_move, null);
        if (moveAlarmDrawable != null) {
            drawableIntrinsicHeight = moveAlarmDrawable.getIntrinsicHeight();
            drawableIntrinsicWidth = moveAlarmDrawable.getIntrinsicWidth();
        }

        drawableDividerWidth = (int) DimenUtil.dp2px(getContext(), 4);
        drawableDividerHeight = (int) DimenUtil.dp2px(getContext(), 4);

        chooseBoxPaint = new Paint();
        oneDb = DimenUtil.dp2px(getContext(), 1);
        int chooseBoxColor = getResources().getColor(R.color.color_4b89ff);
        chooseBoxStrokeWidth = oneDb;
        chooseBoxPaint.setColor(chooseBoxColor);
        chooseBoxPaint.setStyle(Paint.Style.STROKE);
        chooseBoxPaint.setShadowLayer(oneDb * 15, oneDb * 5, oneDb * 5, shadowColor);
        chooseBoxPaint.setStrokeWidth(chooseBoxStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = (int) (getWidth() - Math.max(getPaddingRight(), getPaddingEnd()));
        int height = (int) (getHeight() - getPaddingBottom());

        singleChunkHeight = (height - chunkDividerHeight * 23.0f) / 24.0f;

        // 1. 画出灰色方块
        float curTop = 0;
        for (int i = 0; i < chunkNumber; i++) {
            canvas.drawRect(0, curTop, width, curTop + singleChunkHeight, disablePaint);
            curTop += singleChunkHeight + chunkDividerHeight;
        }

        for (int i = 0; i < enableTimeBarInfoList.size(); i++) {
            TimeBarInfo timeBarInfo = enableTimeBarInfoList.get(i);
            int beginHour = timeBarInfo.beginTime.getHourOfDay();
            int endHour = timeBarInfo.endTime.getHourOfDay();

            float top = beginHour * (chunkDividerHeight + singleChunkHeight);
            float bottom = endHour * (chunkDividerHeight + singleChunkHeight) - chunkDividerHeight;

            float enableChunkHeight = bottom - top;

            // 2. 画出初始的蓝色方块
            canvas.drawRect(0, top, width, bottom, enablePaint);

            // 3. 画对应报警类型的图标
            ArrayList<Drawable> chunkAlarmDrawable = getChunkAlarmDrawable(timeBarInfo.state);
            int totalRow = chunkAlarmDrawable.size() % drawableNumPerLine == 0 ?
                    chunkAlarmDrawable.size() / drawableNumPerLine :
                    chunkAlarmDrawable.size() / drawableNumPerLine + 1;
            int lastRowDrawableNum = chunkAlarmDrawable.size() % drawableNumPerLine == 0 ?
                    drawableNumPerLine :
                    chunkAlarmDrawable.size() % drawableNumPerLine;
            float drawableSize = drawableIntrinsicHeight;
            if (totalRow * drawableSize + drawableDividerHeight * totalRow > enableChunkHeight) {
                // 如果drawable的高度加上间距大于Chunk的高度，则缩小drawable的高度
                drawableSize = (enableChunkHeight - totalRow * drawableDividerHeight) / totalRow;
            }

            for (int index = 0; index < chunkAlarmDrawable.size(); index++) {
                int rowIndex = index / drawableNumPerLine;
                int colIndex = index % drawableNumPerLine;

                // 当前行图标数量
                int drawableNum = 0;
                if (rowIndex < totalRow - 1) {
                    // 当前行不是最后一行
                    drawableNum = drawableNumPerLine;
                } else {
                    drawableNum = lastRowDrawableNum;
                }
                Drawable drawable = chunkAlarmDrawable.get(index);

                float leftOffset = getDrawableLeftOffset(colIndex, drawableNum, width, drawableSize, drawableDividerWidth);
                float topOffset = getDrawableTopOffset(rowIndex, totalRow, enableChunkHeight, drawableSize, drawableDividerHeight);

                drawable.setBounds((int) leftOffset, (int) (top + topOffset), (int) (leftOffset + drawableSize), (int) (top + topOffset + drawableSize));
                drawable.draw(canvas);
            }
        }


        // 画选中框
        if (isChoosingNow) {
            float firstChunk = getChunkTop(firstChooseChunkIndex, singleChunkHeight, chunkDividerHeight) - 3;
            float lastChunk = getChunkTop(lastChooseChunkIndex, singleChunkHeight, chunkDividerHeight) + 3;
            canvas.drawRect(0 + chooseBoxStrokeWidth / 2, firstChunk, width - chooseBoxStrokeWidth / 2, lastChunk + singleChunkHeight, chooseBoxPaint);
        }
    }

    private float getChunkTop(int index, float chunkHeight, float chunkDividerHeight) {
        float top = 0;
        for (int i = 0; i < index; i++) {
            top += chunkHeight + chunkDividerHeight;
        }
        return top;
    }


    /**
     * 计算Drawable的左边位置
     *
     * @param colIndex              目标Drawable，在当前行所在的位置
     * @param columnNum             当前行存在的Drawable数量
     * @param width                 Chunk的宽度
     * @param drawableWidth         drawable的高度
     * @param drawableIntervalWidth Drawable之间的间隔
     */
    private float getDrawableLeftOffset(int colIndex, int columnNum, float width, float drawableWidth, float drawableIntervalWidth) {
        // 1. 计算行中第一个的 left 位置
        float firstOffset = width / 2 - drawableWidth * columnNum / 2 - (columnNum - 1) * drawableIntervalWidth / 2;

        // 2. 计算第 colIndex 的left位置
        return colIndex * (drawableWidth + drawableIntervalWidth) + firstOffset;
    }

    /**
     * 计算Drawable的顶部位置
     */
    private float getDrawableTopOffset(int rowIndex, int totalRow, float height, float drawableHeight, float drawableIntervalHeight) {
        // 1. 计算第一行的top位置
        float firstOffset = height / 2 - drawableHeight * totalRow / 2 - (totalRow - 1) * drawableIntervalHeight / 2;

        // 2. 计算第 colIndex 的left位置
        return rowIndex * (drawableHeight + drawableIntervalHeight) + firstOffset;
    }

    private ArrayList<Drawable> getChunkAlarmDrawable(int state) {
        ArrayList<Drawable> res = new ArrayList<>();

        // TODO: 等其他图标切图上传后更新下面的Drawable
        if ((state & STATE_MOVE_MASK) == STATE_MOVE_MASK) {
            res.add(moveAlarmDrawable);
        }

        if ((state & STATE_HUMAN_MASK) == STATE_HUMAN_MASK) {
            res.add(moveAlarmDrawable);
        }

        if ((state & STATE_BOUNDER_MASK) == STATE_BOUNDER_MASK) {
            res.add(moveAlarmDrawable);
        }

        if ((state & STATE_CRY_MASK) == STATE_CRY_MASK) {
            res.add(moveAlarmDrawable);
            res.add(moveAlarmDrawable);
            res.add(moveAlarmDrawable);
            res.add(moveAlarmDrawable);
            res.add(moveAlarmDrawable);
        }

        return res;
    }

    private int getChunkIndex(float y) {
        return (int) (y / (singleChunkHeight + chunkDividerHeight));
    }

    int downChunkIndex = 0;
    int upChunkIndex = 0;
    int curChunkIndex = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        Log.i(TAG, "x" + x + " y" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isChoosingNow = true;
                downChunkIndex = getChunkIndex(y);
                Log.i(TAG, "ACTION_DOWN" + downChunkIndex + " " + upChunkIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                curChunkIndex = getChunkIndex(y);
                firstChooseChunkIndex = Math.min(downChunkIndex, curChunkIndex);
                lastChooseChunkIndex = Math.max(downChunkIndex, curChunkIndex);
                Log.i(TAG, "ACTION_MOVE" + downChunkIndex + " " + curChunkIndex);
                break;
            case MotionEvent.ACTION_UP:
                upChunkIndex = getChunkIndex(y);
                Log.i(TAG, "ACTION_UP" + downChunkIndex + " " + upChunkIndex);
                isChoosingNow = false;
                break;
        }
        invalidate();


        return true;
    }

    public static class TimeBarInfo {
        private LocalTime beginTime;
        private LocalTime endTime;
        private int state;
        public static final int STATE_MOVE_MASK = 1;
        public static final int STATE_HUMAN_MASK = 2;
        public static final int STATE_BOUNDER_MASK = 4;
        public static final int STATE_CRY_MASK = 8;


        public TimeBarInfo(LocalTime beginTime, LocalTime endTime, int state) {
            this.beginTime = beginTime;
            this.endTime = endTime;
            this.state = state;
        }

        public LocalTime getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(LocalTime beginTime) {
            this.beginTime = beginTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void addState(int state) {
            this.state |= state;
        }

        public void removeState(int state) {
            this.state &= ~state;
        }

        @Override
        public String toString() {
            return "TimeBarInfo{" +
                    "beginTime=" + beginTime +
                    ", endTime=" + endTime +
                    ", state=" + state +
                    '}';
        }
    }

}
