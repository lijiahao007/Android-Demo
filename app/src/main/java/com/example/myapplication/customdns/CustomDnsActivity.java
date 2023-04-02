package com.example.myapplication.customdns;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityCustomDnsBinding;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomDnsActivity extends BaseActivity<ActivityCustomDnsBinding> {

    private OkHttpClient customDnsClient;
    private OkHttpClient defaultClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用自定义DNS服务器地址
        String customDnsServerIpAddress = "46.245.81.68";
        CustomDns customDns = new CustomDns(customDnsServerIpAddress);

        // 创建一个新的OkHttp客户端实例，使用自定义DNS解析
        customDnsClient = new OkHttpClient.Builder()
                .dns(customDns)
                .build();

        DefaultDns defaultDns = new DefaultDns();

        defaultClient = new OkHttpClient.Builder()
                .dns(defaultDns)
                .build();

        // 创建一个网络请求
        Request request = new Request.Builder()
                .url("https://www.bilibli.com/")
                .build();


        binding.btnRequest.setOnClickListener(new View.OnClickListener() {
            boolean isUsingDefaultDns = false;
            @Override
            public void onClick(View v) {
                // 发起网络请求
                Log.i(TAG, "发起网络请求");
                customDnsClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // 请求失败时的处理
                        Log.e(TAG, "网络请求失败：", e);
                        if (!isUsingDefaultDns) {
                            Log.w(TAG, "现用默认的DNS解析域名");
                            defaultClient.newCall(request).enqueue(this);
                            isUsingDefaultDns = true;
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 请求成功时的处理
                        String responseStr = null;
                        if (response.body() != null) {
                            responseStr =new String(response.body().bytes());
                        }
                        Log.i(TAG, "网络请求成功：" + responseStr);
                    }
                });
            }
        });
    }
}