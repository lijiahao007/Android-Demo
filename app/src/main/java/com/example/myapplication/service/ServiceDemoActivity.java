package com.example.myapplication.service;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.IntentFlagActivity;
import com.example.myapplication.activity.Teacher;

public class ServiceDemoActivity extends AppCompatActivity {

    static final String TAG = "ServiceDemoActivity";
    private DemoService1 demoService1 = null;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 这里的IBinder 就是Service中onBind()返回的IBinder。可以通过它获取Service实例。
            demoService1 = ((DemoService1.ServiceBinder) service).getDemoService1();
            Log.i(TAG, name + "  demoService1 实例获取，服务链接成功" + demoService1);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, name + "demoService1 链接取消");
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_demo);

        // 1. startService
        findViewById(R.id.btn_start_service).setOnClickListener(view -> {
            Intent intent = new Intent(this, DemoService1.class);
            startService(intent);
        });

        // 2. bindService
        findViewById(R.id.btn_bind_service).setOnClickListener(view -> {
            Intent intent = new Intent(this, DemoService1.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        });

        // 3. stopService
        findViewById(R.id.btn_stop_service).setOnClickListener(view -> {
            stopService(new Intent(this, DemoService1.class));
        });

        // 4. unbindService
        findViewById(R.id.btn_unbind_service).setOnClickListener(view -> {
            unbindService(connection);
        });

        // 5. IntentService
        findViewById(R.id.btn_intent_service).setOnClickListener(view -> {
            Intent intent = new Intent(this, DemoIntentService1.class);
            startService(intent);
            Toast.makeText(this , "intentService", Toast.LENGTH_SHORT).show();
        });


    }
}