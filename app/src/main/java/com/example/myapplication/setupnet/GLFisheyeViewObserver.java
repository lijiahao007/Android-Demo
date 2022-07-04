package com.example.myapplication.setupnet;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.hyfisheyepano.GLFisheyeView;

public class GLFisheyeViewObserver implements DefaultLifecycleObserver {
    private GLFisheyeView glFisheyeView;

    public GLFisheyeViewObserver(GLFisheyeView glFisheyeView) {
        this.glFisheyeView = glFisheyeView;
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onResume(owner);
        if (glFisheyeView != null) {
            glFisheyeView.onResume();
            Log.i("GLFisheyeViewObserver", "glFisheyeView onResume");
        }
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onPause(owner);
        if(glFisheyeView != null) {
            glFisheyeView.onPause();
            Log.i("GLFisheyeViewObserver", "glFisheyeView onPause");
        }
    }
}
