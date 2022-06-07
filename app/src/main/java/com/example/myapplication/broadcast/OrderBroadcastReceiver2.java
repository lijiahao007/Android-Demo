package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class OrderBroadcastReceiver2 extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("msg");
        String msg = name;
        Bundle resultExtras = getResultExtras(true);
        if (resultExtras.containsKey("msg")) {
            msg = (String) resultExtras.get("msg");
        }
        msg += " fuck";
        resultExtras.putString("msg", msg);
        setResultExtras(resultExtras);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "receive2:" + msg, Toast.LENGTH_SHORT).show();
        Log.i("DemoBroadcastReceiver1", "收到广播:" + msg + " receiver2" + " time:" + LocalTime.now());
    }
}
