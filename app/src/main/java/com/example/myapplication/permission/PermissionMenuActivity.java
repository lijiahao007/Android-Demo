package com.example.myapplication.permission;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.activity.ExampleActivity1;

public class PermissionMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_menu);

        findViewById(R.id.permission_native_request).setOnClickListener(view -> {
            startActivity(new Intent(this, PermissionDemoActivity.class));
        });

        findViewById(R.id.easy_permission_request).setOnClickListener(view -> {
            startActivity(new Intent(this, EasyPermissionActivity.class));
        });
    }
}