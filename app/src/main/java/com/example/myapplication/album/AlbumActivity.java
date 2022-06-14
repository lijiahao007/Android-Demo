package com.example.myapplication.album;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.StatusBarManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AlbumFragmentAdapter albumFragmentAdapter;
    private LinearLayout editToolsLayout;
    private ImageView ivBack;
    private ImageView ivEdit;
    private TextView tvSelectAll;
    private TextView tvExitEdit;

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

        ivBack.setOnClickListener(view -> {
            finish();
        });

        // 编辑模式
        ivEdit.setOnClickListener(view -> {
            ivEdit.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
            editToolsLayout.setVisibility(View.VISIBLE);
            tvSelectAll.setVisibility(View.VISIBLE);
            tvExitEdit.setVisibility(View.VISIBLE);
        });

        // 退出编辑模式
        tvExitEdit.setOnClickListener(view -> {
            ivEdit.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            editToolsLayout.setVisibility(View.GONE);
            tvSelectAll.setVisibility(View.GONE);
            tvExitEdit.setVisibility(View.GONE);
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
}