package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.example.myapplication.activity.Teacher;

public class DemoService1 extends Service {

    static final String TAG = "DemoService1";
    private final IBinder binder = new ServiceBinder();

    public DemoService1() {
        Log.i(TAG, "构造函数()" + this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand" + this);
        Teacher teacher = (Teacher) intent.getParcelableExtra("teacher");
        Log.i(TAG, "params: " + teacher);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onStartCommand" + this);
        super.onDestroy();
    }

    class  ServiceBinder extends Binder {

    }
}