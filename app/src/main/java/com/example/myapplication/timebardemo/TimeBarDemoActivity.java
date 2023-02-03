package com.example.myapplication.timebardemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityTimeBarDemoBinding;

public class TimeBarDemoActivity extends BaseActivity<ActivityTimeBarDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.nestScrollView.setSelected(true);

        boolean selected = binding.timeBar.isSelected();
        Log.i(TAG, "" + selected);

        mBaseActivityHandler.postDelayed(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1000L);
    }
}