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
import android.view.GestureDetector;
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
import com.example.hyfisheyepano.OnScaleChangeListener;
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
    private Button btnStartVideoRecord; // ??????????????????
    private Button btnStopVideoRecord; // ??????????????????
    private volatile boolean isScreenshotting = false; // ????????????????????????????????????
    private volatile boolean isVideoRecording = false; // ??????????????????
    private TextView tvRecordTime; // ??????????????????
    private Timer timer = null; // ???????????????
    private File tempFile = null; // ????????????
    private Button btnReverse;
    private CheckBox cbSound;
    private Button btnQuality;
    private int videoQuality = 0; // ???????????? 0:?????? 1:??????
    private TextView tvVideoQuality;
    private LogView logView;
    private GestureDetector gestureDetector;
    private boolean isGLFisheyeViewScaling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????????????????????Activity???????????????AppCompatActivity
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_preview);

        deviceInfo = SetupNetDemoActivity.getDeviceInfo();
        llContainer = findViewById(R.id.llContainer);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // 1. ????????????
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
                        Log.i(TAG, "???????????????" + loginHandle.getnResult());
                    }
                } else {
                    Log.i(TAG, "???????????????loginHandle == null");
                }
            }
        });
    }

    private void initMediaPlayer() {
        // 1. ?????????mHSMediaPlayer
        if (mHSMediaPlayer == null) {
            mHSMediaPlayer = new HSMediaPlayer(this, false);
        }
        mHSMediaPlayer.setHWDecodeStatus(false, false);
        // 1.1 ???????????????????????? GLFisheyeView
        GLFisheyeView glFisheyeView = new GLFisheyeView(this);
        glFisheyeView.setMode(HSMediaPlayer.PANO_PLAY_MODE_13);
        //
        glFisheyeView.setScale(true);
        // 1.2 GLFisheyeView ?????????????????????16:9
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(screenWidth, screenWidth * 9 / 16);
        glFisheyeView.setLayoutParams(layoutParams);
        mHSMediaPlayer.setGlFishView(glFisheyeView);
        // 1.3 ??????????????????????????????
        mHSMediaPlayer.setTvTimeOSD(new ITimeTextCallback() {
            @Override
            public void setTimeText(String s) {
//                Log.i(TAG, "mHSMediaPlayer.setTvTimeOSD: setTimeText:" + s);
            }
        });

        // 1.4 ???glFisheyeView????????????????????????
        llContainer.addView(mHSMediaPlayer.getGLFisheyeView());
        // 1.5 glFisheyeView????????????????????????
        getLifecycle().addObserver(new GLFisheyeViewObserver(glFisheyeView));

        // 1.6 ????????????
        startPlay();

        // 1.7 ??????????????????
        tvInfo = findViewById(R.id.tvOnline);
        boolean isbInLan = loginHandle.isbInLan();
        DeviceStatus deviceStatus = LoginHelper.getDeviceStatus(deviceInfo);
        int onlineStat = deviceStatus.getnOnlineStat();
        int onlineState = deviceInfo.getnOnLineStat();
        Log.i(TAG, "isbInLan:" + isbInLan + " deviceStatus.onlineStat:" + onlineStat + " deviceInfo.onlineState:" + onlineState);
        tvInfo.setText(isbInLan ? "???????????????" : "???????????????");

        // 1.8 ?????????????????????????????????
        btnFullScreen = findViewById(R.id.btnFullScreen);
        btnFullScreen.setOnClickListener(view -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            btnFullScreen.setText("????????????");
        });
        ivBack = findViewById(R.id.ivBack);
        ivBack.setVisibility(View.GONE);
        ivBack.setOnClickListener(view -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            btnFullScreen.setText("??????");
        });

        // 1.9 ??????????????????
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

        // 1.10 ??????????????????
        // (1) ????????????
        if (loginHandle.isbPTZ()) { // ????????????
            Log.i(TAG, "????????????");
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

            // (2) ??????????????????
            isGLFisheyeViewScaling = false; // ???????????????????????????????????????
            final float MIN_DISTANCE = 100;
            final float MIN_VELOCITY = 100;
            gestureDetector = new GestureDetector(this, new MyGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float distanceX = e2.getX() - e1.getX();
                    float distanceY = e2.getY() - e1.getY();
                    float vx = Math.abs(velocityX);
                    float vy = Math.abs(velocityY);
                    Log.i(TAG, "onFling: velocityX:" + velocityX + " velocityY:" + velocityY + " distanceX:" + distanceX + " distanceY:" + distanceY);

                    if (!isGLFisheyeViewScaling) {
                        // ?????????????????????????????????????????????
                        if (Math.abs(distanceX) > MIN_DISTANCE && vx > MIN_VELOCITY) {
                            if (distanceX > 0) {
                                Log.i(TAG, "onFling: ????????????");
                                ptzHandlerThread.move(Direction.RIGHT);
                            } else {
                                Log.i(TAG, "onFling: ????????????");
                                ptzHandlerThread.move(Direction.LEFT);
                            }
                        }

                        if (Math.abs(distanceY) > MIN_DISTANCE && vy > MIN_VELOCITY) {
                            if (distanceY > 0) {
                                Log.i(TAG, "onFling: ????????????");
                                ptzHandlerThread.move(Direction.DOWN);
                            } else {
                                Log.i(TAG, "onFling: ????????????");
                                ptzHandlerThread.move(Direction.UP);
                            }
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });

            if (mHSMediaPlayer != null && mHSMediaPlayer.getGLFisheyeView() != null) {
                mHSMediaPlayer.getGLFisheyeView().setScaleChangeListener(new OnScaleChangeListener() {
                    @Override
                    public void onScaleChange(float v) {
                        Log.i(TAG, "onScaleChange: " + v);
                        if (v > 1) {
                            isGLFisheyeViewScaling = true;
                        } else {
                            isGLFisheyeViewScaling = false;
                        }
                    }
                });

                mHSMediaPlayer.getGLFisheyeView().setOnTouchListener((v, event) -> {
                    return gestureDetector.onTouchEvent(event);
                });

            }

        }


        // 1.11 ?????????
        if (loginHandle.isbPTZX()) {
            Log.i(TAG, "???????????????");

        }

        // 1.12 ??????
        // ????????????????????????????????????????????????
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
                                    emitter.onError(new Exception("????????????"));
                                }
                                emitter.onComplete();
                            }
                        })
                        .subscribeOn(Schedulers.io()) // ??????????????????
                        .observeOn(AndroidSchedulers.mainThread()) // ???????????????
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onNext(@io.reactivex.rxjava3.annotations.NonNull Boolean compress) {
                                if (compress) {
                                    Log.i(TAG, "????????????");
                                    Toast.makeText(PreviewActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "??????????????????");
                                    Toast.makeText(PreviewActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.i(TAG, "????????????");
                                Toast.makeText(PreviewActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                isScreenshotting = false;
                            }

                            @Override
                            public void onComplete() {
                                isScreenshotting = false;
                            }
                        });
            } else {
                Toast.makeText(PreviewActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        // 1.13 ????????????
        // (1) ????????????
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
                                    Log.i(TAG, "????????????");
                                    Toast.makeText(PreviewActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "??????????????????");
                                    Toast.makeText(PreviewActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
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
        // (2) ????????????
        btnStopVideoRecord = findViewById(R.id.btnStopVideoRecord);
        btnStopVideoRecord.setOnClickListener(view -> {
            if (isVideoRecording) {
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                            @Override
                            public void subscribe(@io.reactivex.rxjava3.annotations.NonNull ObservableEmitter<Boolean> emitter) throws Throwable {
                                isVideoRecording = false;
                                boolean result = mHSMediaPlayer.stopRecord();
                                if (result) {
                                    // ????????????????????????,?????????????????????
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
                                    Toast.makeText(PreviewActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                    Log.i(TAG, "???????????????????????????????????????");
                                } else {
                                    Log.i(TAG, "??????????????????");
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

        // 1.14 ????????????
        btnReverse = findViewById(R.id.btnReverse);
        btnReverse.setOnClickListener(view -> {
            if (mHSMediaPlayer.isPlaying() && loginHandle.isbReversePRI()) {
                boolean result = mHSMediaPlayer.setCamImageOrientation(1000);
                if (result) {
                    Log.i(TAG, "??????????????????");
                    Toast.makeText(PreviewActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "??????????????????");
                    Toast.makeText(PreviewActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 1.15 ????????????
        cbSound = findViewById(R.id.cbSound);
        cbSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mHSMediaPlayer.enableAudio(isChecked);
        });

        // 1.16 ?????????????????????
        btnQuality = findViewById(R.id.btnQuality);
        tvVideoQuality = findViewById(R.id.tvVideoQuality);
        videoQuality = 0; // ?????????0
        btnQuality.setOnClickListener(view -> {
            mHSMediaPlayer.stopPlay();
            if (videoQuality == 0) {
                videoQuality = 1;
                mHSMediaPlayer.startPlay(
                        0, // ?????????????????????0,????????????????????????
                        0, // ???????????? (?????????0)
                        videoQuality, // ???????????????
                        true, // ??????????????????
                        loginHandle // ????????????
                );
                tvVideoQuality.setText("??????");
            } else {
                videoQuality = 0;
                mHSMediaPlayer.startPlay(
                        0, // ?????????????????????0,????????????????????????
                        0, // ???????????? (?????????0)
                        videoQuality, // ???????????????
                        true, // ??????????????????
                        loginHandle // ????????????
                );
                tvVideoQuality.setText("??????");
            }
        });

        // 1.17 ??????????????????
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

                        emitter.onNext("ID:" + loginHandle.getnDeviceID());
                        emitter.onNext("Wifi:" + networkConfig.getStrWifiName());
                        emitter.onNext("IP:" + ipConfig.getStrIP());
                        emitter.onNext("SD?????????:" + recordConfig.getnDiskRemainSize() + "/" + recordConfig.getnDiskSize());
                        emitter.onNext("??????:" + dateTimeConfig.getStrTime() + " ??????" + dateTimeConfig.getnTimeType());
                        emitter.onNext("??????:" + dateTimeConfig.getnTimeZoneIndex());
                        emitter.onNext("??????:" + accountConfig.getStrUserName());
                        emitter.onNext("?????????????????????:" + versionInfo.getStrAPPVersion() + " date:" + versionInfo.getStrAPPVersionDate());
                        emitter.onNext("???????????????" + versionInfo.getStrKelVersion() + "???date:" + versionInfo.getStrKelVersionDate());
                        emitter.onNext("???????????????" + versionInfo.getStrHWVersion() + " date:" + versionInfo.getStrHWVersionDate());
                        emitter.onNext("??????????????????" + versionUpdate.getnDeviceVersionUpdate());
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
        screenWidth = dm.widthPixels;// ?????????
        screenHeight = dm.heightPixels;// ?????????

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // ??????
            showLandscape();
        } else {
            // ??????
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

        // ???????????????
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

        // ???????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    private void startPlay() {
        startPlay(0);
    }

    /*
    ???????????????videoQuality???0??????????????????1????????????
     */
    private void startPlay(int videoQuality) {
        if (mHSMediaPlayer != null) {
            mHSMediaPlayer.startPlay(
                    0, // ?????????????????????0,????????????????????????
                    0, // ???????????? (?????????0)
                    videoQuality, // ???????????????
                    true, // ??????????????????
                    loginHandle // ????????????
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
            ptzHandlerThread.quit(); // ????????????
        }

        if (mHSMediaPlayer != null) {
            mHSMediaPlayer.release();
            mHSMediaPlayer = null;
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