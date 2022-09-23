package com.example.myapplication.tablayout;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class LifecycleLogObserver implements DefaultLifecycleObserver {

    String tag;

    public LifecycleLogObserver(String tag) {
        this.tag = tag;
    }

    public LifecycleLogObserver(Object obj) {
        this.tag = obj.getClass().getSimpleName();
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.i(tag, "onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.i(tag, "onStart");
        DefaultLifecycleObserver.super.onStart(owner);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.i(tag, "onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.i(tag, "onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.i(tag, "onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.i(tag, "onDestroy");
    }
}
