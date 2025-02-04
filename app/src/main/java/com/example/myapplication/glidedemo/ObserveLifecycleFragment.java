package com.example.myapplication.glidedemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@SuppressLint("LongLogTag")
public class ObserveLifecycleFragment extends Fragment {
    private ActivityLifecycle lifecycle;
    private final static String TAG = "ObserveLifecycleFragment";
    public ObserveLifecycleFragment() {
        this.lifecycle = new ActivityLifecycle();
    }

    public ActivityLifecycle getMyLifecycle() {
        return lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        lifecycle.onDestroy();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView()");
    }

}
