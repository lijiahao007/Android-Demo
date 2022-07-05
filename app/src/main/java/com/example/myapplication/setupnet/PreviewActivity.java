package com.example.myapplication.setupnet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyfisheyepano.GLFisheyeView;
import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;
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
import com.macrovideo.sdk.setting.AccountConfigInfo;
import com.macrovideo.sdk.setting.DateTimeConfigInfo;
import com.macrovideo.sdk.setting.DeviceAccountSetting;
import com.macrovideo.sdk.setting.DeviceDateTimeSetting;
import com.macrovideo.sdk.setting.DeviceNetworkSetting;
import com.macrovideo.sdk.setting.DeviceRecordSetting;
import com.macrovideo.sdk.setting.DeviceVersionSetting;
import com.macrovideo.sdk.setting.IPConfigInfo;
import com.macrovideo.sdk.setting.NetworkConfigInfo;
import com.macrovideo.sdk.setting.RecordConfigInfo;
import com.macrovideo.sdk.setting.VersionConfigInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    private PTZHandlerThread ptzHandlerThread;
    private Button btnScreenShot;
    private Button btnStartVideoRecord; // 开始录像按钮
    private Button btnStopVideoRecord; // 结束录像按钮
    private volatile boolean isScreenshotting = false; // 是否上一次截图还没有完成
    private volatile boolean isVideoRecording = false; // 是否正在录像
    private TextView tvRecordTime; // 录像时间显示
    private Timer timer = null; // 录像计时器
    private File tempFile = null; // 录像文件
    private Button btnReverse;
    private CheckBox cbSound;
    private Button btnQuality;
    private int videoQuality = 0; // 视频质量 0:标清 1:高清
    private TextView tvVideoQuality;
    private LogView logView;

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
        if (loginHandle.isbPTZ()) { // 支持云台
            Log.i(TAG, "支持云台");
            btnLeft = findViewById(R.id.btnLeft);
            btnRight = findViewById(R.id.btnRight);
            btnUp = findViewById(R.id.btnUp);
            btnDown = findViewById(R.id.btnDown);
            btnLeft.setTag(Direction.LEFT);
            btnRight.setTag(Direction.RIGHT);
            btnUp.setTag(Direction.UP);
            btnDown.setTag(Direction.DOWN);

            Button[] buttons = new Button[]{btnLeft, btnRight, btnUp, btnDown};
            ptzHandlerThread = new PTZHandlerThread(mHSMediaPlayer);
            ptzHandlerThread.start();
            for (Button button : buttons) {
                button.setOnClickListener(view -> {
                    ptzHandlerThread.move((Direction) view.getTag());
                });
            }
        }

        // 1.11 预置位
        if (loginHandle.isbPTZX()) {
            Log.i(TAG, "支持预置位");

        }

        // 1.12 截屏
        // 只有一次截图完成才可以截另外一次
        isScreenshotting = false;
        btnScreenShot = findViewById(R.id.btnScreenShot);
        btnScreenShot.setOnClickListener(view -> {
            if (!isScreenshotting) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                                isScreenshotting = true;
                                Bitmap bitmap = mHSMediaPlayer.screenShot();
                                if (bitmap != null) {
                                    ContentResolver contentResolver = getContentResolver();
                                    Uri newUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                                    boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, contentResolver.openOutputStream(newUri));
                                    emitter.onNext(compress);
                                } else {
                                    emitter.onError(new Exception("截屏失败"));
                                }
                                emitter.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io()) // 被观察者线程
                        .observeOn(AndroidSchedulers.mainThread()) // 观察者线程
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean compress) {
                                if (compress) {
                                    Log.i(TAG, "截屏成功");
                                    Toast.makeText(PreviewActivity.this, "截屏成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "截图保存失败");
                                    Toast.makeText(PreviewActivity.this, "截图保存失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.i(TAG, "截图失败");
                                Toast.makeText(PreviewActivity.this, "截屏失败", Toast.LENGTH_SHORT).show();
                                isScreenshotting = false;
                            }

                            @Override
                            public void onComplete() {
                                isScreenshotting = false;
                            }
                        });
            } else {
                Toast.makeText(PreviewActivity.this, "截屏中，请稍后", Toast.LENGTH_SHORT).show();
            }
        });

        // 1.13 实时录像
        // (1) 开始录像
        tvRecordTime = findViewById(R.id.tvRecordTime);
        tvRecordTime.setVisibility(View.GONE);
        btnStartVideoRecord = findViewById(R.id.btnStartVideoRecord);
        timer = null;
        isVideoRecording = false;
        btnStartVideoRecord.setOnClickListener(view -> {
            if (!isVideoRecording) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                                isVideoRecording = true;
                                File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                                String timeStamp = System.currentTimeMillis() + "";
                                tempFile = File.createTempFile("record_" + timeStamp, ".mp4", externalFilesDir);
                                boolean result = mHSMediaPlayer.startRecord(tempFile.getAbsolutePath());
                                emitter.onNext(result);
                                emitter.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                tvRecordTime.setVisibility(View.VISIBLE);
                                tvRecordTime.setText("00:00");
                                if (timer != null) {
                                    timer.cancel();
                                }
                                timer = new Timer();
                                AtomicLong curSeconds = new AtomicLong();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(() -> {
                                            long seconds = curSeconds.getAndIncrement();
                                            String time = String.format("%02d:%02d", seconds / 60, seconds % 60);
                                            tvRecordTime.setText(time);
                                        });
                                    }
                                }, 0, 1000);
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                                if (aBoolean) {
                                    Log.i(TAG, "开始录像");
                                    Toast.makeText(PreviewActivity.this, "开始录像", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "开始录像失败");
                                    Toast.makeText(PreviewActivity.this, "开始录像失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        // (2) 结束录像
        btnStopVideoRecord = findViewById(R.id.btnStopVideoRecord);
        btnStopVideoRecord.setOnClickListener(view -> {
            if (isVideoRecording) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                                isVideoRecording = false;
                                boolean result = mHSMediaPlayer.stopRecord();
                                if (result) {
                                    // 将文件存储在相册,并删除临时文件
                                    ContentResolver contentResolver = getContentResolver();
                                    Uri newVideo = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                            new ContentValues());
                                    OutputStream outputStream = contentResolver.openOutputStream(newVideo);
                                    FileInputStream fileInputStream = new FileInputStream(tempFile);
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = fileInputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, len);
                                    }
                                    outputStream.close();
                                    fileInputStream.close();
                                    tempFile.delete();
                                    tempFile = null;
                                    emitter.onNext(true);
                                } else {
                                    emitter.onNext(false);
                                }
                                emitter.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean aBoolean) {
                                if (aBoolean) {
                                    Toast.makeText(PreviewActivity.this, "录像保存成功", Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "停止录像成功，录像保存成功");
                                } else {
                                    Log.i(TAG, "停止录像失败");
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                timer.cancel();
                                timer = null;
                                tvRecordTime.setVisibility(View.GONE);
                            }

                            @Override
                            public void onComplete() {
                                timer.cancel();
                                timer = null;
                                tvRecordTime.setVisibility(View.GONE);
                            }
                        });
            }
        });

        // 1.14 图像倒置
        btnReverse = findViewById(R.id.btnReverse);
        btnReverse.setOnClickListener(view -> {
            if (mHSMediaPlayer.isPlaying()) {
                boolean result = mHSMediaPlayer.setCamImageOrientation(1000);
                if (result) {
                    Log.i(TAG, "图像倒置成功");
                    Toast.makeText(PreviewActivity.this, "图像倒置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "图像倒置失败");
                    Toast.makeText(PreviewActivity.this, "图像倒置失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 1.15 音量设置
        cbSound = findViewById(R.id.cbSound);
        cbSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mHSMediaPlayer.enableAudio(isChecked);
        });

        // 1.16 视频清晰度选择
        btnQuality = findViewById(R.id.btnQuality);
        tvVideoQuality = findViewById(R.id.tvVideoQuality);
        videoQuality = 0; // 默认为0
        btnQuality.setOnClickListener(view -> {
            mHSMediaPlayer.stopPlay();
            if (videoQuality == 0) {
                videoQuality = 1;
                mHSMediaPlayer.startPlay(
                        0, // 播放窗口，默认0,（不支持多窗口）
                        0, // 播放通道 (默认为0)
                        videoQuality, // 视频清晰度
                        true, // 是否开启视频
                        loginHandle // 登录句柄
                );
                tvVideoQuality.setText("高清");
            } else {
                videoQuality = 0;
                mHSMediaPlayer.startPlay(
                        0, // 播放窗口，默认0,（不支持多窗口）
                        0, // 播放通道 (默认为0)
                        videoQuality, // 视频清晰度
                        true, // 是否开启视频
                        loginHandle // 登录句柄
                );
                tvVideoQuality.setText("标清");
            }
        });

        // 1.17 获取设备信息
        logView = findViewById(R.id.log_view);
        updateDeviceInfo();
    }


    private void updateDeviceInfo() {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<String> emitter) throws Throwable {
                        NetworkConfigInfo networkConfig = DeviceNetworkSetting.getNetworkConfig(deviceInfo, loginHandle);
                        IPConfigInfo ipConfig = DeviceNetworkSetting.getIPConfig(deviceInfo, loginHandle);
                        RecordConfigInfo recordConfig = DeviceRecordSetting.getRecordConfig(deviceInfo, loginHandle);
                        DateTimeConfigInfo dateTimeConfig = DeviceDateTimeSetting.getDateTimeConfig(deviceInfo, loginHandle);
                        AccountConfigInfo accountConfig = DeviceAccountSetting.getAccountConfig(deviceInfo, loginHandle);
                        VersionConfigInfo versionInfo = DeviceVersionSetting.getVersionInfo(deviceInfo, loginHandle);
                        VersionConfigInfo versionUpdate = DeviceVersionSetting.getVersionUpdate(deviceInfo, loginHandle);
//                        AlarmConfigInfo alarmConfig = DeviceAlarmSetting.getAlarmConfig(deviceInfo, loginHandle); // 这个类用不了

                        emitter.onNext("Wifi:" + networkConfig.getStrWifiName());
                        emitter.onNext("IP:" + ipConfig.getStrIP());
                        emitter.onNext("SD卡存储:" + recordConfig.getnDiskRemainSize() + "/" + recordConfig.getnDiskSize());
                        emitter.onNext("时间:" + dateTimeConfig.getStrTime() + " 类型" + dateTimeConfig.getnTimeType());
                        emitter.onNext("时区:" + dateTimeConfig.getnTimeZoneIndex());
                        emitter.onNext("账号:" + accountConfig.getStrUserName());
                        emitter.onNext("设备端软件版本:" + versionInfo.getStrAPPVersion() + " date:" + versionInfo.getStrAPPVersionDate());
                        emitter.onNext("内核版本：" + versionInfo.getStrKelVersion() + "　date:" + versionInfo.getStrKelVersionDate());
                        emitter.onNext("硬件版本：" + versionInfo.getStrHWVersion() + " date:" + versionInfo.getStrHWVersionDate());
                        emitter.onNext("是否有更新：" + versionUpdate.getnDeviceVersionUpdate());
//                        emitter.onNext("是否有布撤防：" + alarmConfig.isHasAlarmConfig());
//                        emitter.onNext("是否有移动报警：" + alarmConfig.isbMotionAlarmSwitch());


                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        logView.addLog(s);
                    }
                });
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
        startPlay(0);
    }

    /*
    开始播放，videoQuality为0时为标清，为1时为高清
     */
    private void startPlay(int videoQuality) {
        if (mHSMediaPlayer != null) {
            mHSMediaPlayer.startPlay(
                    0, // 播放窗口，默认0,（不支持多窗口）
                    0, // 播放通道 (默认为0)
                    videoQuality, // 视频清晰度
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
        if (ptzHandlerThread != null) {
            ptzHandlerThread.quit(); // 退出线程
        }
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