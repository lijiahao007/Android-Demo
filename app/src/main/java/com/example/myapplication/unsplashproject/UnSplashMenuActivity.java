package com.example.myapplication.unsplashproject;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.databinding.ActivityUnSplashMenuBinding;
import com.example.myapplication.unsplashproject.onlyokhttp.UnsplashPhotoListActivity;

import java.util.ArrayList;

public class UnSplashMenuActivity extends BaseActivity<ActivityUnSplashMenuBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Only OkHttp", UnsplashPhotoListActivity.class));
        }});
    }
}