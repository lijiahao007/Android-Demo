package com.example.myapplication.statusinset;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySoftInputState2Binding;

public class SoftInputState2Activity extends BaseActivity<ActivitySoftInputState2Binding> {

    @Override
    protected int[] bindClick() {
        return new int[]{R.id.btn_back};
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();

        // 1. state_unspecified : 是softInputMode的默认值，
        attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
        window.setAttributes(attributes);
//
//        binding.editText.setFocusable(true);
//        binding.editText.setFocusableInTouchMode(true);
//        binding.editText.requestFocus();
    }
}