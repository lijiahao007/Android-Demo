package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityCoordinateDemoBinding;

public class CoordinateDemoActivity extends BaseActivity<ActivityCoordinateDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.view1.setOnTouchListener(new View.OnTouchListener() {

            private int lastY;
            private int lastX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float viewX = event.getX();
                float viewY = event.getY();
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                Log.i(TAG, "onTouch 点击位置坐标: viewX = " + viewX + ", viewY = " + viewY + ", rawX = " + rawX + ", rawY = " + rawY);

                int left = v.getLeft();
                int right = v.getRight();
                int top = v.getTop();
                int bottom = v.getBottom();
                Log.i(TAG, "onTouch 点击View的坐标: left = " + left + ", right = " + right + ", top = " + top + ", bottom = " + bottom);

                Log.i("CustomView3", "action:" + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) viewX;
                        lastY = (int) viewY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float offsetX = viewX - lastX;
                        float offsetY = viewY - lastY;
                        v.layout((int)(left + offsetX), (int)(top + offsetY), (int)(right + offsetX), (int)((bottom + offsetY)));
                        break;
                }
                return true;
            }
        });

    }
}