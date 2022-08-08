package com.example.myapplication.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.myapplication.R;

public class FragmentDemoActivity extends AppCompatActivity {

    private FrameLayout flContainer;
    private FragmentManager supportFragmentManager;
    private String TAG = "FragmentDemoActivity";
    private Button btnOneFragment;
    private boolean isOneFragmentShow = false;
    private boolean isTwoFragmentShow = true;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_demo);

        supportFragmentManager = getSupportFragmentManager();

        OneFragment oneFragment = OneFragment.newInstance(null, null);
        TwoFragment twoFragment = TwoFragment.newInstance(null, null);
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, oneFragment, oneFragment.getClass().getSimpleName());
        fragmentTransaction.commitNow();


        handler.postDelayed(()-> {
            FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.fl_container, twoFragment, twoFragment.getClass().getSimpleName());
            fragmentTransaction1.commitNow();
        }, 1000);

        handler.postDelayed(()-> {
            FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.fl_container, oneFragment, oneFragment.getClass().getSimpleName());
            fragmentTransaction1.commitNow();
        }, 2000);


        handler.postDelayed(()-> {
            FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
            Log.i(TAG, "\n");
            fragmentTransaction1.hide(oneFragment);
            fragmentTransaction1.commitNow();
        }, 3000);

        handler.postDelayed(()-> {
            FragmentTransaction fragmentTransaction1 = supportFragmentManager.beginTransaction();
            Log.i(TAG, "\n");
            fragmentTransaction1.show(oneFragment);
            fragmentTransaction1.commitNow();
        }, 4000);


        Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fl_container);
        Log.i(TAG, "fragment:" + fragmentById);


    }


}