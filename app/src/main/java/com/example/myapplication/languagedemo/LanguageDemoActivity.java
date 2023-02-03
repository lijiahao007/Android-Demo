package com.example.myapplication.languagedemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLanguageDemoBinding;

public class LanguageDemoActivity extends BaseActivity<ActivityLanguageDemoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnChangeLanguage.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isArab = sharedPreferences.getBoolean("isArab", false);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("isArab", !isArab);
            edit.apply();
            recreate();
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isArab = sharedPreferences.getBoolean("isArab", false);
        binding.btnChangeLanguage.setText("切换语言（" + isArab + ")");
        binding.text1.setText(getString(R.string.str_viewing, "helloworld"));
        binding.text2.setText(getString(R.string.str_album_share_image, 10));

        binding.tvHover.setFocusable(true);
        binding.tvHover.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                Log.i(TAG, "Hover 文本  action=[" + event.getAction() + "]");
                return false;
            }
        });


//        binding.tvHover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "Click文本");
//            }
//        });

    }
}