package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;

public class ExampleActivity2 extends AppCompatActivity {

    static final String TAG = "ExampleActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example2);
        Log.i("Lifecycle", TAG + " onCreate()");

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Lifecycle", TAG + " onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle", TAG + " onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Lifecycle", TAG + " onPause()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lifecycle", TAG + " onStop()");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Lifecycle", TAG + " onDestroy()");
    }
}