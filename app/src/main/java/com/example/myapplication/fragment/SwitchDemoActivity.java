package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySwitchDemoBinding;

public class SwitchDemoActivity extends BaseActivity<ActivitySwitchDemoBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment(Demo1Fragment.newInstance());
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fl_container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.i(TAG, "backStackEntryCount=[" + backStackEntryCount + "]");
        if (backStackEntryCount != 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }

    }
}