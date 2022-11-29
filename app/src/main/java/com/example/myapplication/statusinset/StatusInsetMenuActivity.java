package com.example.myapplication.statusinset;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.databinding.ActivityStatusInsetDemoBinding;

import java.util.ArrayList;

public class StatusInsetMenuActivity extends BaseActivity<ActivityStatusInsetDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("状态栏", StatusBarDemoActivity.class));
            add(new MenuAdapter.MenuInfo("导航栏", NavigationBarDemoActivity.class));
            add(new MenuAdapter.MenuInfo("软键盘", SoftImeActivity.class));
            add(new MenuAdapter.MenuInfo("InsetViewCompat", WindowInsetViewCompatDemoActivity.class));
        }});
    }
}