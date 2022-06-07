package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;

public class LaunchModeActivity3 extends AppCompatActivity {
    static final String TAG = "LaunchModeActivity3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode3);

        findViewById(R.id.btn_launch).setOnClickListener(view -> {
            startActivity(new Intent(this, LaunchModeActivity3.class));
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "LaunchModeActivity3 onNewIntent()", Toast.LENGTH_SHORT).show();
        Log.i("LaunchMode", TAG + " onNewIntent()");
    }


}