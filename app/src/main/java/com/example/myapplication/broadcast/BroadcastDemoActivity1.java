package com.example.myapplication.broadcast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.time.LocalTime;

public class BroadcastDemoActivity1 extends AppCompatActivity {

    public static final String Broadcast = "com.example.myapplication.broadcast";
    public static final String OrderBroadcast = "com.example.myapplication.order_broadcast";
    public static final String LocalBroadcast = "com.example.myapplication.local_broadcast";
    public static final String StickBroadcast = "com.example.myapplication.stick_broadcast";


    private DemoBroadcastReceiver1 receiver1;
    private EditText text;
    private DemoBroadcastReceiver1 receiver2;
    private OrderBroadcastReceiver2 orderReceiver2;
    private OrderBroadcastReceiver1 orderReceiver1;
    private LocalBroadcastManager localBroadcastManager;
    private LocalBroadcastReceiver1 localReceiver1;
    private TextView info;
    private DemoStickBroadcastReceiver1 demoBroadcastReceiver1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_demo1);
        text = findViewById(R.id.et_broadcast_msg);
        info = findViewById(R.id.tv_info);

        findViewById(R.id.btn_send_broadcast).setOnClickListener(view -> {
            String msg = text.getText().toString();
            Intent broadcastIntent = new Intent(Broadcast);
            broadcastIntent.putExtra("msg", msg);
            sendBroadcast(broadcastIntent);
            Toast.makeText(this, "发送无序广播:" + msg, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_send_ordered_broadcast).setOnClickListener(view -> {
            String msg = text.getText().toString();
            Intent broadcastIntent = new Intent(OrderBroadcast);
            broadcastIntent.putExtra("msg", msg);
            sendOrderedBroadcast(broadcastIntent, null);
            Toast.makeText(this, "发送有序广播:" + msg, Toast.LENGTH_SHORT).show();
        });

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        findViewById(R.id.btn_send_local_broadcast).setOnClickListener(view -> {
            String msg = text.getText().toString();
            Intent broadcastIntent = new Intent(LocalBroadcast);
            broadcastIntent.putExtra("msg", msg);
            localBroadcastManager.sendBroadcast(broadcastIntent);
            Toast.makeText(this, "发送本地广播:" + msg, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_send_sticky_broadcast).setOnClickListener(view -> {
            String msg = text.getText().toString();
            Intent broadcastIntent = new Intent(StickBroadcast);
            broadcastIntent.putExtra("msg", msg);
            sendStickyBroadcast(broadcastIntent); // 因为安全问题，在Android5被弃用
            Toast.makeText(this, "发送粘性广播:" + msg, Toast.LENGTH_SHORT).show();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        // 1. 注册无序广播
        IntentFilter filter = new IntentFilter(Broadcast);
        receiver1 = new DemoBroadcastReceiver1(info);
        registerReceiver(receiver1, filter);

        receiver2 = new DemoBroadcastReceiver1(info);
        registerReceiver(receiver2, filter);

        // 2. 注册有序广播
        orderReceiver1 = new OrderBroadcastReceiver1(info);
        orderReceiver2 = new OrderBroadcastReceiver2(info);
        IntentFilter orderFilter1 = new IntentFilter(OrderBroadcast);
        IntentFilter orderFilter2 = new IntentFilter(OrderBroadcast);
        orderFilter1.setPriority(400);
        orderFilter2.setPriority(300);
        registerReceiver(orderReceiver1, orderFilter1);
        registerReceiver(orderReceiver2, orderFilter2);

        // 3. 注册本地广播
        localReceiver1 = new LocalBroadcastReceiver1(info);
        IntentFilter localFilter1 = new IntentFilter(LocalBroadcast);
        localBroadcastManager.registerReceiver(localReceiver1, localFilter1);

        // 4. 注册粘性广播
        IntentFilter stickFilter = new IntentFilter(StickBroadcast);
        demoBroadcastReceiver1 = new DemoStickBroadcastReceiver1(info);
        registerReceiver(demoBroadcastReceiver1, stickFilter);
        // 4.1 粘性广播在发送后会自动存储，可以直接使用registerReceiver获取存储的数据
        getWindow().getDecorView().postDelayed(()-> {
            Intent intent = registerReceiver(null, stickFilter);
            String msg = intent.getStringExtra("msg");
            String text = info.getText().toString();
            text += "从粘性广播存储在本地的地方获取值:" + msg  +  "  time: " + LocalTime.now() + "\n";
            info.setText(text);
        }, 5000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(orderReceiver1);
        unregisterReceiver(orderReceiver2);
        localBroadcastManager.unregisterReceiver(localReceiver1);
        unregisterReceiver(demoBroadcastReceiver1);
    }
}