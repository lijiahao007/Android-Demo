package com.example.myapplication.tablayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

public class TabLayoutMenuActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_menu);
        TextView tvNormalTabLayout = findViewById(R.id.tv_normal_tablayout);
        tvNormalTabLayout.setOnClickListener(this);
        findViewById(R.id.tv_loop_viewpager).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_normal_tablayout:
                startActivity(new Intent(this, TabLayoutDemoActivity.class));
                break;
            case R.id.tv_loop_viewpager:

                break;

        }
    }
}