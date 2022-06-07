package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class IntentFlagActivity1 extends AppCompatActivity {

    static final String TAG = "IntentFlagActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_flag1);
        Log.i("IntentFlag", "IntentFlagActivity1 onCreate()");
        findViewById(R.id.btn_launch_self).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btn_launch_flag2).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btn_launch_self1).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity1.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "IntentFlagActivity1 onNewIntent", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}