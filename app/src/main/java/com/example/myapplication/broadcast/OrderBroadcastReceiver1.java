package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class OrderBroadcastReceiver1 extends BroadcastReceiver {
    private TextView textView;

    public OrderBroadcastReceiver1(TextView textView) {
        this.textView = textView;
    }

    public OrderBroadcastReceiver1() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("msg");
        Bundle resultExtras = getResultExtras(true);
        String msg = name;
        if (resultExtras.containsKey("msg")) {
            msg = (String) resultExtras.get("msg");
        }

        msg += " receiver1";
        resultExtras.putString("msg", msg);
        setResultExtras(resultExtras);

        String info = textView.getText().toString();
        info += "receiver1 收到有序广播:" + name  +  "  time: " + LocalTime.now() + "\n";
        info += "resultExtras:" + msg + "\n";
        textView.setText(info);
    }
}
