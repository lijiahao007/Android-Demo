package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.activity.ExampleActivity1;
import com.example.myapplication.broadcast.BroadcastDemoActivity1;
import com.example.myapplication.contentprovider.ContentProviderDemoActivity;
import com.example.myapplication.intent.IntentActivity;
import com.example.myapplication.permission.PermissionDemoActivity;
import com.example.myapplication.recycleview.RecyclerViewDemoActivity;
import com.example.myapplication.service.ServiceDemoActivity;
import com.example.myapplication.tablayout.TabLayoutDemoActivity;

public class ActivityDemo1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        TextView intentFilterDemo = findViewById(R.id.intent_filter_demo);
        intentFilterDemo.setOnClickListener(view -> {
            startActivity(new Intent(this, IntentActivity.class));
        });

        findViewById(R.id.activity_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, ExampleActivity1.class));
        });

        findViewById(R.id.service_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, ServiceDemoActivity.class));
        });

        findViewById(R.id.broadcast_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, BroadcastDemoActivity1.class));
        });

        findViewById(R.id.content_provider_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, ContentProviderDemoActivity.class));
        });

        findViewById(R.id.recycler_view_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, RecyclerViewDemoActivity.class));
        });

        findViewById(R.id.tablayout_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, TabLayoutDemoActivity.class));
        });

        findViewById(R.id.permission_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, PermissionDemoActivity.class));
        });
    }
}