package com.example.myapplication.setupnet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.example.hyfisheyepano.GLFisheyeView;
import com.example.myapplication.R;
import com.macrovideo.sdk.media.HSMediaPlayer;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.objects.RecordVideoInfo;

public class PlayBackVideoActivity extends AppCompatActivity {

    private RecordVideoInfo recordVideoInfo;
    private LoginHandle loginHandle;
    private LinearLayout llContainer;
    private HSMediaPlayer hsMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back_video);

        recordVideoInfo = getIntent().getParcelableExtra(PlayBackActivity.CLICK_RECORD_VIDEO_INFO);
        loginHandle = getIntent().getParcelableExtra(PlayBackActivity.LOGIN_HANDLE);

        llContainer = findViewById(R.id.llContainer);

        hsMediaPlayer = new HSMediaPlayer(this, false);
        GLFisheyeView glFisheyeView = new GLFisheyeView(this);
        glFisheyeView.setMode(HSMediaPlayer.PANO_PLAY_MODE_13);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, screenHeight);
        glFisheyeView.setLayoutParams(layoutParams);
        llContainer.addView(glFisheyeView);
        getLifecycle().addObserver(new GLFisheyeViewObserver(glFisheyeView));

        hsMediaPlayer.startPlayBack(0, loginHandle, recordVideoInfo, true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hsMediaPlayer.stopPlayBack();
        hsMediaPlayer.release();
    }
}