package com.example.myapplication.statusinset;


import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityStatusBarDemoBinding;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class StatusBarDemoActivity extends BaseActivity<ActivityStatusBarDemoBinding> {

    private int originOptions;
    private int originStatusBarColor;
    private int originFlags;

    @Override
    protected int[] bindClick() {
        return new int[]{
                R.id.btn_show_behind, R.id.btn_transparent_status_bar, R.id.btn_reset,
                R.id.btn_window_inset, R.id.btn_status_bar_dark_light_change, R.id.btn_window_inset_compat,
                R.id.btn_status_bar_dark_light_change_compat, R.id.btn_window_inset_compat_old, R.id.btn_status_bar_dark_light_change_ole_way,
                R.id.btn_show_dialog

        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_behind:
                showBehindStatusBar();
                break;
            case R.id.btn_transparent_status_bar:
                makeStatusBarTransparent();
                break;
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_window_inset:
                getWindowInsetHeight();
                break;
            case R.id.btn_status_bar_dark_light_change:
                changeStatusBarDarkLight();
                break;
            case R.id.btn_window_inset_compat:
                getWindowInsetHeightCompat();
                break;
            case R.id.btn_status_bar_dark_light_change_compat:
                changeStatusBarDarkLightCompat();
                break;
            case R.id.btn_window_inset_compat_old:
                getWindowInsetHeightOldWay();
                break;
            case R.id.btn_status_bar_dark_light_change_ole_way:
                changeStatusBarDarkLightOld();
                break;
            case R.id.btn_show_dialog:
                showAlertDialog();
                break;
        }
    }


    /**
     * 布局内容显示在状态栏后方
     */
    private void showBehindStatusBar() {
        binding.getRoot().setFitsSystemWindows(true);
        View decorView = getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(options);
    }

    /**
     * 状态栏透明
     */
    private void makeStatusBarTransparent() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * Android 11 及以上版本使用WindowInset获取状态栏、导航栏数据
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void getWindowInsetHeight() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        Insets statusBar = rootWindowInsets.getInsets(WindowInsets.Type.statusBars());
        int bottom = statusBar.bottom;
        int top = statusBar.top;
        int left = statusBar.left;
        int right = statusBar.right;
        Log.i(TAG, "setWindowInsetListener -- status bar top=" + top + "  bottom=" + bottom + " left=" + left + " right=" + right);
        Log.i(TAG, "setWindowInsetListener -- height=" + (bottom - top));

        Insets navigationBar = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
        int nav_bottom = navigationBar.bottom;
        int nav_top = navigationBar.top;
        int nav_left = navigationBar.left;
        int nav_right = navigationBar.right;
        Log.i(TAG, "setOnApplyWindowInsetsListener -- navigation bar top=" + nav_top + "  bottom=" + nav_bottom + " left=" + nav_left + " right=" + nav_right);
        Log.i(TAG, "setOnApplyWindowInsetsListener -- height=" + (nav_bottom - nav_top));
    }


    /**
     * Android 11 以下版本使用WindowInset获取状态栏、导航栏数据
     */
    private void getWindowInsetHeightCompat() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
        androidx.core.graphics.Insets statusBar = rootWindowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
        int top = statusBar.top;
        int bottom = statusBar.bottom;
        Log.i(TAG, "setOnApplyWindowInsetsListener -- navigation bar top=" + top + "  bottom=" + bottom);
        Log.i(TAG, "setOnApplyWindowInsetsListener -- height=" + (bottom - top));


        androidx.core.graphics.Insets navigationBar = rootWindowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
        int nav_top = navigationBar.top;
        int nav_bottom = navigationBar.bottom;

        Log.i(TAG, "setOnApplyWindowInsetsListener -- navigation bar top=" + nav_top + " bottom=" + nav_bottom);
        Log.i(TAG, "setOnApplyWindowInsetsListener -- height=" + (nav_bottom - nav_top));
    }


    private void getWindowInsetHeightOldWay() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG, "getWindowInsetHeightOldWay -- statusBarHeight=" + statusBarHeight);


        int navigationBarHeight = 0;
        int resourceId1 = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId1 > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId1);
        }
        Log.i(TAG, "getWindowInsetHeightOldWay -- navigationBarHeight=" + navigationBarHeight);
    }


    /**
     * 状态栏深色 & 浅色
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void changeStatusBarDarkLight() {
        Window window = getWindow();
        WindowInsetsController insetsController = window.getInsetsController();
        if (insetsController.getSystemBarsAppearance() == APPEARANCE_LIGHT_STATUS_BARS) {
            insetsController.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            insetsController.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
    }

    private void changeStatusBarDarkLightCompat() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(decorView);
        if (windowInsetsController != null) {
            boolean appearanceLightStatusBars = windowInsetsController.isAppearanceLightStatusBars();
            windowInsetsController.setAppearanceLightStatusBars(!appearanceLightStatusBars);
        }
    }

    private void changeStatusBarDarkLightOld() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        int option = flag;
        if ((flag & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
            option &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            option |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        decorView.setSystemUiVisibility(option);
    }


    private void showAlertDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
//        builder.setCancelable(true);
//        builder.setView(R.layout.dialog_round_alert);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//            @Override
//            public void onShow(DialogInterface dialog) {
//                Window window = alertDialog.getWindow();
//                if (window != null) {
//                    View decorView = window.getDecorView();
//                    if (decorView != null) {
//                        int flag = decorView.getSystemUiVisibility();
//                        flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//                        decorView.setSystemUiVisibility(flag);
//                    }
//                }
//            }
//        });
//        alertDialog.show();

//        MyAlertDialog dialog = new MyAlertDialog();
//        dialog.show(getSupportFragmentManager(), null);


//        MyDialog dialog = new MyDialog(this, R.style.AlertDialogFullWidthTheme);
//        dialog.show();

        MyAlertDialog1 dialog = new MyAlertDialog1(this, R.style.AlertDialogFullWidthTheme);
        dialog.show();


    }


    private void reset() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(originOptions);
        window.setStatusBarColor(originStatusBarColor);

        WindowManager.LayoutParams attributes = window.getAttributes();
        window.clearFlags(attributes.flags);
        window.addFlags(originFlags);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 隐藏ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 2. 沉浸式
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 获取原始设置
        Window window = getWindow();
        originOptions = window.getDecorView().getSystemUiVisibility();
        originStatusBarColor = window.getStatusBarColor();
        WindowManager.LayoutParams attributes = window.getAttributes();
        originFlags = attributes.flags;

        Log.i(TAG, "originOptions=" + originOptions +
                "\noriginStatusBarColor=" + originStatusBarColor +
                "\n");


        // 获取状态栏、导航栏的高度
//        window.getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
//            @Override
//            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
//                Insets statusBar = insets.getInsets(WindowInsets.Type.statusBars());
//                int bottom = statusBar.bottom;
//                int top = statusBar.top;
//                int left = statusBar.left;
//                int right = statusBar.right;
//                Log.i(TAG, "setOnApplyWindowInsetsListener -- status bar top=" + top + "  bottom=" + bottom + " left=" + left + " right=" + right);
//                Log.i(TAG, "setOnApplyWindowInsetsListener -- height=" + (bottom - top));
//
//
//                Insets navigationBar = insets.getInsets(WindowInsets.Type.navigationBars());
//                int nav_bottom = navigationBar.bottom;
//                int nav_top = navigationBar.top;
//                int nav_left = navigationBar.left;
//                int nav_right = navigationBar.right;
//                Log.i(TAG, "setOnApplyWindowInsetsListener -- navigation bar top=" + nav_top + "  bottom=" + nav_bottom + " left=" + nav_left + " right=" + nav_right);
//                Log.i(TAG, "setOnApplyWindowInsetsListener -- height=" + (nav_bottom - nav_top));
//                return null;
//            }
//        });
    }
}