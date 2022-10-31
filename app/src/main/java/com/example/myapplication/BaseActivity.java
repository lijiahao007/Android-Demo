package com.example.myapplication;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.myapplication.tablayout.LifecycleLogObserver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected T binding;
    protected final String TAG = getClass().getSimpleName();
    private Toast toast;
    protected final Handler mBaseActivityHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new LifecycleLogObserver(TAG));

        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedSuperclass = (ParameterizedType) genericSuperclass;
        assert parameterizedSuperclass != null;
        Type[] actualTypeArguments = parameterizedSuperclass.getActualTypeArguments();
        Type viewBindingType = actualTypeArguments[0];
        Class viewBindingClass = (Class) viewBindingType;
        try {
            Method inflate = viewBindingClass.getMethod("inflate", LayoutInflater.class);
            binding = (T) inflate.invoke(null, getLayoutInflater());
            assert binding != null;
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void showToast(String msg) {
        if (toast != null) {
            toast.cancel();
        }

        toast = new Toast(this);
        View toastView = getLayoutInflater().inflate(R.layout.toast, null);
        toast.setView(toastView);
        TextView tvToast = toastView.findViewById(R.id.tv_toast);
        tvToast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "isSystemDarkUiMode:" + isSystemDarkUiMode(this));
        Log.i(TAG, "isAppDarkUiMode:" + isAppDarkUiMode(this));
    }

    /**
     * 判断系统当前是否深色模式
     *
     * @param context
     * @return
     */
    public static boolean isSystemDarkUiMode(Context context) {
        int uiMode = context.getResources().getConfiguration().uiMode;
        return (uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * 判断当前APP是否深色模式
     *
     * @param context
     * @return
     */
    public static boolean isAppDarkUiMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
}
