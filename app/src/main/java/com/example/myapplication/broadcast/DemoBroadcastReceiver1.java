package com.example.myapplication.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class DemoBroadcastReceiver1 extends BroadcastReceiver {

    private TextView textView;
    public DemoBroadcastReceiver1(TextView textView) {
        this.textView = textView;
    }

    public DemoBroadcastReceiver1() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("msg");
        Log.i("DemoBroadcastReceiver1", "收到无序广播:" + name + " receiver:" + this + "   time:" + LocalTime.now());
        String info = textView.getText().toString();
        info += "收到无序广播:" + name  +  "  time: " + LocalTime.now() + "\n";
        textView.setText(info);

    }
}
