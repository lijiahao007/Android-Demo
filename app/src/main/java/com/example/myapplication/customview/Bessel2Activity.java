package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityBessel2Binding;
import com.example.myapplication.setupnet.CountdownTextView;

public class Bessel2Activity extends BaseActivity<ActivityBessel2Binding> {

    private CountdownTextView ctv;
    private Button btnStop;
    private Button btnStart;
    private ObjectAnimator objectAnimator;
    boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ctv = findViewById(R.id.ctv);
//        btnStart = findViewById(R.id.btnStart);
//        btnStop = findViewById(R.id.btnStop);
//
//        btnStart.setOnClickListener(view -> {
//            ctv.startCountDown();
//        });
//        btnStart.setOnClickListener(view -> {
//            ctv.stopCountDown();
//        });

        objectAnimator = ObjectAnimator.ofFloat(binding.imageView, "alpha", 0, 1, 0);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);

        binding.imageView.setOnClickListener(view -> {
            Log.i(TAG, "imageView" + isStop);
            if (!isStop) {
                objectAnimator.start();
            } else {
                objectAnimator.cancel();
            }
            isStop = !isStop;
        });


    }
}