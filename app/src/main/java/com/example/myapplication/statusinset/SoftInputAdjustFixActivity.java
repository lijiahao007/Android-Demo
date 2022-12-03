package com.example.myapplication.statusinset;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivitySoftInputAdjustFixBinding;
import com.example.myapplication.utils.DimenUtil;

public class SoftInputAdjustFixActivity extends BaseActivity<ActivitySoftInputAdjustFixBinding> {

    @Override
    protected int[] bindClick() {
        return super.bindClick();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        window.setAttributes(attributes);

        View decorView = window.getDecorView();
        decorView.setBackgroundColor(Color.RED);
        int oneDb = (int) DimenUtil.dp2px(this, 1);
        binding.imageView2.scrollTo(oneDb * 10, oneDb * 10);

    }
}