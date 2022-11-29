package com.example.myapplication.statusinset;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
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

        View decorView = window.getDecorView();
        decorView.setBackgroundColor(Color.RED);

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int lastHeight = 0;

            @Override
            public void onGlobalLayout() {
                View decorView = getWindow().getDecorView();
                WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
                Insets insets = rootWindowInsets.getInsets(WindowInsetsCompat.Type.ime());
                int imeHeight = insets.bottom;
                if (imeHeight != lastHeight) {
                    lastHeight = imeHeight;
                    ActionBar supportActionBar = getSupportActionBar();
                    int height = 0;
                    if (supportActionBar != null) {
                        height = supportActionBar.getHeight();
                    }

                    Log.i(TAG, "addOnGlobalLayoutListener  imeHeight=" + imeHeight + " supportActionBar=" + height);
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imageView1.getLayoutParams();
                    if (imeHeight > 0) {
                        // 展开了软键盘
                        layoutParams.topMargin = imeHeight - height;
                        binding.imageView1.setLayoutParams(layoutParams);
                        int finalHeight = height;
                        binding.imageView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    binding.imageView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                                // 在imageView1应用了topMargin后，滚动布局需要向下滚动相应距离
                                binding.nestScrollView.scrollBy(0, imeHeight - finalHeight);
                            }
                        });
                    } else {
                        // 关闭了软键盘
                        layoutParams.topMargin = 0;
                        binding.imageView1.setLayoutParams(layoutParams);

                    }

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