package com.example.myapplication.rubbish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityEnableDemoBinding;

public class EnableDemoActivity extends BaseActivity<ActivityEnableDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (binding.textView.isActivated()) {
//                    showToast("activated");
//                } else {
//                    showToast("not activated");
//                }
//
//                binding.textView.setActivated(!binding.textView.isActivated());
//            }
//        });


        binding.myView.setEnabled(false);

        binding.myView.onDisableClick = new MyView.OnDisableClick() {
            @Override
            public void onDisableClick(View view) {
                Log.i(TAG, "onDisableClick");
            }
        };

        binding.myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("MyView", "!!!!");
                return false;
            }
        });


//            binding.myView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

        binding.btnAddColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimatorSet set1 = new AnimatorSet();
                ObjectAnimator translationX = ObjectAnimator.ofFloat(binding.viewAnim, "translationX", -200);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(binding.viewAnim, "translationY", -200);
                set1.playTogether(translationX, translationY);
                set1.setDuration(1000);

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.viewAnim, "scaleX", 0, 1);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.viewAnim, "scaleY", 0, 1);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleY, scaleX);
                animatorSet.setDuration(1000);

                AnimatorSet set2 = new AnimatorSet();
                set2.playSequentially(set1, animatorSet);
                set2.start();

                set1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.viewAnim.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
}