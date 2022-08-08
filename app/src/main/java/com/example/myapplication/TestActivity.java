package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends AppCompatActivity {

    private final static String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SharedPreferences sp = getSharedPreferences("hhha", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("key1", 1);
        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("hhha", Context.MODE_PRIVATE);
        int key1 = sharedPreferences.getInt("key1", -1);
        Log.i(TAG, "key1:" + key1);

    }
}