package com.example.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, fragment)
                .commitNow();

        handler.postDelayed(() -> {
            getSupportFragmentManager().beginTransaction()
                    .detach(fragment)
                    .commitNow();
        }, 1000);

    }
}