package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

public class IntentFlagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_flag);

        findViewById(R.id.btn_single_top).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity1.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_clear_top).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity2.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_clear_task).setOnClickListener(view -> {
            Intent intent = new Intent(this, IntentFlagActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }
}