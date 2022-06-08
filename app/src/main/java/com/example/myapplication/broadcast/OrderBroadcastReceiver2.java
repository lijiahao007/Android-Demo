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

public class OrderBroadcastReceiver2 extends BroadcastReceiver {
    private TextView textView;
    public OrderBroadcastReceiver2(TextView textView) {
        this.textView = textView;
    }

    public OrderBroadcastReceiver2() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getStringExtra("msg");
        String msg = name;
        Bundle resultExtras = getResultExtras(true);
        if (resultExtras.containsKey("msg")) {
            msg = (String) resultExtras.get("msg");
        }
        msg += " receiver2";
        resultExtras.putString("msg", msg);
        setResultExtras(resultExtras);
        String info = textView.getText().toString();
        info += "receiver2 收到有序广播:" + name  +  "  time: " + LocalTime.now() + "\n";
        info += "resultExtras:" + msg + "\n";
        textView.setText(info);

    }
}
