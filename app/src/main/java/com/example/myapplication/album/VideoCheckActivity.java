package com.example.myapplication.album;

import static com.example.myapplication.album.AlbumActivity.DELETE_BUNDLE;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.example.myapplication.R;
import com.example.myapplication.album.adapter.MediaBeanAdapter;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaBeanDBHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yc.video.player.VideoPlayer;
import com.yc.video.ui.view.BasisVideoController;

import java.io.File;
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
    private LinearLayout soundLayout;
    private LinearLayout screenShoot;
    private TextView tvFileName;
    private ImageView ivBack;
    private SQLiteDatabase database;
    private MediaBeanDBHelper helper;
    private ContentResolver contentResolver;
    private ArrayList<MediaBean> deleteMediaBean = new ArrayList<>();
    private VideoPlayer mVideoPlayer;
    private CheckBox cbSound;
    private BasisVideoController videoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_check);
        setStatusBarColor();
        getIntentParams();
        ivBack = findViewById(R.id.iv_back);
        tvFileName = findViewById(R.id.tv_file_name);
        screenShoot = findViewById(R.id.layout_screen_shot);
        soundLayout = findViewById(R.id.layout_sound);
        cbSound = findViewById(R.id.cb_sound);
        fullScreenControl = findViewById(R.id.layout_full_screen);
        cbPlay = findViewById(R.id.cb_play);
        ivPrevVideo = findViewById(R.id.iv_prev);
        ivNextVideo = findViewById(R.id.iv_next);
        ivShare = findViewById(R.id.iv_share);
        ivDelete = findViewById(R.id.iv_deleter);
        mVideoPlayer = findViewById(R.id.vp_show);

        initVideoView();
        initToolsView();
    }

    private void initToolsView() {
        contentResolver = getContentResolver();
        helper = new MediaBeanDBHelper(this);
        database = helper.getWritableDatabase();

        ivShare.setOnClickListener(view -> {
            MediaBean bean = mediaBeans.get(curPos);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("video/mp4");
            intent.putExtra(Intent.EXTRA_STREAM, bean.getUri());
            startActivity(intent);
        });

        ivDelete.setOnClickListener(view -> {
            MediaBean bean = mediaBeans.get(curPos);
            Log.i(TAG, "需要删除的是:" + bean + " curPos=" + curPos);

            new MaterialAlertDialogBuilder(this)
                    .setMessage("是否删除该该视频")
                    .setNegativeButton("取消", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .setPositiveButton("确定", (dialog, which) -> {
                        // 调整当前显示的视频
                        int newPos = -1; // 需要显示的视频位置
                        if (curPos < mediaBeans.size() - 1) {
                            newPos = curPos + 1;
                        } else if (curPos != 0) {
                            newPos = curPos - 1;
                        }
                        if (newPos != -1) {
//                            vvShow.setVideoURI(mediaBeans.get(newPos).getUri());
                        } else {
//                            vvShow.pause();
//                            vvShow.setVideoURI(null);
                            Toast.makeText(this, "这是最后一个视频了", Toast.LENGTH_SHORT).show();
                        }
                        MediaBean removeBean = mediaBeans.remove(curPos);
                        // 从数据库中删除、从文件中删除
                        deleteMediaBeanInDataBase(removeBean);
                        // 记录下删除的视频
                        deleteMediaBean.add(bean);
                        curPos = newPos;

                    })
                    .show();
        });

        ivBack.setOnClickListener(view -> {
            setReturnResult();
            finish();
        });
        String fileName = new File(mediaBeans.get(curPos).getFileName()).getName();
        tvFileName.setText(fileName);

    }

    private int deleteMediaBeanInDataBase(MediaBean bean) {
        String where = MediaBean.Entry._ID + " = " + bean.getId();
        int delete = database.delete(MediaBean.Entry.TABLE_NAME, where, null);
        if (delete == 1) {
            delete = contentResolver.delete(bean.getUri(), null, null);
            Log.i(TAG, "成功删除:" + delete + " bean:" + bean);
        }
        return delete;
    }

    private void initVideoView() {
        MediaBean bean = mediaBeans.get(curPos);

        //创建基础视频播放器，一般播放器的功能
        videoController = new BasisVideoController(this);
        //设置控制器
        mVideoPlayer.setController(videoController);
        //设置视频播放链接地址
        mVideoPlayer.setUrl(bean.getUri().toString());
        //开始播放
        mVideoPlayer.start();
        mVideoPlayer.setMute(!cbSound.isChecked());

        fullScreenControl.setOnClickListener(view -> {
            mVideoPlayer.startFullScreen();
        });

        ivNextVideo.setOnClickListener(view -> {
            if (curPos < mediaBeans.size() - 1) {
                curPos++;
                mVideoPlayer.setUrl(mediaBeans.get(curPos).getUri().toString());
                mVideoPlayer.pause();
                mVideoPlayer.replay(true);
            } else {
                Toast.makeText(this, "已是最后一个视频", Toast.LENGTH_SHORT).show();
            }
        });

        ivPrevVideo.setOnClickListener(view -> {
            if (mediaBeans.size() != 0 && curPos > 0) {
                curPos--;
                mVideoPlayer.setUrl(mediaBeans.get(curPos).getUri().toString());
                mVideoPlayer.pause();
                mVideoPlayer.replay(true);
            } else {
                Toast.makeText(this, "已是第一个视频", Toast.LENGTH_SHORT).show();
            }
        });

        cbPlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mVideoPlayer.resume();
            } else {
                mVideoPlayer.pause();
            }
        });

        cbSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mVideoPlayer.setMute(false);
            } else {
                mVideoPlayer.setMute(true);
            }
        });


    }

    private void getIntentParams() {
        mediaBeans = getIntent().getParcelableArrayListExtra(MediaBeanAdapter.MEDIA_BEAN_LIST);
        position = getIntent().getIntExtra(MediaBeanAdapter.CLICK_POSITION, -1);
        curPos = position;
    }

    @Override
    public void onBackPressed() {
        setReturnResult();
        finish();
        super.onBackPressed();
    }

    private void setReturnResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(DELETE_BUNDLE, deleteMediaBean);
        Log.i("result passing ", "deletebeans size=" + deleteMediaBean.size());
        setResult(DELETE_RESULT_CODE, intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}