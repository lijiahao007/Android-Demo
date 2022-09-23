package com.example.myapplication.customview;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityTabLayoutDemo2Binding;
import com.google.android.material.tabs.TabLayout;

import java.util.concurrent.atomic.AtomicInteger;

public class TabLayoutDemoActivity extends BaseActivity<ActivityTabLayoutDemo2Binding> {
    private String TAG = "TabLayoutDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(TabLayoutDemoActivity.this, tab.getText(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onTabSelected: " + tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabUnselected: " + tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i(TAG, "onTabReselected: " + tab.getText());
            }
        });

        binding.tabLayout.addTab(binding.tabLayout.newTab()
                .setText("tag1"));

        binding.tabLayout.addTab(binding.tabLayout.newTab()
                .setText("tag2"));

        binding.tabLayout.addTab(binding.tabLayout.newTab()
                .setText("tag3"));

        binding.tabLayout.getTabAt(1).select();

        AtomicInteger count = new AtomicInteger();
        binding.customView.setListener(view -> {
            Log.i("CustomView2", "click");
            showToast("count=" + count.incrementAndGet());
        });
    }


}