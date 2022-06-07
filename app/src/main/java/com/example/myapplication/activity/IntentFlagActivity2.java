package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;

public class IntentFlagActivity2 extends AppCompatActivity {

    static final String TAG = "IntentFlagActivity2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_flag2);

        findViewById(R.id.btn_launch_flag1_standard).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity1.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_launch_flag2_clear_top).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "IntentFlat2Activity onNewIntent", Toast.LENGTH_SHORT).show();
        Log.i("IntentFlagActivity2", "onNewIntent");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

    }
}