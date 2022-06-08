package com.example.myapplication.broadcast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class BroadcastDemoActivity1 extends AppCompatActivity {

    public static final String Broadcast = "com.example.myapplication.broadcast";
    public static final String OrderBroadcast = "com.example.myapplication.order_broadcast";
    public static final String LocalBroadcast = "com.example.myapplication.local_broadcast";

    private DemoBroadcastReceiver1 receiver1;
    private EditText text;
    private DemoBroadcastReceiver1 receiver2;
    private OrderBroadcastReceiver2 orderReceiver2;
    private OrderBroadcastReceiver1 orderReceiver1;
    private LocalBroadcastManager localBroadcastManager;
    private LocalBroadcastReceiver1 localReceiver1;
    private TextView info;

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

    }

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


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(orderReceiver1);
        unregisterReceiver(orderReceiver2);
        localBroadcastManager.unregisterReceiver(localReceiver1);
    }
}