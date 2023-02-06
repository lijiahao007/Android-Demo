package com.example.myapplication.glidedemo;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityGlideDemoBinding;

public class GlideDemoActivity extends BaseActivity<ActivityGlideDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Glide.with(this)
                .load("https://imgcps.jd.com/ling4/100012043978/5Lqs6YCJ5aW96LSn/5L2g5YC85b6X5oul5pyJ/p-5f3a47329785549f6bc7a6f9/dfbbc83a/cr/s/q.jpg")
                .into(binding.ivSrc);


        GlideLifecycleObserve glideLifecycleObserve = new GlideLifecycleObserve(this);

    }
}