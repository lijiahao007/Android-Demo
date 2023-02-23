package com.example.myapplication.customview1;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;

import androidx.coordinatorlayout.widget.ViewGroupUtils;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityTouchDelegateDemoBinding;
import com.macrovideo.sdk.tools.LogUtils;

public class TouchDelegateDemoActivity extends BaseActivity<ActivityTouchDelegateDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick btn");
            }
        });

        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick btn1");

            }
        });

        binding.btn1.setFocusable(false);

        binding.btn.post(() -> {
            Rect rect = new Rect();
            ViewGroupUtils.getDescendantRect(binding.rlParent, binding.btn, rect);
            Log.i(TAG, rect + "");
            rect.inset(-500, -500);
            binding.rlParent.setTouchDelegate(new TouchDelegate(rect, binding.btn));
        });

        function();
    }

    private void function() {
        // 下面代码会先执行`0~1000` 然后再执行a~z
        mBaseActivityHandler.post(() -> {
            for (int i = 0; i < 26; i++) {
                char ch = (char) ('a' + i);
                Log.i(TAG, "" + ch);
            }
        });
        for (int i = 0; i < 1000; i++) {
            Log.i(TAG, "" + i);
        }
    }
}