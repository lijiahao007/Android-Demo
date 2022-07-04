package com.example.myapplication.setupnet;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

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
import com.macrovideo.sdk.objects.DeviceStatus;
import com.macrovideo.sdk.objects.LoginParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PreviewActivity extends AppCompatActivity {

    private DeviceInfo deviceInfo = null;
    private LoginHandle loginHandle = null;
    private final static String TAG = "PreviewActivity";
    private HSMediaPlayer mHSMediaPlayer = null;
    private LinearLayout llContainer;
    private final int loginSuccessWhat = 0;
    private int screenHeight;
    private int screenWidth;
    private TextView tvInfo;
    private Button btnFullScreen;
    private ImageView ivBack;
    private Button btnSpeak;
    private Button btnUp;
    private Button btnDown;
    private Button btnLeft;
    private Button btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // 这种方法只适用于Activity，不适用于AppCompatActivity
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_preview);

        deviceInfo = SetupNetDemoActivity.getDeviceInfo();
        llContainer = findViewById(R.id.llContainer);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // 1. 登录设备
        LoginParam loginParam = new LoginParam(deviceInfo, Defines.LOGIN_FOR_PLAY);
        LoginHelper.loginDevice(this, loginParam, new ILoginDeviceCallback() {
            @Override
            public void onLogin(LoginHandle loginHandle) {
                if (loginHandle != null) {
                    if (loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                        PreviewActivity.this.loginHandle = loginHandle;
                        Log.i(TAG, "login success");
                        handler.obtainMessage(loginSuccessWhat).sendToTarget();
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
        // 1. 初始化mHSMediaPlayer
        if (mHSMediaPlayer == null) {
            mHSMediaPlayer = new HSMediaPlayer(this, false);
        }
        mHSMediaPlayer.setHWDecodeStatus(false, false);
        // 1.1 获取视频渲染试图 GLFisheyeView
        GLFisheyeView glFisheyeView = new GLFisheyeView(this);
        glFisheyeView.setMode(HSMediaPlayer.PANO_PLAY_MODE_13);
        //
        glFisheyeView.setScale(true);
        // 1.2 GLFisheyeView 的长宽，比例为16:9
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(screenWidth, screenWidth * 9 / 16);
        glFisheyeView.setLayoutParams(layoutParams);
        mHSMediaPlayer.setGlFishView(glFisheyeView);
        // 1.3 设置设备时间显示回调
        mHSMediaPlayer.setTvTimeOSD(new ITimeTextCallback() {
            @Override
            public void setTimeText(String s) {
//                Log.i(TAG, "mHSMediaPlayer.setTvTimeOSD: setTimeText:" + s);
            }
        });

        // 1.4 将glFisheyeView添加到界面布局中
        llContainer.addView(mHSMediaPlayer.getGLFisheyeView());
        // 1.5 glFisheyeView添加生命周期监听
        getLifecycle().addObserver(new GLFisheyeViewObserver(glFisheyeView));

        // 1.6 开启播放
        startPlay();

        // 1.7 获取设备状态
        tvInfo = findViewById(R.id.tvOnline);
        boolean isbInLan = loginHandle.isbInLan();
        DeviceStatus deviceStatus = LoginHelper.getDeviceStatus(deviceInfo);
        int onlineStat = deviceStatus.getnOnlineStat();
        int onlineState = deviceInfo.getnOnLineStat();
        Log.i(TAG, "isbInLan:" + isbInLan + " deviceStatus.onlineStat:" + onlineStat + " deviceInfo.onlineState:" + onlineState);
        tvInfo.setText(isbInLan ? "局域网在线" : "互联网在线");

        // 1.8 全屏设置，退出全屏设置
        btnFullScreen = findViewById(R.id.btnFullScreen);
        btnFullScreen.setOnClickListener(view -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            btnFullScreen.setText("退出全屏");
        });
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.GONE);
        ivBack.setOnClickListener(view -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            btnFullScreen.setText("全屏");
        });

        // 1.9 设置语音对讲
        btnSpeak = findViewById(R.id.btnSpeak);
        btnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHSMediaPlayer.startSpeak();
                        break;
                    case MotionEvent.ACTION_UP:
                        mHSMediaPlayer.stopSpeak();
                        break;
                }
                return true;
            }
        });

        // 1.10 设置云台操作
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        HashMap<Button, ArrayList<Boolean>> mPTZActionMap = new HashMap<Button, ArrayList<Boolean>>(4) {{ // array是 左右上下
            put(btnLeft, new ArrayList<Boolean>() {{
                add(true);
                add(false);
                add(false);
                add(false);
            }});
            put(btnRight, new ArrayList<Boolean>() {{
                add(false);
                add(true);
                add(false);
                add(false);
            }});
            put(btnUp, new ArrayList<Boolean>() {{
                add(false);
                add(false);
                add(true);
                add(false);
            }});
            put(btnDown, new ArrayList<Boolean>() {{
                add(false);
                add(false);
                add(false);
                add(true);
            }});
        }};
        ArrayList<Button> directionButtons = new ArrayList<Button>(4) {{
            add(btnLeft);
            add(btnRight);
            add(btnUp);
            add(btnDown);
        }};


        for (Button button: directionButtons) {
            button.setOnClickListener(view ->{
                ArrayList<Boolean> arrayList = mPTZActionMap.get((Button) view);
                if (arrayList != null) {
                    mHSMediaPlayer.setPTZAction(arrayList.get(0), arrayList.get(1), arrayList.get(2), arrayList.get(3), 1);
                }
            });
        }

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;// 屏幕宽
        screenHeight = dm.heightPixels;// 屏幕高

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 全屏
            showLandscape();
        } else {
            // 竖屏
            showPortrait();
        }
    }

    private void showLandscape() {
        btnFullScreen.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);

        if (mHSMediaPlayer != null && mHSMediaPlayer.getGLFisheyeView() != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight);
            mHSMediaPlayer.getGLFisheyeView().setLayoutParams(layoutParams);
            mHSMediaPlayer.onOreintationChange(Configuration.ORIENTATION_LANDSCAPE);
        }

        // 开启沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }

    private void showPortrait() {
        btnFullScreen.setVisibility(View.VISIBLE);
        llContainer.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.GONE);

        if (mHSMediaPlayer != null && mHSMediaPlayer.getGLFisheyeView() != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenWidth * 9 / 16);
            mHSMediaPlayer.getGLFisheyeView().setLayoutParams(layoutParams);
            mHSMediaPlayer.onOreintationChange(Configuration.ORIENTATION_PORTRAIT);
        }

        // 关闭沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    private void startPlay() {
        if (mHSMediaPlayer != null) {
            mHSMediaPlayer.startPlay(
                    0, // 播放窗口，默认0,（不支持多窗口）
                    0, // 播放通道 (默认为0)
                    0, // 视频清晰度
                    true, // 是否开启视频
                    loginHandle // 登录句柄
            );
            mHSMediaPlayer.getGLFisheyeView().onResume();
            Log.i(TAG, "startPlay");
        }
    }

    private void stopPlay() {
        if (mHSMediaPlayer != null) {
            mHSMediaPlayer.stopPlay();
            mHSMediaPlayer.getGLFisheyeView().onPause();
            Log.i(TAG, "stopPlay");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay();
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case loginSuccessWhat:
                    initMediaPlayer();
                    break;
            }
        }
    };
}