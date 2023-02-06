package com.example.myapplication.glidedemo;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


// 下面展示的是，在Glide中是如何监听Activity的生命周期的。
// 原理：通过插入一个没有界面的Fragment来侦听状态。
public class GlideLifecycleObserve implements MyLifecycleListener {

    private ObserveLifecycleFragment fragment;
    private String FRAGMENT_TAG = "FRAGMENT_TAG";
    private final String TAG = "GlideLifecycleObserve";

    public GlideLifecycleObserve(FragmentActivity fragmentActivity) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        fragment = new ObserveLifecycleFragment();
        if (isActivityVisible(fragmentActivity)) {
            fragment.getMyLifecycle().onStart();
        }
        fm.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
        fragment.getMyLifecycle().addListener(this);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "GlideLifecycleObserve onStart");
    }

    @Override
    public void onStop() {
        Log.i(TAG, "GlideLifecycleObserve onStop");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "GlideLifecycleObserve onDestroy");
        fragment.getMyLifecycle().removeListener(this);
        fragment = null;
    }

    private static boolean isActivityVisible(Activity activity) {
        // This is a poor heuristic, but it's about all we have. We'd rather err on the side of visible
        // and start requests than on the side of invisible and ignore valid requests.
        return activity == null || !activity.isFinishing();
    }
}
