package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    static final String TAG = "MyApplication";
    public ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

}
