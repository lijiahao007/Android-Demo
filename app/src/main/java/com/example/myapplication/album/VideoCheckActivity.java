package com.example.myapplication.album;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.example.myapplication.R;
import com.example.myapplication.album.adapter.MediaBeanAdapter;
import com.example.myapplication.album.bean.MediaBean;

import java.util.ArrayList;

public class VideoCheckActivity extends AppCompatActivity {

    public static final int DELETE_RESULT_CODE = 3;
    private static final String TAG = "VideoCheckActivity";
    private ArrayList<MediaBean> mediaBeans;
    private int position;
    private int curPos = position;
    private ImageView ivDelete;
    private ImageView ivShare;
    private ImageView ivNextVideo;
    private ImageView ivPrevVideo;
    private CheckBox cbPlay;
    private LinearLayout fullScreenControl;
    private LinearLayout soundControl;
    private LinearLayout screenShoot;
    private VideoView vvShow;
    private TextView tvFileName;
    private ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_check);
        setStatusBarColor();
        getIntentParams();
        ivBack = findViewById(R.id.iv_back);
        tvFileName = findViewById(R.id.tv_file_name);
        vvShow = findViewById(R.id.vv_show);
        screenShoot = findViewById(R.id.layout_screen_shot);
        soundControl = findViewById(R.id.layout_sound);
        fullScreenControl = findViewById(R.id.layout_full_screen);
        cbPlay = findViewById(R.id.cb_play);
        ivPrevVideo = findViewById(R.id.iv_prev);
        ivNextVideo = findViewById(R.id.iv_next);
        ivShare = findViewById(R.id.iv_share);
        ivDelete = findViewById(R.id.iv_deleter);
        initVideoView();
    }

    private void initVideoView() {
        MediaBean bean = mediaBeans.get(curPos);
        vvShow.setVideoURI(bean.getUri());
        vvShow.requestFocus();
        MediaController mediaController = new MediaController(this);
        vvShow.setMediaController(mediaController);
        cbPlay.setChecked(false);

        vvShow.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                cbPlay.setChecked(true);
                mediaController.setAnchorView(vvShow);
            }
        });


        vvShow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                cbPlay.setChecked(false);
            }
        });


        cbPlay.setChecked(true);
        cbPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vvShow.start();
                } else {
                    vvShow.pause();
                }
            }
        });

        ivNextVideo.setOnClickListener(view -> {
            if (curPos < mediaBeans.size() - 1) {
                curPos++;
                MediaBean newBean = mediaBeans.get(curPos);
                vvShow.setVideoURI(newBean.getUri());
                vvShow.start();
                cbPlay.setChecked(true);
            } else {
                Toast.makeText(this, "已经是最后一个视频", Toast.LENGTH_SHORT).show();
            }
        });

        ivPrevVideo.setOnClickListener(view -> {
            if (curPos > 0) {
                curPos--;
                MediaBean newBean = mediaBeans.get(curPos);
                vvShow.setVideoURI(newBean.getUri());
                vvShow.start();
                cbPlay.setChecked(true);
            } else {
                Toast.makeText(this, "已经是第一个视频", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getIntentParams() {
        mediaBeans = getIntent().getParcelableArrayListExtra(MediaBeanAdapter.MEDIA_BEAN_LIST);
        position = getIntent().getIntExtra(MediaBeanAdapter.CLICK_POSITION, -1);
        curPos = position;
    }


    private void setStatusBarColor() {
        Window window = getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // 设置状态栏可见 & 状态栏字体颜色为黑色

        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

}