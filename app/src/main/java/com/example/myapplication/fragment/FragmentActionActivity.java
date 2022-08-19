package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.viewpager.fragment.OneFragment;

public class FragmentActionActivity extends AppCompatActivity {

    private FrameLayout flContainer;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_action);

        flContainer = findViewById(R.id.fl_container);

        OneFragment fragment = new OneFragment();
        TwoFragment twoFragment = new TwoFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, fragment)
                .add(R.id.fl_container, twoFragment)
                .hide(twoFragment)
                .commitNow();

        View viewById = findViewById(R.id.btn);
        viewById.setOnClickListener(view -> {
            startActivity(new Intent(this, FragmentTmpActivity.class));
        });
    }
}