package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class LaunchModeActivity1 extends AppCompatActivity {

    static final String TAG = "LaunchModeActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_mode1);
        Log.i("LaunchMode", TAG + " onCreate()");

        findViewById(R.id.btn_launch2).setOnClickListener(view -> {
            startActivity(new Intent(this, LaunchModeActivity2.class));
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "LaunchModeActivity1 onNewIntent()", Toast.LENGTH_SHORT).show();
        Log.i("LaunchMode", TAG + " onNewIntent()");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LaunchMode", TAG + " onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LaunchMode", TAG + " onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LaunchMode", TAG + " onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LaunchMode", TAG + " onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LaunchMode", TAG + " onDestroy()");
    }
}