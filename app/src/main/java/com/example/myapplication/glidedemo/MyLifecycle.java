package com.example.myapplication.glidedemo;

public interface MyLifecycle {
    void addListener(MyLifecycleListener lifecycleListener);
    void removeListener(MyLifecycleListener lifecycleListener);
}
