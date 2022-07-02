package com.example.myapplication.animation;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;


// 帧动画
public class FrameAnimationActivity extends AppCompatActivity {

    private ImageView ivFrameAnimation;
    private ImageView ivFrameAnimation1;
    private AnimationDrawable runAnimation;
    private AnimationDrawable runAnimation1;
    private ImageView ivVectorFrameAnimation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_amination);

        ivFrameAnimation = findViewById(R.id.iv_frame_animation);
        ivFrameAnimation.setBackgroundResource(R.drawable.anim_run);
        runAnimation = (AnimationDrawable) ivFrameAnimation.getBackground();


        ivFrameAnimation1 = findViewById(R.id.iv_frame_animation1);
        runAnimation1 = (AnimationDrawable) ivFrameAnimation1.getBackground();
        ivFrameAnimation1.setOnClickListener(view -> runAnimation1.start());


        ivVectorFrameAnimation = findViewById(R.id.iv_vector_frame_animation);
        AnimatedVectorDrawable background = (AnimatedVectorDrawable) ivVectorFrameAnimation.getBackground();
        ivVectorFrameAnimation.setOnClickListener(view -> background.start());


    }

    @Override
    protected void onStart() {
        super.onStart();
        runAnimation.start();
        runAnimation1.start();
    }
}