package com.example.myapplication.statusinset;


import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

import static androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP;

import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityStatusBarDemoBinding;

import java.util.List;


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
                R.id.btn_show_dialog, R.id.btn_hide_status_bar_old_way, R.id.btn_hide_action_bar,
                R.id.btn_hide_status_bar, R.id.btn_hide_status_bar_compat, R.id.btn_immerse_old_way,
                R.id.btn_immerse_sticky_old_way, R.id.btn_traverse_decorview, R.id.btn_hide_status_bar_with_window_flag,
                R.id.btn_cutout_setting
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
            case R.id.btn_hide_status_bar_old_way:
                showHideStatusBarOldWay();
                break;
            case R.id.btn_hide_action_bar:
                showHideActionBar();
                break;
            case R.id.btn_hide_status_bar:
                showHideStatusBar();
                break;
            case R.id.btn_hide_status_bar_compat:
                showHideStatusBarCompat();
                break;
            case R.id.btn_immerse_old_way:
                showImmerseOldWay();
                break;
            case R.id.btn_immerse_sticky_old_way:
                showImmerseStickOldWay();
                break;
            case R.id.btn_traverse_decorview:
                showTraverseDecorView();
                break;
            case R.id.btn_hide_status_bar_with_window_flag:
                showHideStatusBarWithWindowFlag();
                break;
            case R.id.btn_cutout_setting:
                showCutoutSetting();
                break;
        }
    }


    /**
     * 遍历DecorView的所有子元素
     */
    private void showTraverseDecorView() {
        Window window = getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        for (int i = 0; i < decorView.getChildCount(); i++) {
            View child = decorView.getChildAt(i);
            int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = getResources().getResourceEntryName(id);
                Log.i(TAG, "resourceEntryName == " + resourceEntryName + " isVisible=" + (child.getVisibility() == View.VISIBLE));
            }
        }
    }

    /**
     * cutout
     */
    private void showCutoutSetting() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
        if (rootWindowInsets == null) {
            Log.i(TAG, "showCutoutSetting --> rootWindowInsets");
            return;
        }
        DisplayCutoutCompat displayCutout = rootWindowInsets.getDisplayCutout();

        if (displayCutout == null) {
            Log.i(TAG, "showCutoutSetting --> displayCutout==null");
            return;
        }

        // 获取显示器上非功能区域的边界矩形
        List<Rect> boundingRects = displayCutout.getBoundingRects();

        int safeInsetBottom = displayCutout.getSafeInsetBottom();
        int safeInsetLeft = displayCutout.getSafeInsetLeft();
        int safeInsetRight = displayCutout.getSafeInsetRight();
        int safeInsetTop = displayCutout.getSafeInsetTop();
        androidx.core.graphics.Insets waterfallInsets = displayCutout.getWaterfallInsets();

        // 安全区域距离屏幕下边的距离
        Log.i(TAG, "safeInsetBottom=" + safeInsetBottom);

        // 安全区域距离屏幕左边的距离
        Log.i(TAG, "safeInsetLeft=" + safeInsetLeft);

        // 安全区域距离屏幕右边的距离
        Log.i(TAG, "safeInsetRight=" + safeInsetRight);

        // 安全区域距离屏幕上边的距离
        Log.i(TAG, "safeInsetTop=" + safeInsetTop);

        Log.i(TAG, String.format("waterfallInsets=(%d, %d, %d, %d)", waterfallInsets.left, waterfallInsets.top, waterfallInsets.right, waterfallInsets.bottom));

        for (Rect boundingRect : boundingRects) {
            Log.i(TAG, "boundingRect = " + boundingRect);
        }

        Rect rect = new Rect();
        binding.getRoot().getWindowVisibleDisplayFrame(rect);
        Log.i(TAG, "rootView Rect=" + rect);

        Rect rect1 = new Rect();
        decorView.getWindowVisibleDisplayFrame(rect1);
        Log.i(TAG, "decorView Rect=" + rect1);


        // LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT- 使用此默认设置，内容在纵向模式下显示时会呈现到剪切区域中，但在横向模式下显示时内容会变成 letterbox。
        // LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES- 内容以纵向和横向模式呈现到切口区域。
        // LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER- 内容永远不会呈现到剪切区域中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(attributes);
        }
    }


    /**
     * 使用 Window Flag 来显示、隐藏状态栏
     */
    private void showHideStatusBarWithWindowFlag() {
        Window window = getWindow();
        int flags = window.getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    /**
     * Stick 沉浸式
     */
    private void showImmerseStickOldWay() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        flag |= SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flag);
    }

    /**
     * 普通沉浸式
     */
    private void showImmerseOldWay() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        flag |= SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(flag);
    }


    boolean isShowStatusBar = true;

    /**
     * Android11以上使用WindowInsetController来控制状态栏的显示和隐藏
     */
    private void showHideStatusBar() {
        Window window = getWindow();
        WindowInsetsController insetsController = window.getInsetsController();
        if (isShowStatusBar) {
            isShowStatusBar = false;
            insetsController.hide(WindowInsets.Type.statusBars());
        } else {
            isShowStatusBar = true;
            insetsController.show(WindowInsets.Type.statusBars());
        }
    }

    /**
     * Android11以上使用WindowInsetsControllerCompat来控制状态栏的隐藏和显示
     */
    private void showHideStatusBarCompat() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        if (isShowStatusBar) {
            isShowStatusBar = false;
            insetsController.hide(WindowInsetsCompat.Type.statusBars());
        } else {
            isShowStatusBar = true;
            insetsController.show(WindowInsetsCompat.Type.statusBars());
        }
    }

    /**
     * 使用SystemUiVisibility来 显示或隐藏状态栏
     */
    private void showHideStatusBarOldWay() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if ((flag & SYSTEM_UI_FLAG_FULLSCREEN) == SYSTEM_UI_FLAG_FULLSCREEN) {
            flag &= ~SYSTEM_UI_FLAG_FULLSCREEN;
            isShowStatusBar = false;
        } else {
            flag |= SYSTEM_UI_FLAG_FULLSCREEN;
            isShowStatusBar = true;
        }
        decorView.setSystemUiVisibility(flag);
    }


    /**
     * 使用SystemUIVisibility 布局内容显示在状态栏后方
     */
    private void showBehindStatusBar() {
        binding.getRoot().setFitsSystemWindows(true);
        View decorView = getWindow().getDecorView();
        int flag = decorView.getSystemUiVisibility();

        int options = flag | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(options);
    }

    /**
     * 使用 WindowFlag 状态栏透明
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


    /**
     * 使用预设值来获取状态栏高度 & 导航栏高度
     */
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
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(window, decorView);
        boolean appearanceLightStatusBars = windowInsetsController.isAppearanceLightStatusBars();
        windowInsetsController.setAppearanceLightStatusBars(!appearanceLightStatusBars);
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
        MyAlertDialog1 dialog = new MyAlertDialog1(this, R.style.AlertDialogFullWidthTheme);
        dialog.show();
    }

    private void showHideActionBar() {
        if (getSupportActionBar() != null) {
            boolean showing = getSupportActionBar().isShowing();
            if (showing) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
        }
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
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }

        // 获取原始设置
        Window window = getWindow();
        originOptions = window.getDecorView().getSystemUiVisibility();
        originStatusBarColor = window.getStatusBarColor();
        WindowManager.LayoutParams attributes = window.getAttributes();
        originFlags = attributes.flags;

        Log.i(TAG, "originOptions=" + originOptions +
                "\noriginStatusBarColor=" + originStatusBarColor +
                "\n");


        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 软键盘监听 (只对Android 11及以上生效)
            ViewCompat.setWindowInsetsAnimationCallback(decorView, new WindowInsetsAnimationCompat.Callback(WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP) {
                @NonNull
                @Override
                public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                    androidx.core.graphics.Insets imeInset = insets.getInsets(WindowInsetsCompat.Type.ime());
                    Log.i(TAG, String.format("onProgress (%d, %d, %d, %d)", imeInset.left, imeInset.top, imeInset.right, imeInset.bottom));
                    return insets;
                }
            });
        } else {
            // Android10及以下的设备就只能监听开头和结尾了。
            ViewCompat.setOnApplyWindowInsetsListener(decorView, new OnApplyWindowInsetsListener() {
                @NonNull
                @Override
                public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                    androidx.core.graphics.Insets imeInset = insets.getInsets(WindowInsetsCompat.Type.ime());
                    Log.i(TAG, String.format("(%d, %d, %d, %d)", imeInset.left, imeInset.top, imeInset.right, imeInset.bottom));
                    return insets;
                }
            });
        }


    }
}