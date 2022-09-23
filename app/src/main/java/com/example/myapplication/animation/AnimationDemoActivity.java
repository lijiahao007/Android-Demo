package com.example.myapplication.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityAnimationDemoBinding;

import java.util.ArrayList;

public class AnimationDemoActivity extends BaseActivity<ActivityAnimationDemoBinding> {

    private Button btnZhenAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("帧动画", FrameAnimationActivity.class));
            add(new MenuAdapter.MenuInfo("抖动", ShakeActivity.class));


        }});


    }
}