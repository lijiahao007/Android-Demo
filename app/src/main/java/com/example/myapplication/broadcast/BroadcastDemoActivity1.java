package com.example.myapplication.broadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;

public class BroadcastDemoActivity1 extends AppCompatActivity {

    public static final String Broadcast = "com.example.myapplication.broadcast";
    public static final String OrderBroadcast = "com.example.myapplication.order_broadcast";

    private DemoBroadcastReceiver1 receiver1;
    private EditText text;
    private DemoBroadcastReceiver1 receiver2;
    private OrderBroadcastReceiver2 orderReceiver2;
    private OrderBroadcastReceiver1 orderReceiver1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_demo1);
        text = findViewById(R.id.et_broadcast_msg);

        findViewById(R.id.btn_send_broadcast).setOnClickListener(view -> {
            String msg = text.getText().toString();
            Intent broadcastIntent = new Intent(Broadcast);
            broadcastIntent.putExtra("msg", msg);
            sendBroadcast(broadcastIntent);
        });

        findViewById(R.id.btn_send_ordered_broadcast).setOnClickListener(view -> {
            String msg = text.getText().toString();
            Intent broadcastIntent = new Intent(OrderBroadcast);
            broadcastIntent.putExtra("msg", msg);
            sendOrderedBroadcast(broadcastIntent, null);
            Toast.makeText(this, "broadcast:" + msg, Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(Broadcast);
        receiver1 = new DemoBroadcastReceiver1();
        registerReceiver(receiver1, filter);

        receiver2 = new DemoBroadcastReceiver1();
        registerReceiver(receiver2, filter);

        orderReceiver1 = new OrderBroadcastReceiver1();
        orderReceiver2 = new OrderBroadcastReceiver2();
        IntentFilter orderFilter1 = new IntentFilter(OrderBroadcast);
        IntentFilter orderFilter2 = new IntentFilter(OrderBroadcast);
        orderFilter1.setPriority(400);
        orderFilter2.setPriority(300);
        registerReceiver(orderReceiver1, orderFilter1);
        registerReceiver(orderReceiver2, orderFilter2);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(orderReceiver1);
        unregisterReceiver(orderReceiver2);
    }
}