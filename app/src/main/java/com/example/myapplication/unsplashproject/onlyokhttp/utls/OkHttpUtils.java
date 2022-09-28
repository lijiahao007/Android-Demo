package com.example.myapplication.unsplashproject.onlyokhttp.utls;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashPhoto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";

    private static OkHttpClient okHttpClient = null;
    private static Gson gson = null;


    static {
        getInstance();
        gson = new GsonBuilder()
                .create();
    }

    /**
     * 单例模式
     */
    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .addNetworkInterceptor(new HttpLoggingInterceptor())
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    private static final String UNSPLASH_ACCESS_TOKEN = "wMFwQhlS6JzfPU8SJijqMG9gvpTdcpYOWG5xGuZE8UA";
    private static final String UNSPLASH_SECRET_TOKEN = "PS1xSEgvY8TTpGqtDaEo5Ueww467FCI5BnAUsPVvCFk";

    public static String UNSPLASH_BASE_URL = "https://api.unsplash.com";
    private static String UNSPLASH_RANDOM_PHOTO_URL = UNSPLASH_BASE_URL + "/photos/random";


    /**
     * 同步的从unsplash网站中获取count张随机图片
     *
     * @param count 图片的个数
     * @return UnsplashPhoto列表 (里面有图片的信息)
     */
    @WorkerThread
    public static List<UnsplashPhoto> getRandomPhotos(int count) {
        HttpUrl url = HttpUrl.parse(UNSPLASH_RANDOM_PHOTO_URL)
                .newBuilder()
                .addQueryParameter("count", String.valueOf(count))
                .build();
        Request request = buildGetUnsplashRequest(url);
        try {
            Response response = okHttpClient.newCall(request).execute();
            String body = response.body().string();
            List<UnsplashPhoto> unsplashPhotoList = gson.fromJson(body, new TypeToken<List<UnsplashPhoto>>() {
            }.getType());
            return unsplashPhotoList;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getRandomPhotos --> " + e);
        }
        return null;
    }

    /**
     * 异步的从unsplash网站中获取count张随机图片
     * 注意：回调是在工作线程中调用
     *
     * @param count    图片的个数
     * @param callback 回调
     * @return
     */
    public static void getRandomPhotos(int count, UnsplashPhotoListCallback callback) {
        HttpUrl url = HttpUrl.parse(UNSPLASH_RANDOM_PHOTO_URL)
                .newBuilder()
                .addQueryParameter("count", String.valueOf(count))
                .build();
        Request request = buildGetUnsplashRequest(url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(call, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String body = response.body().string();
                List<UnsplashPhoto> unsplashPhotoList = gson.fromJson(body, new TypeToken<List<UnsplashPhoto>>() {
                }.getType());
                callback.onResponse(call, unsplashPhotoList);
            }
        });
    }

    public interface UnsplashPhotoListCallback {
        void onResponse(@NonNull Call call, @NonNull List<UnsplashPhoto> photos);

        void onError(@NonNull Call call, @NonNull Exception e);
    }


    private static Request buildGetUnsplashRequest(HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .header("Authorization", "Client-ID " + UNSPLASH_ACCESS_TOKEN)
                .get()
                .build();
    }
}
