package com.example.myapplication.setupnet;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

public class PTZHandlerThread extends HandlerThread {

    private Handler handle;

    public PTZHandlerThread() {
        super("PTZHandlerThread");
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handle = new Handler(getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

            }
        };
    }

    public void move(Direction direction) {
        handle.obtainMessage(direction.ordinal()).sendToTarget();
    }


}
