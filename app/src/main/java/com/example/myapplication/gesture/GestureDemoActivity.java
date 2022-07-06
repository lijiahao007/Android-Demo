package com.example.myapplication.gesture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;

public class GestureDemoActivity extends AppCompatActivity {

    private LogView logView;
    private GestureDetector gestureDetector;
    private TextView textView;
    private LinearLayout llContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_demo);

        logView = findViewById(R.id.log_view);
        textView = findViewById(R.id.textView);
        gestureDetector = new GestureDetector(this, new MyGestureListener());

        llContainer = findViewById(R.id.llContainer);
        llContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("GestureDemoActivity", "llContainer onTouch");
                gestureDetector.onTouchEvent(event);
                textView.onTouchEvent(event);
                return true; // 必须返回为true，MyGestureListener中全部动作才可以监听到。
            }
        });


    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            logView.addLog("onLongPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            logView.addLog("onSingleTapUp");
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            logView.addLog("onScroll distanceX=" + distanceX + " distanceY=" + distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            logView.addLog("onFling velocityX=" + velocityX + " velocityY=" + velocityY);
            logView.addLog("\t\te1.getX()=" + e1.getX() + " e1.getY()=" + e1.getY());
            logView.addLog("\t\te2.getX()=" + e2.getX() + " e2.getY()=" + e2.getY());
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            logView.addLog("onShowPress");
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            logView.addLog("onDown");
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            logView.addLog("onDoubleTap");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            logView.addLog("onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            logView.addLog("onSingleTapConfirmed");
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            logView.addLog("onContextClick");
            return super.onContextClick(e);
        }
    }
}