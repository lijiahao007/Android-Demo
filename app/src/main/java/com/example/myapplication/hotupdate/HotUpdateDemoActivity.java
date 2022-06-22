package com.example.myapplication.hotupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;

public class HotUpdateDemoActivity extends AppCompatActivity {

    private final static String TAG =  "HotUpdateDemoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_update_demo);



        Log.i(TAG, "Activity 的classLoader:" + Activity.class.getClassLoader()); // BootClassLoader
        Log.i(TAG, "AppCompatActivity 的classLoader:" + AppCompatActivity.class.getClassLoader()); //PathClassLoader
        Log.i(TAG, "MyApplication 的classLoader:" + MyApplication.class.getClassLoader()); //PathClassLoader
        Log.i(TAG, "HotUpdateDemoActivity 的classLoader:" + HotUpdateDemoActivity.class.getClassLoader()); //PathClassLoader
    }
}