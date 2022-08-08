package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.tablayout.TabLayoutDemoActivity;

public class CustomViewMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_menu);


        findViewById(R.id.canvas_normal).setOnClickListener(view -> {
            startActivity(new Intent(this, CustomViewDemoActivity.class));
        });

        findViewById(R.id.bessel_two).setOnClickListener(view -> {
            startActivity(new Intent(this, Bessel2Activity.class));
        });

        findViewById(R.id.bessel_three).setOnClickListener(view -> {
            startActivity(new Intent(this, Bessel3Activity.class));
        });

        findViewById(R.id.elastic_ball).setOnClickListener(view -> {
            startActivity(new Intent(this, ElasticBallActivity.class));
        });

        findViewById(R.id.matrix_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, MatrixPolyActivity.class));
        });

        findViewById(R.id.click_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, ClickEventDispatcherActivity.class));
        });
    }
}