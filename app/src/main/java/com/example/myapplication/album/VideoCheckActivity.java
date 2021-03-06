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
//import com.yc.video.player.VideoPlayer;
//import com.yc.video.ui.view.BasisVideoController;

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
//    private VideoPlayer mVideoPlayer;
    private CheckBox cbSound;
//    private BasisVideoController videoController;

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
//        mVideoPlayer = findViewById(R.id.vp_show);

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
            Log.i(TAG, "??????????????????:" + bean + " curPos=" + curPos);

            new MaterialAlertDialogBuilder(this)
                    .setMessage("????????????????????????")
                    .setNegativeButton("??????", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .setPositiveButton("??????", (dialog, which) -> {
                        // ???????????????????????????
                        int newPos = -1; // ???????????????????????????
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
                            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        MediaBean removeBean = mediaBeans.remove(curPos);
                        // ??????????????????????????????????????????
                        deleteMediaBeanInDataBase(removeBean);
                        // ????????????????????????
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
            Log.i(TAG, "????????????:" + delete + " bean:" + bean);
        }
        return delete;
    }

    private void initVideoView() {
        MediaBean bean = mediaBeans.get(curPos);

        //??????????????????????????????????????????????????????
//        videoController = new BasisVideoController(this);
        //???????????????
//        mVideoPlayer.setController(videoController);
        //??????????????????????????????
//        mVideoPlayer.setUrl(bean.getUri().toString());
        //????????????
//        mVideoPlayer.start();
//        mVideoPlayer.setMute(!cbSound.isChecked());

        fullScreenControl.setOnClickListener(view -> {
//            mVideoPlayer.startFullScreen();
        });

        ivNextVideo.setOnClickListener(view -> {
            if (curPos < mediaBeans.size() - 1) {
                curPos++;
//                mVideoPlayer.setUrl(mediaBeans.get(curPos).getUri().toString());
//                mVideoPlayer.pause();
//                mVideoPlayer.replay(true);
            } else {
                Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        ivPrevVideo.setOnClickListener(view -> {
            if (mediaBeans.size() != 0 && curPos > 0) {
                curPos--;
//                mVideoPlayer.setUrl(mediaBeans.get(curPos).getUri().toString());
//                mVideoPlayer.pause();
//                mVideoPlayer.replay(true);
            } else {
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });

        cbPlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                mVideoPlayer.resume();
            } else {
//                mVideoPlayer.pause();
            }
        });

        cbSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
//                mVideoPlayer.setMute(false);
            } else {
//                mVideoPlayer.setMute(true);
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
        //?????????????????????
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //??????Flag?????????????????????????????????
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //?????????????????????
        window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        //???????????????????????????????????????
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // ????????????????????? & ??????????????????????????????

        //???view?????????????????????????????????????????????
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