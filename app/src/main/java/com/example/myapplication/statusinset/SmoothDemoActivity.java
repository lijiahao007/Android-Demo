package com.example.myapplication.statusinset;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySmoothDemoBinding;
import com.example.myapplication.utils.DimenUtil;

public class SmoothDemoActivity extends BaseActivity<ActivitySmoothDemoBinding> {

    @Override
    protected int[] bindClick() {
        return new int[] {
                R.id.btn_to_positive,
                R.id.btn_to_minus,
                R.id.btn_by_positive,
                R.id.btn_by_minus,
        };
    }

    private int dp2px(int dp) {
        return (int) DimenUtil.dp2px(this, dp);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_to_positive:
                binding.imageView1.scrollTo(dp2px(10), dp2px(10));
                binding.clLayout.scrollTo(dp2px(10), dp2px(10));
                binding.btnTest.scrollTo(dp2px(10), dp2px(10));

                break;
            case R.id.btn_to_minus:
                binding.imageView1.scrollTo(dp2px(-10), dp2px(-10));
                binding.clLayout.scrollTo(dp2px(-10), dp2px(-10));
                binding.btnTest.scrollTo(dp2px(-10), dp2px(-10));
                break;
            case R.id.btn_by_positive:
                binding.imageView1.scrollBy(dp2px(10), dp2px(10));
                binding.clLayout.scrollBy(dp2px(10), dp2px(10));
                binding.btnTest.scrollBy(dp2px(10), dp2px(10));
                break;
            case R.id.btn_by_minus:
                binding.imageView1.scrollBy(dp2px(-10), dp2px(-10));
                binding.clLayout.scrollBy(dp2px(-10), dp2px(-10));
                binding.btnTest.scrollBy(dp2px(-10), dp2px(-10));
                break;
        }
        binding.imageView1.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}