package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class LocalBroadcastReceiver1 extends BroadcastReceiver {

    private TextView textView;
    public LocalBroadcastReceiver1(TextView textView) {
        this.textView = textView;
    }

    public LocalBroadcastReceiver1() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("msg");

        String info = textView.getText().toString();
        info += "收到本地广播:" + name  +  "  time: " + LocalTime.now() + "\n";
        textView.setText(info);
    }
}
