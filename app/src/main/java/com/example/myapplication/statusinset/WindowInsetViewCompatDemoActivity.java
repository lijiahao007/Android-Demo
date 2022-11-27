package com.example.myapplication.statusinset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityWindowInsetViewCompatDemoBinding;

public class WindowInsetViewCompatDemoActivity extends BaseActivity<ActivityWindowInsetViewCompatDemoBinding> {

    @Override
    protected int[] bindClick() {
        return new int[]{R.id.btn_apply_window_inset, R.id.btn_show_hide_status_bar, R.id.btn_show_hide_nav_bar,
                R.id.btn_show_hide_ime, R.id.btn_show_hide_ime_new, R.id.btn_soft_input_state_mode
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_apply_window_inset:
                applyWindowInset();
                break;
            case R.id.btn_show_hide_status_bar:
                showHideStatusBar();
                break;
            case R.id.btn_show_hide_nav_bar:
                showHideNavBar();
                break;
            case R.id.btn_show_hide_ime:
                showHideIme();
                break;
            case R.id.btn_show_hide_ime_new:
                showHideImeNew();
                break;
            case R.id.btn_soft_input_state_mode:
                startActivity(new Intent(this, SoftInputStateActivity.class));
                break;
        }
    }


    private void showHideImeNew() {
        Insets imeInsets = getImeInsets();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        int imeType = WindowInsetsCompat.Type.ime();
        if (imeInsets != null && imeInsets.bottom > 0) {
            // 关闭软键盘
            insetsController.hide(imeType);
        } else {
            // 打开软键盘
            binding.editText.setFocusable(true);
            binding.editText.setFocusableInTouchMode(true);
            binding.editText.requestFocus();
            insetsController.show(imeType);
        }
    }

    private void showHideIme() {
        Insets imeInsets = getImeInsets();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imeInsets != null && imeInsets.bottom > 0) {
            // 关闭软键盘
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            // 打开软键盘
            binding.editText.setFocusable(true);
            binding.editText.setFocusableInTouchMode(true);
            binding.editText.requestFocus();
            imm.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    private void showHideNavBar() {
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        Insets systemBarInsets = getSystemBarInsets();
        int statusType = WindowInsetsCompat.Type.navigationBars();
        if (systemBarInsets != null && systemBarInsets.bottom > 0) {
            Log.i(TAG, "hideNavBar");
            insetsController.hide(statusType);
        } else {
            Log.i(TAG, "showNavBar" + systemBarInsets);
            insetsController.show(statusType);
        }
    }

    private void showHideStatusBar() {
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        Insets systemBarInsets = getSystemBarInsets();
        int statusType = WindowInsetsCompat.Type.statusBars();
        if (systemBarInsets != null && systemBarInsets.top > 0) {
            Log.i(TAG, "hideStatusBar");
            insetsController.hide(statusType);
        } else {
            Log.i(TAG, "showStatusBar" + systemBarInsets);
            insetsController.show(statusType);
        }
    }

    private Insets getSystemBarInsets() {
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(getWindow().getDecorView());
        if (rootWindowInsets == null) {
            return null;
        }
        return rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
    }

    private Insets getImeInsets() {
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(getWindow().getDecorView());
        if (rootWindowInsets == null) {
            return null;
        }
        return rootWindowInsets.getInsets(WindowInsetsCompat.Type.ime());
    }

    private void applyWindowInset() {
        Log.i(TAG, "applyWindowInset");
        View decorView = getWindow().getDecorView();
        ViewCompat.setOnApplyWindowInsetsListener(decorView, new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                Log.i(TAG, "onApplyWindowInsets");
                Insets systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                Insets imeInset = insets.getInsets(WindowInsetsCompat.Type.ime());
                Log.i(TAG, "status=" + systemBarInset.top + "; navigation=" + systemBarInset.bottom + "; imt=" + imeInset.bottom);
                return insets;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}