package com.example.myapplication.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class AnimationDemoActivity extends AppCompatActivity {

    private Button btnZhenAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);
        btnZhenAnimation = findViewById(R.id.btn_frame_animation);
        btnZhenAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimationDemoActivity.this, FrameAnimationActivity.class);
                startActivity(intent);
            }
        });
    }
}