package com.example.myapplication.recycleview;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityUsplashPhotoListBinding;
import com.example.myapplication.recycleview.entity.UnsplashPhoto;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.macrovideo.sdk.tools.LogUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsplashPhotoListActivity extends BaseActivity<ActivityUsplashPhotoListBinding> {

    private static final String UNSPLASH_ACCESS_TOKEN = "wMFwQhlS6JzfPU8SJijqMG9gvpTdcpYOWG5xGuZE8UA";
    private static final String UNSPLASH_SECRET_TOKEN = "PS1xSEgvY8TTpGqtDaEo5Ueww467FCI5BnAUsPVvCFk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        HttpUrl baseUrl = HttpUrl.parse("https://api.unsplash.com/photos/random");
        Log.e(TAG, "baseUrl == " + baseUrl);

        if (baseUrl == null) {
            return;
        }

        HttpUrl url = baseUrl.newBuilder()
//                .addQueryParameter("count", "1")
                .build();
        Log.e(TAG, "url = " + url);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Client-ID " + UNSPLASH_ACCESS_TOKEN)
                .get()
                .build();


        new Thread(() -> {
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    mBaseActivityHandler.post(() -> {
                        showToast("请求失败");
                        Log.e(TAG, "请求失败" + e);
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.i(TAG, "responseData=" + responseData);
                    Gson gson = new Gson();
                    try {
                        UnsplashPhoto unsplashPhoto = gson.fromJson(responseData, UnsplashPhoto.class);
                        Log.i(TAG, "unsplashPhoto=" + unsplashPhoto);
                        String fullUrl = unsplashPhoto.getUrls().getFull();
                        mBaseActivityHandler.post(() -> {
                           Glide.with(getBaseContext()).load(fullUrl).into(binding.ivPhoto);
                        });
                    } catch (JsonSyntaxException e) {
                        mBaseActivityHandler.post(() -> {
                            showToast("请求失败");
                            Log.e(TAG, "请求失败" + e);
                        });
                    }
                }
            });
        }).start();
    }
}