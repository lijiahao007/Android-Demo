package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.unsplashproject.onlyokhttp.utls.DataBaseManager;
import com.macrovideo.sdk.SDKHelper;
import com.tencent.mmkv.MMKV;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    static final String TAG = "MyApplication";
    public ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        SDKHelper.initPhoneType(10);
        MMKV.initialize(this);
        DataBaseManager.initDataBaseManager(getApplicationContext());

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(updateResources(base, getCurLocale(base)));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateResources(getApplicationContext(), getCurLocale(getApplicationContext()));
    }

    public static Locale getCurLocale(Context context ) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isArab = defaultSharedPreferences.getBoolean("isArab", false);
        if (isArab) {
            return new Locale("ar", "", "阿拉伯语");
        } else {
            return new Locale("en", "", "英语");
        }
    }

    public static Context updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }
}
