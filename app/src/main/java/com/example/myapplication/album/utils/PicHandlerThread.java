package com.example.myapplication.album.utils;

import android.os.HandlerThread;

public class PicHandlerThread extends HandlerThread {
    public PicHandlerThread(String name) {
        super(name);
    }

    public PicHandlerThread(String name, int priority) {
        super(name, priority);
    }

    public void action(){}
}
