package com.example.myapplication.statusinset;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
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
                R.id.btn_transparent_nav, R.id.btn_change_color, R.id.btn_set_nav_icon_color, R.id.btn_set_nav_icon_color_compat
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
        }
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
        boolean appearanceLightNavigationBars = insetsController.isAppearanceLightNavigationBars();
        insetsController.setAppearanceLightNavigationBars(!appearanceLightNavigationBars);
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