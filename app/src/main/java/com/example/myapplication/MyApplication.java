package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;

public class MyApplication extends Application {

    static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }



}
