package com.example.myapplication.statusinset;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivitySoftInputAdjustBinding;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SoftInputAdjustScrollActivity extends BaseActivity<ActivitySoftInputAdjustBinding> {

    @Override
    protected int[] bindClick() {
        return new int[]{

        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        window.setAttributes(attributes);

        binding.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int id = v.getId();
                String resourceEntryName = getResources().getResourceEntryName(id);
                String resourceName = getResources().getResourceName(id);
                String resourcePackageName = getResources().getResourcePackageName(id);
                String resourceTypeName = getResources().getResourceTypeName(id);
                Log.i(TAG, "onFocusChange" + hasFocus + " " + resourceEntryName + " " + resourceName + " " + resourcePackageName + " " + resourceTypeName);
            }
        });

        binding.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                String resourceEntryName = getResources().getResourceEntryName(id);
                Log.i(TAG, "onClick " + resourceEntryName);

            }
        });

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(TAG, "onGlobalLayout");
                WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(binding.getRoot());
                Insets insets = rootWindowInsets.getInsets(WindowInsetsCompat.Type.ime());
                if (insets.bottom > 0) {
                    Log.i(TAG, "onGlobalLayout -- 弹起软键盘");
                } else {
                    Log.i(TAG, "onGlobalLayout -- 隐藏软键盘");
                }
            }
        });


    }

    private void traverseViewGroup(ViewGroup viewGroup, String tag) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            int id = childView.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = getResources().getResourceEntryName(id);
                Log.i(TAG, tag + " -- " + resourceEntryName);
            }
        }
    }
}