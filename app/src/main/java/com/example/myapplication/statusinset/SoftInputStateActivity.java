package com.example.myapplication.statusinset;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySoftInputStateBinding;

public class SoftInputStateActivity extends BaseActivity<ActivitySoftInputStateBinding> {

    @Override
    protected int[] bindClick() {
        return new int[] { R.id.btn_launch_activity_next};
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_launch_activity_next:
                startActivity(new Intent(this, SoftInputState2Activity.class));
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
    }
}