package com.example.myapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DemoIntentService1 extends IntentService {

    public DemoIntentService1() {
        super("DemoIntentService1");
        Log.i("DemoIntentService1", "DemoIntentService1() create ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(1000);
            Log.i("DemoIntentService1", "工作线程是: " + Thread.currentThread().getName());
            Log.i("DemoIntentService1", "sleep 1s");
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

}