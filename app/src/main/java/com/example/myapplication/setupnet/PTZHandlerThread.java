package com.example.myapplication.setupnet;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.macrovideo.sdk.media.HSMediaPlayer;

public class PTZHandlerThread extends HandlerThread {

    private Handler handle;
    private HSMediaPlayer mHSMediaPlayer = null;

    public PTZHandlerThread(HSMediaPlayer mHSMediaPlayer) {
        super("PTZHandlerThread");
        this.mHSMediaPlayer = mHSMediaPlayer;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handle = new Handler(getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Direction direction = Direction.values()[msg.what];
                if (mHSMediaPlayer == null) {
                    return;
                }
                int step = msg.arg1;
                switch (direction) {
                    case UP:
                        mHSMediaPlayer.setPTZAction(false, false, true, false, step);
                        break;
                    case DOWN:
                        mHSMediaPlayer.setPTZAction(false, false, false, true, step);
                        break;
                    case LEFT:
                        mHSMediaPlayer.setPTZAction(true, false, false, false, step);
                        break;
                    case RIGHT:
                        mHSMediaPlayer.setPTZAction(false, true, false, false, step);
                        break;
                }
            }
        };
    }

    public void move(Direction direction, int step) {
        Message message = handle.obtainMessage(direction.ordinal());
        message.arg1 = step;
        message.sendToTarget();
    }

    public void move(Direction direction) {
        move(direction, 1);
    }
}
