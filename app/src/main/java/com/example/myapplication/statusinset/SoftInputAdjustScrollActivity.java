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

        ViewGroup decorView = (ViewGroup) window.getDecorView();
        decorView.setBackgroundColor(Color.RED);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        View actionBarContainer = decorView.findViewById(androidx.appcompat.R.id.action_bar_container);

        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int lastHeight = 0;

            @Override
            public void onGlobalLayout() {
                WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
                if (rootWindowInsets == null) {
                    return;
                }
                Insets insets = rootWindowInsets.getInsets(WindowInsetsCompat.Type.ime());
                int imeHeight = insets.bottom; // 软件盘 + 导航栏的高度
                int statusHeight = insets.top; // 状态栏高度
                Log.i(TAG, "imeHeight=" + imeHeight + "statusHeight=" + statusHeight);

                if (imeHeight != lastHeight) {

                    // 1. 设置状态值
                    lastHeight = imeHeight;

                    // 2. 计算EditText在屏幕中 距离底部的距离
                    int[] location = new int[2];
                    binding.editText.getLocationOnScreen(location);
                    int viewBottom = location[1] + binding.editText.getHeight();
                    int bottom = 0;
                    if (viewBottom <= screenHeight) {
                        bottom = screenHeight - (location[1] + binding.editText.getHeight());
                    }


                    // 3. 计算布局向上移动的距离
                    int offset = imeHeight - bottom;

                    if (imeHeight > 0) {
                        // 打开软件盘
                        if (offset > 0) {
                            // EditText在软件盘下方

                            // 1. 设置ActionBar TopMargin
                            if (actionBarContainer != null) {
                                mBaseActivityHandler.postDelayed(() -> {
                                    ActionBarOverlayLayout.LayoutParams layoutParams = (ActionBarOverlayLayout.LayoutParams) actionBarContainer.getLayoutParams();
                                    layoutParams.topMargin = offset + statusHeight;
                                    actionBarContainer.setLayoutParams(layoutParams);
                                    Log.i(TAG, "offset + statusHeight=" + (offset + statusHeight));
                                }, 200);
                            }

                            // 2. 设置内容的TopMargin
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imageView1.getLayoutParams();
                            layoutParams.topMargin = offset;
                            binding.imageView1.setLayoutParams(layoutParams);

                            binding.imageView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    binding.nestScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    if (binding.nestScrollView.getMeasuredHeight() > 0) {
                                        binding.nestScrollView.scrollBy(0, offset);
                                    }
                                }
                            });

                        }
                    } else {
                        if (actionBarContainer != null) {
                            ActionBarOverlayLayout.LayoutParams layoutParams = (ActionBarOverlayLayout.LayoutParams) actionBarContainer.getLayoutParams();
                            layoutParams.topMargin = 0;
                            actionBarContainer.setLayoutParams(layoutParams);
                        }

                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.imageView1.getLayoutParams();
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