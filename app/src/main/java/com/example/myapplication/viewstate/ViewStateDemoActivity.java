package com.example.myapplication.viewstate;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityViewStateDemoBinding;

public class ViewStateDemoActivity extends BaseActivity<ActivityViewStateDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.clLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean activated = binding.clLayout.isActivated();
                binding.clLayout.setActivated(!activated);
            }
        });

    }
}