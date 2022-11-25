package com.example.myapplication.statusinset;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityNavigationBarDemoBinding;

import java.util.Random;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NavigationBarDemoActivity extends BaseActivity<ActivityNavigationBarDemoBinding> {

    @Override
    protected int[] bindClick() {
        return new int[]{
                R.id.btn_show_behind_navigation_bar, R.id.btn_fit_window,
                R.id.btn_transparent_nav, R.id.btn_change_color, R.id.btn_set_nav_icon_color, R.id.btn_set_nav_icon_color_compat,
                R.id.btn_set_nav_icon_color_window_inset, R.id.btn_get_nav_bar_height, R.id.btn_get_nav_bar_height_compat,
                R.id.btn_get_nav_bar_height_default, R.id.btn_show_hide_nav_bar, R.id.btn_show_hide_nav_bar_inset,
                R.id.btn_show_hide_nav_bar_inset_compat, R.id.btn_immerse_nav_bar, R.id.btn_stick_immerse_nav_bar,
                R.id.btn_get_screen_height, R.id.btn_show_hide_status_bar
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_behind_navigation_bar:
                showContentBehindNavigationBar();
                break;
            case R.id.btn_fit_window:
                showContentFitWindow();
                break;
            case R.id.btn_transparent_nav:
                makeNavigationBarTransparent();
                break;
            case R.id.btn_change_color:
                changeNavigationBarColor();
                break;
            case R.id.btn_set_nav_icon_color:
                setNavigationBarIconColor();
                break;
            case R.id.btn_set_nav_icon_color_compat:
                setNavigationBarIconColorCompat();
                break;
            case R.id.btn_set_nav_icon_color_window_inset:
                setNavigationBarIconColorInset();
                break;
            case R.id.btn_get_nav_bar_height:
                getNavigationBarHeight();
                break;
            case R.id.btn_get_nav_bar_height_compat:
                getNavigationBarHeightCompat();
                break;
            case R.id.btn_get_nav_bar_height_default:
                getNavigationBarHeightDefault();
                break;
            case R.id.btn_show_hide_nav_bar:
                showHideNavigationBar();
                break;
            case R.id.btn_show_hide_nav_bar_inset:
                showHideNavigationBarInset();
                break;
            case R.id.btn_show_hide_nav_bar_inset_compat:
                showHideNavigationBarInsetCompat();
                break;
            case R.id.btn_immerse_nav_bar:
                makeNavigationBarImmerse();
                break;
            case R.id.btn_stick_immerse_nav_bar:
                makeNavigationBarStickImmerse();
                break;
            case R.id.btn_get_screen_height:
                getScreenHeight();
                break;
            case R.id.btn_show_hide_status_bar:
                showHideStatusBar();
                break;
        }
    }

    private void showHideStatusBar() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
        int statusBarType = WindowInsetsCompat.Type.statusBars();
        androidx.core.graphics.Insets statusBarInset = rootWindowInsets.getInsets(statusBarType);
        Log.i(TAG, "statusBarInset=" + statusBarInset.bottom + " " + statusBarInset.top);
        if (statusBarInset.top > 0) {
            insetsController.hide(statusBarType);
        } else {
            insetsController.show(statusBarType);
        }
    }

    private void getScreenHeight() {


        // 1. 全面屏下 --> getMetrics + NavigationBarHeight + StatusBarHeight = getRealMetrics   （注意：即使在代码中隐藏了状态栏 & 导航栏，这里的 NavigationBarHeight 和 StatusBarHeight 都不会变为0， 值仍是之前的值）
        // 2. 全面屏下， 在系统设置中关闭导航栏 --> getMetrics + StatusBarHeight = getRealMetrics  （也就是说，只有在系统设置中关闭导航栏）
        // 3. 非全面屏下 --> getMetrics + NavigationBarHeight = getRealMetrics   （即使在代码中将导航栏隐藏，这里的NavigationBarHeight不会变为0，仍是隐藏起前的导航栏高度）
        // 4. 非全面下， 没有导航栏时 --> getMetrics = getRealMetrics

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Log.i(TAG, "getMetrics --> " + "height=" + height + "  width=" + width);

        DisplayMetrics realMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(realMetrics);
        int realHeight = realMetrics.heightPixels;
        int realWidth = realMetrics.widthPixels;
        Log.i(TAG, "getRealMetrics --> " + "height=" + realHeight + " width=" + realWidth);

        Log.i(TAG, "realHeight - height=" + (realHeight - height));
    }

    private void makeNavigationBarStickImmerse() {
        View decorView = getWindow().getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if ((flag & SYSTEM_UI_FLAG_HIDE_NAVIGATION) == SYSTEM_UI_FLAG_HIDE_NAVIGATION
                && (flag & SYSTEM_UI_FLAG_FULLSCREEN) == SYSTEM_UI_FLAG_FULLSCREEN
                && (flag & SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == SYSTEM_UI_FLAG_IMMERSIVE_STICKY) {
            // 取消沉浸式
            flag &= ~SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            flag &= ~SYSTEM_UI_FLAG_FULLSCREEN;
            flag &= ~SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        } else {
            flag |= SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            flag |= SYSTEM_UI_FLAG_FULLSCREEN;
            flag |= SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        decorView.setSystemUiVisibility(flag);
    }

    private void makeNavigationBarImmerse() {
        View decorView = getWindow().getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if ((flag & SYSTEM_UI_FLAG_HIDE_NAVIGATION) == SYSTEM_UI_FLAG_HIDE_NAVIGATION
                && (flag & SYSTEM_UI_FLAG_FULLSCREEN) == SYSTEM_UI_FLAG_FULLSCREEN
                && (flag & SYSTEM_UI_FLAG_IMMERSIVE) == SYSTEM_UI_FLAG_IMMERSIVE) {
            // 取消沉浸式
            flag &= ~SYSTEM_UI_FLAG_IMMERSIVE;
            flag &= ~SYSTEM_UI_FLAG_FULLSCREEN;
            flag &= ~SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        } else {
            flag |= SYSTEM_UI_FLAG_IMMERSIVE;
            flag |= SYSTEM_UI_FLAG_FULLSCREEN;
            flag |= SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        decorView.setSystemUiVisibility(flag);
    }

    private void showHideNavigationBarInsetCompat() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsCompat insets = ViewCompat.getRootWindowInsets(decorView);
        androidx.core.graphics.Insets navInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        Log.i(TAG, "showHideNavigationBarInsetCompat:" + navInsets.bottom);
        if (insetsController == null) {
            Log.i(TAG, "showHideNavigationBarInset insetsController == null");
            return;
        }
        if (navInsets.bottom > 0) {
            insetsController.hide(WindowInsetsCompat.Type.navigationBars());
        } else {
            insetsController.show(WindowInsetsCompat.Type.navigationBars());
        }
    }

    private void showHideNavigationBarInset() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        boolean visible = rootWindowInsets.isVisible(WindowInsets.Type.navigationBars());
        WindowInsetsController insetsController = window.getInsetsController();
        if (visible) {
            insetsController.hide(WindowInsets.Type.navigationBars());
        } else {
            insetsController.show(WindowInsets.Type.navigationBars());
        }
    }

    private void showHideNavigationBar() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();

        if ((flag & SYSTEM_UI_FLAG_FULLSCREEN) == SYSTEM_UI_FLAG_FULLSCREEN
                && (flag & SYSTEM_UI_FLAG_HIDE_NAVIGATION) == SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
            flag &= ~SYSTEM_UI_FLAG_FULLSCREEN;
            flag &= ~SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        } else {
            flag |= SYSTEM_UI_FLAG_FULLSCREEN
                    | SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        decorView.setSystemUiVisibility(flag);
    }

    private void getNavigationBarHeightDefault() {
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int navHeight = getResources().getDimensionPixelSize(resourceId);
            Log.i(TAG, "navigation_bar_height = " + navHeight);
        }
    }

    private void getNavigationBarHeightCompat() {
        View decorView = getWindow().getDecorView();
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
        androidx.core.graphics.Insets navInset = rootWindowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
        int height = Math.abs(navInset.bottom - navInset.top);
        Log.i(TAG, "navigatoin height: " + height);
    }

    private void getNavigationBarHeight() {
        View decorView = getWindow().getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        Insets navInset = rootWindowInsets.getInsets(WindowInsets.Type.navigationBars());
        int height = Math.abs(navInset.bottom - navInset.top);
        Log.i(TAG, "navigation bar height: " + height);
    }

    private void setNavigationBarIconColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        if ((flag & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) == View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) {
            flag &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            flag |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        decorView.setSystemUiVisibility(flag);
    }

    private void setNavigationBarIconColorCompat() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        if (insetsController == null) {
            Log.i(TAG, "setNavigationBarIconColorCompat -> insetsController == null");
            return;
        }

        boolean appearanceLightNavigationBars = insetsController.isAppearanceLightNavigationBars();
        insetsController.setAppearanceLightNavigationBars(!appearanceLightNavigationBars);
    }

    private void setNavigationBarIconColorInset() {
        Window window = getWindow();
        WindowInsetsController insetsController = window.getInsetsController();
        if (insetsController == null) {
            Log.i(TAG, "setNavigationBarIconColorInset -> insetsController == null");
            return;
        }
        int systemBarsAppearance = insetsController.getSystemBarsAppearance();
        if (systemBarsAppearance == WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS) {
            insetsController.setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        } else {
            insetsController.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }
    }

    private void changeNavigationBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.setNavigationBarContrastEnforced(false);
        }
        int[] colors = new int[]{
                Color.RED,
                Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.CYAN,
                Color.MAGENTA,
                Color.BLACK,
                Color.GRAY,
                Color.DKGRAY,
                Color.LTGRAY,
                Color.WHITE,
        };
        Random random = new Random();
        int index = random.nextInt(colors.length);
        window.setNavigationBarColor(colors[index]);
    }

    private void makeNavigationBarTransparent() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.setNavigationBarContrastEnforced(false);
        }
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    private void showContentFitWindow() {
        boolean fitsSystemWindows = binding.getRoot().getFitsSystemWindows();
        binding.getRoot().setFitsSystemWindows(!fitsSystemWindows);
    }

    private void showContentBehindNavigationBar() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int flag = decorView.getSystemUiVisibility();
        flag |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(flag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


    }
}