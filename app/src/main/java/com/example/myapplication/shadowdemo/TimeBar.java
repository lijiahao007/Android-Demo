package com.example.myapplication.shadowdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimeBar extends View {


    private int enableColor;
    private int disableColor;

    private ArrayList<TimeBarInfo> enableTimeList = new ArrayList<TimeBarInfo>() {
        {
            add(new TimeBarInfo(new LocalTime(8, 0), new LocalTime(12, 0)));
            add(new TimeBarInfo(new LocalTime(15, 0), new LocalTime(18, 0)));
            add(new TimeBarInfo(new LocalTime(20, 0), new LocalTime(22, 0)));
        }
    };
    private Paint disablePaint;
    private Paint enablePaint;
    private float oneDayMillis;

    public TimeBar(Context context) {
        super(context);
        init((AttributeSet) null, 0);
    }

    public TimeBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        enableColor = getContext().getResources().getColor(R.color.color_80acff);
        disableColor = getContext().getResources().getColor(R.color.color_e5e5e5_1);

        enablePaint = new Paint();
        enablePaint.setColor(enableColor);
        disablePaint = new Paint();
        disablePaint.setColor(disableColor);
        oneDayMillis = 24 * 60 * 60 * 1000;
    }


    /**
     * 会先对 enableTimeList 进行时间上的升序排序
     *
     * @param enableTimeList
     */
    public void setEnableTimeList(ArrayList<TimeBarInfo> enableTimeList) {
        this.enableTimeList = enableTimeList;
        Collections.sort(enableTimeList, new Comparator<TimeBarInfo>() {
            @Override
            public int compare(TimeBarInfo o1, TimeBarInfo o2) {
                if (o1.beginTime.isBefore(o2.beginTime)) {
                    return -1;
                } else if (o1.beginTime.isEqual(o2.beginTime)) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();

        // 1. 先画灰色的timeBar
        canvas.drawRect(0, 0, width, height, disablePaint);

        // 2. 画蓝色的timeBar
        for (int i = 0; i < enableTimeList.size(); i++) {
            TimeBarInfo timeBarInfo = enableTimeList.get(i);
            LocalTime beginTime = timeBarInfo.beginTime;
            LocalTime endTime = timeBarInfo.endTime;

            float beginHeight = (beginTime.getMillisOfDay() / oneDayMillis) * height;
            float endHeight = (endTime.getMillisOfDay() / oneDayMillis) * height;
            Log.i("TimeBar", "beginHeight=" + beginHeight + " endHeight=" + endHeight + " totalHeight=" + getHeight());
            canvas.drawRect(0, beginHeight, width, endHeight, enablePaint);
        }
    }


    public static class TimeBarInfo {
        LocalTime beginTime;
        LocalTime endTime;

        public TimeBarInfo(LocalTime beginTime, LocalTime endTime) {
            this.beginTime = beginTime;
            this.endTime = endTime;
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
    }
}
