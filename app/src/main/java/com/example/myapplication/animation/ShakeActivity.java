package com.example.myapplication.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityShakeBinding;
import com.example.myapplication.utils.DimenUtil;

public class ShakeActivity extends BaseActivity<ActivityShakeBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(binding.textView, "translationX",
                dp2px(5), dp2px(-5),
                dp2px(10), dp2px(-10),
                dp2px(5), dp2px(-5), dp2px(0)
        );
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animator_shake);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);

        animator.setTarget(binding.textView1);


        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectAnimator.start();
            }
        });

        binding.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.start();
            }
        });


    }

    private float dp2px(int dp) {
        return DimenUtil.dp2px(getBaseContext(), dp);
    }

}