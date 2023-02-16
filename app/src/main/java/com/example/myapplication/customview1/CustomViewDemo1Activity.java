package com.example.myapplication.customview1;

import static androidx.constraintlayout.widget.ConstraintLayout.*;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityCustomViewDemo1Binding;

import java.util.Random;

public class CustomViewDemo1Activity extends BaseActivity<ActivityCustomViewDemo1Binding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Random random = new Random();
        // 1. scrollTo scrollBy只移动View的内容，不改变View在父布局的位置
        binding.scrollView.setOnClickListener(view -> {
            int height = binding.scrollView.getHeight();
            int width = binding.scrollView.getWidth();

            float destX = random.nextFloat() * width - width / 2f;
            float destY = random.nextFloat() * height - height / 2f;

            binding.scrollView.smoothMoveTo(destX, destY);
        });

        // 2. 使用属性动画会改变View在父布局中的位置。
        binding.scrollView1.setOnClickListener(view -> {
            binding.scrollView1.smoothMoveTo(100, 100);
        });


        // 3. 使用属性动画 & 布局属性 移动
        binding.viewLayout.setOnClickListener(view -> {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.viewLayout.getLayoutParams();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 500);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // 完成的比例
                    Float value = (Float) animation.getAnimatedValue();
                    layoutParams.leftMargin = (int) value.floatValue();
                    binding.viewLayout.requestLayout();
                }
            });
            valueAnimator.start();
        });

        binding.myDraggingView.setEnabled(false);
        // 4. 重写了OnTouchEvent后，OnClickListener回调不会被执行
        binding.myDraggingView.setOnClickListener(view -> {
            Log.i(TAG, "myDraggingView OnClickListener");
            showToast("OnClickListener");
        });

    }
}