package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

public class LaunchModeActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode2);
        findViewById(R.id.btn_launch2).setOnClickListener(view -> {
            startActivity(new Intent(this, LaunchModeActivity1.class));
        });
    }
}