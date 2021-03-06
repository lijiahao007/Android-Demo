package com.example.myapplication;

import android.app.Application;
import android.util.Log;

import com.macrovideo.sdk.SDKHelper;
import com.tencent.mmkv.MMKV;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {

    static final String TAG = "MyApplication";
    public ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        SDKHelper.initPhoneType(10);
        MMKV.initialize(this);
    }

}
