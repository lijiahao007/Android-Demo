package com.example.myapplication.album;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AlbumFragmentAdapter albumFragmentAdapter;
    private LinearLayout editToolsLayout;
    private ImageView ivBack;
    private ImageView ivEdit;
    private TextView tvSelectAll;
    private TextView tvExitEdit;
    public static final String EDIT_MODE_CHANGE = "edit_mode_change";
    public static final String EDIT_MODE = "edit_mode";
    public static final String SELECT_ALL_CHANGE = "select_all_change";
    public static final String SELECT_ALL = "select_all";
    public static final String DELETE = "delete";
    public static final String SHARE = "share";

    private ImageView ivShare;
    private ImageView ivDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initTabLayout();
        initToolbar();
        setStatusBarColor();
    }

    // 初始化tabLayout 并关联ViewPager2.
    private void initTabLayout() {
        viewPager = findViewById(R.id.view_pager2);
        tabLayout = findViewById(R.id.tab_layout);

        albumFragmentAdapter = new AlbumFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(albumFragmentAdapter);
        int[] iconId = {
                R.drawable.ic_photo,
                R.drawable.ic_video
        };
        String[] tabText = {
                "图片",
                "视频"
        };
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                View customView = LayoutInflater.from(AlbumActivity.this).inflate(R.layout.view_album_tab, null);
                ImageView icon = customView.findViewById(R.id.iv_tab_icon);
                TextView text = customView.findViewById(R.id.tv_tab_text);
                icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), iconId[position], null));
                text.setText(tabText[position]);
                tab.setCustomView(customView);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedColor = ResourcesCompat.getColor(getResources(), R.color.tab_selected, null);
                ImageView icon = tab.getCustomView().findViewById(R.id.iv_tab_icon);
                icon.setColorFilter(selectedColor);
                TextView text = tab.getCustomView().findViewById(R.id.tv_tab_text);
                text.setTextColor(selectedColor);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int selectedColor = ResourcesCompat.getColor(getResources(), R.color.tab_unselected, null);
                ImageView icon = tab.getCustomView().findViewById(R.id.iv_tab_icon);
                icon.setColorFilter(selectedColor);
                TextView text = tab.getCustomView().findViewById(R.id.tv_tab_text);
                text.setTextColor(selectedColor);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayoutMediator.attach();
    }

    // 初始化应用工具栏
    private void initToolbar() {
        editToolsLayout = findViewById(R.id.edit_tools_layout);
        ivEdit = findViewById(R.id.iv_edit);
        ivBack = findViewById(R.id.iv_back);
        tvSelectAll = findViewById(R.id.tv_select_all);
        tvExitEdit = findViewById(R.id.tv_exit_edit);
        ivShare = findViewById(R.id.iv_share);
        ivDelete = findViewById(R.id.iv_deleter);

        ivBack.setOnClickListener(view -> {
            finish();
        });

        // 1. 编辑模式
        ivEdit.setOnClickListener(view -> {
            ivEdit.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            editToolsLayout.setVisibility(View.VISIBLE);
            tvSelectAll.setVisibility(View.VISIBLE);
            tvExitEdit.setVisibility(View.VISIBLE);
            Bundle bundle = new Bundle();
            bundle.putBoolean(EDIT_MODE, true);
            getSupportFragmentManager().setFragmentResult(EDIT_MODE_CHANGE, bundle);
            viewPager.setUserInputEnabled(false);
        });

        // 2. 退出编辑模式
        tvExitEdit.setOnClickListener(view -> {
            ivEdit.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            editToolsLayout.setVisibility(View.GONE);
            tvSelectAll.setVisibility(View.GONE);
            tvExitEdit.setVisibility(View.GONE);
            Bundle bundle = new Bundle();
            bundle.putBoolean(EDIT_MODE, false);
            getSupportFragmentManager().setFragmentResult(EDIT_MODE_CHANGE, bundle);
            viewPager.setUserInputEnabled(true);
        });

        // 3. 全选和全不选
        tvSelectAll.setOnClickListener(view -> {
            String text = tvSelectAll.getText().toString();
            boolean isSelectAll = false;
            if (text.equals("全选")) {
                tvSelectAll.setText("全不选");
                isSelectAll = true;
            } else {
                tvSelectAll.setText("全选");
                isSelectAll = false;
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(SELECT_ALL, isSelectAll);
            getSupportFragmentManager().setFragmentResult(SELECT_ALL_CHANGE, bundle);
        });

        // 4. 分享
        ivShare.setOnClickListener(view -> {
            getSupportFragmentManager().setFragmentResult(SHARE, new Bundle());
        });

        // 5. 删除
        ivDelete.setOnClickListener(view -> {
            getSupportFragmentManager().setFragmentResult(DELETE, new Bundle());
        });
    }


    private void setStatusBarColor() {
        Window window = getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.transparent, null));
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivityResult", "requestCode" + requestCode + " " + resultCode);
        if (resultCode == PhotoCheckActivity.DELETE_RESULT_CODE && data != null) {
            ArrayList<MediaBean> deleteBean = data.getParcelableArrayListExtra(PhotoCheckActivity.DELETE_BUNDLE);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PhotoCheckActivity.DELETE_BUNDLE, deleteBean);
            Log.i("onActivityResult", "deleteBean" + deleteBean.size());
            getSupportFragmentManager().setFragmentResult(PhotoFragment.DELETE_REQUEST_KEY, bundle);
        }
    }
}