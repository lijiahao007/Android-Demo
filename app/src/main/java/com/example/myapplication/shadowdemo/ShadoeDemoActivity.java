package com.example.myapplication.shadowdemo;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityShadoeDemoBinding;

import org.joda.time.LocalTime;

import java.util.ArrayList;

public class ShadoeDemoActivity extends BaseActivity<ActivityShadoeDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.timebar.setEnableTimeList(new ArrayList<TimeBar.TimeBarInfo>() {{
            add(new TimeBar.TimeBarInfo(new LocalTime(1, 0), new LocalTime(2, 0)));
        }});
    }
}