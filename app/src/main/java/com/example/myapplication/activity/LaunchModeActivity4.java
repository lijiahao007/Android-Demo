package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;

public class LaunchModeActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode4);
        int prevTaskId = getIntent().getIntExtra("prevTaskId", -10086);
        Toast.makeText(this, "\nprevTaskId:" + prevTaskId +
                "\ntaskId:" + getTaskId(), Toast.LENGTH_SHORT).show();
    }
}