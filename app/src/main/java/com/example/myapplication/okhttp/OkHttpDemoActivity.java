package com.example.myapplication.okhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpDemoActivity extends AppCompatActivity {

    private final static String TAG = "OkHttpDemoActivity";


    private ImageView imageView;
    private Button btnGetImage;
    private Button btnGet;


    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Uri uri = (Uri) msg.obj;
            Glide.with(OkHttpDemoActivity.this).load(uri).into(imageView);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_demo);
        get1();
        downloadImage();

    }

    private void get1() {
        btnGet = findViewById(R.id.btn_get_1);
        btnGet.setOnClickListener(view -> {
            Request.Builder requestBuilder = new Request.Builder()
                    .url("https://ghibliapi.herokuapp.com/films")
                    .method("GET", null)
                    .addHeader("Content-Type", "application/json");
            Request request = requestBuilder.build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i(TAG, "call onFailure" + e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String string = Objects.requireNonNull(response.body()).string();
                    Log.i(TAG, "call onResponse");
                    Log.i(TAG, string);
                }
            });
        });
    }

    private void downloadImage() {
        btnGetImage = findViewById(R.id.btn_get_image);
        Log.i(TAG, "downloadImage Thread:" + Thread.currentThread());
        imageView = findViewById(R.id.iv_internet_image);
        btnGetImage.setOnClickListener(view -> {
            String imageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsimplylivingtips.com%2Fwp-content%2Fuploads%2F2016%2F08%2Fbeautiful-flower-pics-for-free-download.jpg&refer=http%3A%2F%2Fsimplylivingtips.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658387859&t=a0199caa881e29e600543c63eaef3709";
            Request request = new Request.Builder().url(imageUrl).build();
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cache(new Cache(getCacheDir(),1024*1024*10)) // 10MB 缓存
                    .addInterceptor(httpLoggingInterceptor)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i(TAG, "onFailure:" + e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    ResponseBody body = response.body();
                    if (body != null) {
                        byte[] bytes = body.bytes();
                        ContentResolver contentResolver = getContentResolver();
                        Uri localUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                        OutputStream outputStream = contentResolver.openOutputStream(localUri);
                        outputStream.write(bytes);
                        Log.i(TAG, "onResponse Thread:" + Thread.currentThread());
                        handler.obtainMessage(1, localUri).sendToTarget();
                    }
                }
            });

        });
    }

}