package com.example.myapplication.statusinset;

import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
import static androidx.core.view.WindowInsetsAnimationCompat.Callback.DISPATCH_MODE_STOP;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsAnimationCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySoftImeBinding;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SoftImeActivity extends BaseActivity<ActivitySoftImeBinding> {
    @Override
    protected int[] bindClick() {
        return new int[]{
                R.id.btn_window_inset_listen, R.id.btn_show_hide_status_bar,
                R.id.btn_show_hide_navigation_bar, R.id.btn_global_layout_get_ime_height, R.id.btn_window_inset_android11
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_window_inset_listen:
                windowInsetCompatListenIme();
                break;
            case R.id.btn_show_hide_status_bar:
                showHideStatusBar();
                break;
            case R.id.btn_show_hide_navigation_bar:
                showHideNavigationBar();
                break;
            case R.id.btn_global_layout_get_ime_height:
                getImeHeightOnGlobalLayoutChange();
                break;
            case R.id.btn_window_inset_android11:
                windowInsetCompatAndroid11();
                break;
        }
    }

    private void windowInsetCompatAndroid11() {
        Log.i(TAG, "windowInsetCompatAndroid11");
        Window window = getWindow();
        View decorView = window.getDecorView();
        ViewCompat.setWindowInsetsAnimationCallback(decorView, new WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
            @Override
            public void onPrepare(@NonNull WindowInsetsAnimationCompat animation) {
                super.onPrepare(animation);
                Log.i(TAG, "onPrepare");
            }


            @NonNull
            @Override
            public WindowInsetsAnimationCompat.BoundsCompat onStart(@NonNull WindowInsetsAnimationCompat animation, @NonNull WindowInsetsAnimationCompat.BoundsCompat bounds) {
                Log.i(TAG, "onStart");
                return super.onStart(animation, bounds);
            }

            @NonNull
            @Override
            public WindowInsetsCompat onProgress(@NonNull WindowInsetsCompat insets, @NonNull List<WindowInsetsAnimationCompat> runningAnimations) {
                float interpolatedFraction = 0;
                Interpolator interpolator = null;
                for (WindowInsetsAnimationCompat animation : runningAnimations) {
                    if (animation.getTypeMask() == WindowInsetsCompat.Type.ime()) {
                        interpolatedFraction = animation.getInterpolatedFraction();
                        interpolator = animation.getInterpolator();
                    }
                }

                Insets imeInset = insets.getInsets(WindowInsetsCompat.Type.ime());
                int imeHeight = imeInset.bottom;
                Log.i(TAG, "imeHeight=" + imeHeight + "  interpolator=" + interpolator + "  interpolatedFraction=" + interpolatedFraction);
                return insets;
            }


            @Override
            public void onEnd(@NonNull WindowInsetsAnimationCompat animation) {
                Log.i(TAG, "onEnd");
                super.onEnd(animation);
            }
        });
    }

    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private void getImeHeightOnGlobalLayoutChange() {
        Log.i(TAG, "getImeHeightOnGlobalLayoutChange");
        View decorView = getWindow().getDecorView();
        ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        if (onGlobalLayoutListener != null) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View decorView = getWindow().getDecorView();
                int top = decorView.getTop();
                int bottom = decorView.getBottom();
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                Log.i(TAG, "real top:" + top);
                Log.i(TAG, "real bottom:" + bottom);
                Log.i(TAG, "visible rect:" + rect);
                Log.i(TAG, "ime height:" + (bottom - rect.bottom));

            }
        };
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private void showHideNavigationBar() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
        Insets systemBarInset = rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

        int navigationBarHeight = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            navigationBarHeight = systemBarInset.bottom;
        } else {
            navigationBarHeight = Math.max(systemBarInset.right, systemBarInset.left);
        }


        if (navigationBarHeight > 0) {
            insetsController.hide(WindowInsetsCompat.Type.navigationBars());
        } else {
            insetsController.show(WindowInsetsCompat.Type.navigationBars());
        }
    }

    private void showHideStatusBar() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat insetsController = WindowCompat.getInsetsController(window, decorView);
        WindowInsetsCompat rootWindowInsets = ViewCompat.getRootWindowInsets(decorView);
        Insets systemBarInset = rootWindowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
        if (systemBarInset.top > 0) {
            insetsController.hide(WindowInsetsCompat.Type.statusBars());
        } else {
            insetsController.show(WindowInsetsCompat.Type.statusBars());
        }
    }

    private void windowInsetCompatListenIme() {
        Log.i(TAG, "windowInsetCompatListenIme");
        View decorView = getWindow().getDecorView();
        ViewCompat.setOnApplyWindowInsetsListener(decorView, new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
                Log.i(TAG, "onApplyWindowInsets");
                Insets insets1 = insets.getInsets(WindowInsetsCompat.Type.ime());
                int bottom = insets1.bottom;
                Log.i(TAG, "onApplyWindowInsets --> ime height=" + bottom);
                return insets;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(attributes);
        }

    }
}