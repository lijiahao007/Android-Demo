package com.example.myapplication.setupnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.hyfisheyepano.GLFisheyeView;
import com.example.myapplication.R;
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.defines.ResultCode;
import com.macrovideo.sdk.media.HSMediaPlayer;
import com.macrovideo.sdk.media.ILoginDeviceCallback;
import com.macrovideo.sdk.media.ITimeTextCallback;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.media.LoginHelper;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.objects.LoginParam;

public class PreviewActivity extends AppCompatActivity {

    private DeviceInfo deviceInfo = null;
    private LoginHandle loginHandle = null;
    private final static String TAG = "PreviewActivity";
    private HSMediaPlayer mHSMediaPlayer = null;
    private Button btnStartPlay;
    private Button btnStopPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        deviceInfo = SetupNetDemoActivity.getDeviceInfo();

        btnStartPlay = findViewById(R.id.btnStartPlay);
        btnStopPlay = findViewById(R.id.btnStopPlay);


        // 1. 登录设备
        LoginParam loginParam = new LoginParam(deviceInfo, Defines.LOGIN_FOR_PLAY);
        LoginHelper.loginDevice(this, loginParam, new ILoginDeviceCallback() {
            @Override
            public void onLogin(LoginHandle loginHandle) {
                if (loginHandle != null) {
                    if (loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                        PreviewActivity.this.loginHandle = loginHandle;
                        initMediaPlayer();
                    } else {
                        Log.i(TAG, "登录失败：" + loginHandle.getnResult());
                    }
                } else {
                    Log.i(TAG, "登录失败：loginHandle == null");
                }
            }
        });
    }

    private void initMediaPlayer() {
        if (mHSMediaPlayer == null) {
            mHSMediaPlayer = new HSMediaPlayer(this, false);
        }

        mHSMediaPlayer.setHWDecodeStatus(false, false);
        GLFisheyeView glFisheyeView = new GLFisheyeView(this);
        glFisheyeView.setMode(HSMediaPlayer.PANO_PLAY_MODE_13);
        mHSMediaPlayer.setGlFishView(glFisheyeView);
        mHSMediaPlayer.setTvTimeOSD(new ITimeTextCallback() {
            @Override
            public void setTimeText(String s) {
                Log.i(TAG, "mHSMediaPlayer.setTvTimeOSD: setTimeText:" + s);
            }
        });
        int channelNum = loginHandle.getChannelNum(); // 播放通道

        btnStartPlay.setOnClickListener(view -> {
            mHSMediaPlayer.startPlay(
                    0, // 播放窗口，默认0,（不支持多窗口）
                    channelNum, // 播放通道 (默认为0)
                    0, // 视频清晰度
                    true, // 是否开启视频
                    loginHandle // 登录句柄
            );
        });

        btnStopPlay.setOnClickListener(view -> {
            mHSMediaPlayer.stopPlay();
        });

    }


    Handler handler = new Handler(getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    };
}