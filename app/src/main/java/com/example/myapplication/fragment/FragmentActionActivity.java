package com.example.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityFragmentActionBinding;
import com.example.myapplication.viewpager.fragment.FourFragment;
import com.example.myapplication.viewpager.fragment.OneFragment;
import com.example.myapplication.viewpager.fragment.ThreeFragment;

public class FragmentActionActivity extends BaseActivity<ActivityFragmentActionBinding> {

    private FrameLayout flContainer;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flContainer = findViewById(R.id.fl_container);

        OneFragment firstFragment = new OneFragment();
        TwoFragment twoFragment = new TwoFragment();
        ThreeFragment threeFragment = new ThreeFragment();
        FourFragment fourFragment = new FourFragment();

        addFragmentToBackStack(firstFragment);
        addFragmentToBackStack(twoFragment);
        addFragmentToBackStack(threeFragment);
        addFragmentToBackStack(fourFragment);


        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackEntryCount; i++ ) {
            FragmentManager.BackStackEntry backStackEntryAt = getSupportFragmentManager().getBackStackEntryAt(i);
            String name = backStackEntryAt.getName();
            int id = backStackEntryAt.getId();
            Log.i("FragmentActionActivity", name + " id=" +  id);
            binding.tvText.setText(binding.tvText.getText().toString() + "\n name + \" id=\" +  id");

        }

        findViewById(R.id.btn).setOnClickListener(view -> {
            getSupportFragmentManager().popBackStack();
        });


    }

    private void addFragmentToBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fl_container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

}