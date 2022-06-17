package com.example.myapplication.album;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.album.adapter.MediaBeanAdapter;
import com.example.myapplication.album.adapter.PhotoCheckAdapter;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaBeanDBHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;

public class PhotoCheckActivity extends AppCompatActivity {

    private ArrayList<MediaBean> mediaBeans;
    private int position;
    private ImageView ivBack;
    private TextView tvFileName;
    private ViewPager2 viewPager;
    private PhotoCheckAdapter photoCheckAdapter;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback;
    private int curPos = position;
    private ImageView ivShare;
    private ImageView ivDelete;
    private ContentResolver contentResolver;
    private static final String TAG = "PhotoCheckActivity";
    private SQLiteDatabase database;
    private MediaBeanDBHelper helper;
    public static final int DELETE_RESULT_CODE = 2;
    public static final String DELETE_BUNDLE = "delete_bundle";

    private ArrayList<MediaBean> deleteMediaBean = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_check);
        setStatusBarColor();
        getIntentParams();
        tvFileName = findViewById(R.id.tv_file_name);
        tvFileName.setText(new File(mediaBeans.get(position).getFileName()).getName());
        ivBack = findViewById(R.id.iv_back);

        // 配置ViewPager
        viewPager = findViewById(R.id.view_pager2);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        photoCheckAdapter = new PhotoCheckAdapter();
        viewPager.setAdapter(photoCheckAdapter);
        photoCheckAdapter.setList(mediaBeans);
        viewPager.setCurrentItem(position, false);
        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                curPos = position;
                String fileName = mediaBeans.get(curPos).getFileName();
                File file = new File(fileName);
                String name = file.getName();
                tvFileName.setText(name);
                Log.i(TAG, "当前:" + mediaBeans.get(curPos) + " curPos:" + curPos);
            }
        };
        viewPager.registerOnPageChangeCallback(onPageChangeCallback);

        ivBack.setOnClickListener(view -> {
            setReturnResult();
            finish();
        });


        contentResolver = getContentResolver();
        ivShare = findViewById(R.id.iv_share);
        ivDelete = findViewById(R.id.iv_deleter);
        ivShare.setOnClickListener(view -> {
            MediaBean bean = mediaBeans.get(curPos);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_STREAM, bean.getUri());
            startActivity(intent);
        });

        helper = new MediaBeanDBHelper(this);
        database = helper.getWritableDatabase();
        contentResolver = getContentResolver();

        ivDelete.setOnClickListener(view -> {
            MediaBean bean = mediaBeans.get(curPos);
            Log.i(TAG, "需要删除的是:" + bean + " curPos=" + curPos );

            new MaterialAlertDialogBuilder(this)
                    .setMessage("是否删除该该图片")
                    .setNegativeButton("取消", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .setPositiveButton("确定", (dialog, which) -> {
                        photoCheckAdapter.deleteItem(bean);
                        deleteMediaBeanInDataBase(bean);
                        mediaBeans.remove(bean);
                        deleteMediaBean.add(bean);
                    })
                    .show();
        });
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

    private void setStatusBarColor() {
        Window window = getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE); // 设置状态栏可见 & 状态栏字体颜色默认为白色

        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    private void getIntentParams() {
        mediaBeans = getIntent().getParcelableArrayListExtra(MediaBeanAdapter.MEDIA_BEAN_LIST);
        position = getIntent().getIntExtra(MediaBeanAdapter.CLICK_POSITION, -1);
        curPos = position;
        Log.i(TAG, "初始状态：" + position + " " + mediaBeans.get(position));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
    }

    private void setReturnResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(DELETE_BUNDLE, deleteMediaBean);
        Log.i("result passing ", "deletebeans size=" + deleteMediaBean.size());
        setResult(DELETE_RESULT_CODE, intent);
    }

    @Override
    public void onBackPressed() {
        setReturnResult();
        finish();
        super.onBackPressed();
    }


}