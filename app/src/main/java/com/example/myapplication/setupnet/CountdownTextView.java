package com.example.myapplication.setupnet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class CountdownTextView extends androidx.appcompat.widget.AppCompatTextView {

    private int countDownInterval;
    private int millisInFuture;
    private CountDownTimer timer;
    private String text = "0";
    private OnFinishCallback onFinishCallback = null;

    public CountdownTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public CountdownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CountdownTextView);
            countDownInterval = typedArray.getInteger(R.styleable.CountdownTextView_countDownInterval, 0);
            millisInFuture = typedArray.getInteger(R.styleable.CountdownTextView_millisInFuture, 0);
            timer = new TextViewCountDownTimer(millisInFuture, countDownInterval);
            typedArray.recycle();
            text = millisInFuture / 1000 + "";
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, 0, getHeight(), getPaint());
    }

    public void setCountDownParam(int millisInFuture, int countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        text = String.valueOf(millisInFuture / 1000);
        timer = new TextViewCountDownTimer(millisInFuture, countDownInterval);
    }

    public void startCountDown() {
        if (timer != null) {
            timer.start();
        }
    }

    public void setOnFinishCallback(OnFinishCallback onFinishCallback) {
        this.onFinishCallback = onFinishCallback;
    }

    interface OnFinishCallback {
        void onFinish();
    }

    public void stopCountDown() {
        if (timer != null) {
            timer.cancel();
        }
    }

    class TextViewCountDownTimer extends CountDownTimer {

        public TextViewCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisInFuture % 1000 == 0) {
                text = String.valueOf(millisUntilFinished / 1000);
                Log.i("CountdownTextView", "onTick: " + text);
                invalidate();
            }
        }

        @Override
        public void onFinish() {
            if (onFinishCallback != null) {
                onFinishCallback.onFinish();
            }
        }
    }

}
