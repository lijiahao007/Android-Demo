package com.example.myapplication.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.myapplication.R;
import com.example.myapplication.activity.IntentFlagActivity;
import com.example.myapplication.activity.Teacher;

public class ServiceDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_demo);

        // 1. startService
        findViewById(R.id.btn_start_service).setOnClickListener(view -> {
            Intent intent = new Intent(this, DemoService1.class);
            intent.putExtra("teacher", new Teacher("hello", 10, 170.0));
            startService(intent);
        });

        // 2. bindService
        findViewById(R.id.btn_start_service).setOnClickListener(view -> {
            Intent intent = new Intent(this, DemoService1.class);
            intent.putExtra("teacher", new Teacher("hello", 10, 170.0));

            ServiceConnection connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    // 这里的IBinder 就是Service中onBind()返回的IBinder。
                    
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            bindService(intent, conn);


        });

    }
}