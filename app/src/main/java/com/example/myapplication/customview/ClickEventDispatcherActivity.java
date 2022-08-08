package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.R;

public class ClickEventDispatcherActivity extends AppCompatActivity {

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_event_dispatcher);

        View parent = findViewById(R.id.rl_parent);
        View child = findViewById(R.id.view_child);
        parent.setOnClickListener(view -> {
            Log.i("ClickEventDispatcherActivity", "parent click" );
        });

        child.setOnClickListener(view -> {
            Log.i("ClickEventDispatcherActivity", "child click" );

        });

    }
}