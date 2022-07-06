package com.example.myapplication.setupnet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.defines.ResultCode;
import com.macrovideo.sdk.media.HSFileDownloader;
import com.macrovideo.sdk.media.IDownloadCallback;
import com.macrovideo.sdk.media.ILoginDeviceCallback;
import com.macrovideo.sdk.media.IRecFileCallback;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.media.LoginHelper;
import com.macrovideo.sdk.media.RecordFileHelper;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.objects.LoginParam;
import com.macrovideo.sdk.objects.RecordFileParam;
import com.macrovideo.sdk.objects.RecordVideoInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlayBackActivity extends AppCompatActivity {

    private static final String TAG = "PlayBackActivity";
    private DeviceInfo deviceInfo;
    private Button btnDatePick;
    private Button btnBeginTimePick;
    private Button btnEndTimePicker;
    private TextView tvDate;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private long date;
    private int beginHour;
    private int beginMinute;
    private int endMinute;
    private int endHour;
    private Button btnSearch;
    private LoginHandle loginHandle;
    private RecyclerView recycleView;
    private RecordVideoAdapter adapter;
    public static final String CLICK_RECORD_VIDEO_INFO = "click_record_video_info";
    public static final String LOGIN_HANDLE = "LOGIN_HANDLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back);

        btnDatePick = findViewById(R.id.btn_date_pick);
        btnBeginTimePick = findViewById(R.id.btn_begin_time_pick);
        btnEndTimePicker = findViewById(R.id.btn_end_time_pick);
        tvDate = findViewById(R.id.tv_date);
        tvBeginTime = findViewById(R.id.tv_begin_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        btnSearch = findViewById(R.id.btn_search);
        recycleView = findViewById(R.id.recycler_view);
        adapter = new RecordVideoAdapter();
        recycleView.setAdapter(adapter);

        // 1. 登录设备
        deviceInfo = SetupNetDemoActivity.getDeviceInfo();
        LoginParam loginParam = new LoginParam(deviceInfo, Defines.LOGIN_FOR_PLAYBACK);
        LoginHelper.loginDevice(this, loginParam, new ILoginDeviceCallback() {
            @Override
            public void onLogin(LoginHandle loginHandle) {

                if (loginHandle != null && loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                    // 2. 初始化回放
                    PlayBackActivity.this.loginHandle = loginHandle;
                    initPlayBack();
                } else {
                    Log.i(TAG, "登录失败");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlayBackActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            PlayBackActivity.this.finish();
                        }
                    });
                }
            }
        });
    }

    private void initPlayBack() {
        // 2.1 选择搜索范围
        date = 0;
        btnDatePick.setOnClickListener(view -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("选择录像日期")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setPositiveButtonText("确定")
                    .build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Log.i(TAG, "选择的日期：" + selection);
                Date date = new Date(selection);
                DateFormat dateFormat = SimpleDateFormat.getDateInstance();
                String dateStr = dateFormat.format(date);
                this.date = selection;
                tvDate.setText(dateStr);
            });
        });

        beginHour = 0;
        beginMinute = 0;
        btnBeginTimePick.setOnClickListener(view -> {
            MaterialTimePicker beginTimePicker = new MaterialTimePicker.Builder()
                    .setTitleText("选择开始时间")
                    .setPositiveButtonText("确定")
                    .setNegativeButtonText("取消")
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build();
            beginTimePicker.show(getSupportFragmentManager(), "BEGIN_TIME_PICKER");
            beginTimePicker.addOnPositiveButtonClickListener(selection -> {
                beginHour = beginTimePicker.getHour();
                beginMinute = beginTimePicker.getMinute();
                String beginTime = String.format("%02d:%02d", beginHour, beginMinute);
                tvBeginTime.setText(beginTime);
            });
        });

        endHour = 0;
        endMinute = 0;
        btnEndTimePicker.setOnClickListener(view -> {
            MaterialTimePicker endTimePicker = new MaterialTimePicker.Builder()
                    .setTitleText("选择结束时间")
                    .setPositiveButtonText("确定")
                    .setNegativeButtonText("取消")
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .build();
            endTimePicker.show(getSupportFragmentManager(), "END_TIME_PICKER");
            endTimePicker.addOnPositiveButtonClickListener(selection -> {
                endHour = endTimePicker.getHour();
                endMinute = endTimePicker.getMinute();
                String endTime = String.format("%02d:%02d", endHour, endMinute);
                tvEndTime.setText(endTime);
            });
        });

        btnSearch.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(date));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            RecordFileParam recordFileParam = new RecordFileParam(0, Defines.FILE_TYPE_ALL, year, month, day, beginHour, beginMinute, 0, endHour, endMinute, 0);
            Disposable subscribe = Observable.create(new ObservableOnSubscribe<ArrayList<RecordVideoInfo>>() {
                        @Override
                        public void subscribe(@NonNull ObservableEmitter<ArrayList<RecordVideoInfo>> emitter) throws Throwable {
                            RecordFileHelper.getRecordVideoInTFCard(loginHandle, recordFileParam, new IRecFileCallback() {
                                @Override
                                public void onReceiveFile(int nRecTotalCount, int nFileCount, ArrayList<RecordVideoInfo> arrayList) {
                                    Log.i(TAG, "搜索到" + nFileCount + "个录像文件");
                                    emitter.onNext(arrayList);
                                    emitter.onComplete();
                                }
                            });
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArrayList<RecordVideoInfo>>() {
                        @Override
                        public void accept(@NonNull ArrayList<RecordVideoInfo> recordVideoInfos) throws Exception {
                            // 2.2 显示搜索结果
                            adapter.submitList(recordVideoInfos);
                        }
                    });
        });

        // 3. 设置Adapter Listener, 处理跳转和下载
        adapter.setOnItemClickListener(new RecordVideoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RecordVideoInfo recordVideoInfo) {
                Intent intent = new Intent(PlayBackActivity.this, PlayBackVideoActivity.class);
                intent.putExtra(CLICK_RECORD_VIDEO_INFO, recordVideoInfo);
                intent.putExtra(LOGIN_HANDLE, (Parcelable) loginHandle);
                startActivity(intent);
            }
        });

        adapter.setRecordVideoDownloadClickListener(new RecordVideoAdapter.RecordVideoDownloadClickListener() {
            @Override
            public void onDownLoad(RecordVideoInfo recordVideoInfo, ProgressBar progressBar, RecordVideoAdapter.IDownloadState downloadState) {
                new Thread("下载录像") {
                    @Override
                    public void run() {
                        super.run();
                        HSFileDownloader hsFileDownloader = new HSFileDownloader();
                        File externalCacheDir = getExternalCacheDir();
                        String timeStamp = System.currentTimeMillis() + "";
                        File tempFile = null;
                        try {
                            tempFile = File.createTempFile("record_video" + timeStamp, ".mp4", externalCacheDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (tempFile == null) {
                            Log.i(TAG, "文件创建失败");
                            return;
                        }
                        File finalTempFile = tempFile;
                        hsFileDownloader.startDownloadRecordVideo(loginHandle, recordVideoInfo, tempFile.getAbsolutePath(), new IDownloadCallback() {
                            @Override
                            public void onDownloadProcess(Object o, int state, int progress) {
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(progress);

                                if (state == 1 || progress == 100) {
                                    // 下载完成
                                    downloadState.onFinish();
                                    ContentResolver contentResolver = getContentResolver();
                                    FileUtils.saveVideoInMediaStore(contentResolver, finalTempFile, true);
                                    Toast.makeText(PlayBackActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                                } else if (state == -1) {
                                    downloadState.onFailed();
                                }
                            }
                        });
                    }
                };
            }
        });


    }
}