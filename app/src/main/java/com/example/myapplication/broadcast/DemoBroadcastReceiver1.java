package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class DemoBroadcastReceiver1 extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("msg");
        Toast.makeText(context, "收到广播:" + name, Toast.LENGTH_SHORT).show();
        Log.i("DemoBroadcastReceiver1", "收到广播:" + name + " receiver:" + this + " time:" + LocalTime.now());

    }
}
