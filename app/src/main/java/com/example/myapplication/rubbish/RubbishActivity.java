package com.example.myapplication.rubbish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityRubbishBinding;

import java.util.ArrayList;

public class RubbishActivity extends BaseActivity<ActivityRubbishBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("enable vs clickable", EnableDemoActivity.class));
        }});
    }
}