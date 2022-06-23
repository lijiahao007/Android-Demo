package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.activity.ExampleActivity1;
import com.example.myapplication.album.AlbumActivity;
import com.example.myapplication.album.PhotoVideoDemoActivity;
import com.example.myapplication.broadcast.BroadcastDemoActivity1;
import com.example.myapplication.contentprovider.ContentProviderDemoActivity;
import com.example.myapplication.customview.CustomViewMenuActivity;
import com.example.myapplication.eventbus.EventBusDemoActivity;
import com.example.myapplication.hotupdate.HotUpdateDemoActivity;
import com.example.myapplication.intent.IntentActivity;
import com.example.myapplication.media.MediaDemoActivity;
import com.example.myapplication.multiThread.MultipleThreadDemoActivity;
import com.example.myapplication.notification.NotificationDemoActivity;
import com.example.myapplication.okhttp.OkHttpDemoActivity;
import com.example.myapplication.permission.PermissionMenuActivity;
import com.example.myapplication.qrcode.QRCodeMenuActivity;
import com.example.myapplication.recycleview.RecyclerViewDemoActivity;
import com.example.myapplication.rxjava.RxJavaDemoActivity;
import com.example.myapplication.service.ServiceDemoActivity;
import com.example.myapplication.tablayout.TabLayoutDemoActivity;
import com.example.myapplication.wifi.WifiDemoActivity;

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
            startActivity(new Intent(this, PermissionMenuActivity.class));
        });

        findViewById(R.id.custom_view_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, CustomViewMenuActivity.class));
        });

        findViewById(R.id.album_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, AlbumActivity.class));
        });

        findViewById(R.id.camera_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, PhotoVideoDemoActivity.class));
        });

        findViewById(R.id.media_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, MediaDemoActivity.class));
        });

        findViewById(R.id.qrcode_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, QRCodeMenuActivity.class));
        });

        findViewById(R.id.wifi_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, WifiDemoActivity.class));
        });

        findViewById(R.id.http_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, OkHttpDemoActivity.class));
        });

        findViewById(R.id.hot_update_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, HotUpdateDemoActivity.class));
        });

        findViewById(R.id.multiple_thread_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, MultipleThreadDemoActivity.class));
        });

        findViewById(R.id.event_bus_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, EventBusDemoActivity.class));
        });

        findViewById(R.id.rxjava_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, RxJavaDemoActivity.class));
        });

        findViewById(R.id.notification_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, NotificationDemoActivity.class));
        });
    }
}