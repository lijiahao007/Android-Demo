package com.example.myapplication.network;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import android.util.Log;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartReader;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class MockServerTest {

    private MockWebServer mockWebServer;
    private Callback callback;

    @Before
    public void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("client receiver response failed: " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = Objects.requireNonNull(response.body()).string();
                System.out.print("client receiver response: ");
                System.out.println(string);
            }
        };
    }

    @After
    public void release() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    public void get1() throws Exception {
        // 1. 获取url
        HttpUrl url = mockWebServer.url("/v1/chat");

        // 2. 预设response
        MockResponse mockResponse = new MockResponse()
                .setBody("{\"status\":\"success\"}")
                .setResponseCode(404);

        mockResponse.throttleBody(1, 1, TimeUnit.SECONDS);

        mockWebServer.enqueue(mockResponse);
        System.out.println("url:" + url);


        // 3. 构建网络请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .method("GET", null);
        Request request = requestBuilder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

        // 4. 模拟服务器获取请求，用于查看请求信息
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        System.out.println("recordedRequest:" + recordedRequest);

    }


    @Test
    public void post1() throws Exception {
        // 1. 预设相应
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("hello");
        mockWebServer.enqueue(mockResponse);

        // 2. 网络请求
        HttpUrl url = mockWebServer.url("/v1/post1");
        System.out.println(url + "  " + mockWebServer.getHostName() + " " + mockWebServer.getPort());
        RequestBody formBody = new FormBody.Builder()
                .add("username", "1234")
                .add("password", "12345567")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);


        // 3. mockServer获取请求
        RecordedRequest receiveRequest = mockWebServer.takeRequest();
        System.out.println("mockServer receiver request:  " + receiveRequest);
        System.out.println("request body:" + receiveRequest.getBody());
    }

    @Test
    public void postFile() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("hello");
        mockWebServer.enqueue(mockResponse);

        HttpUrl url = mockWebServer.url("/postFile");
        RequestBody body = RequestBody.create(new File("C:/Users/admin/Desktop/Git 基本使用考核.doc"), MediaType.parse("text/plain")); // 将文件以二进制形式写入requestBody中
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("file-name", "Git.doc")
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

        try {
            RecordedRequest takeRequest = mockWebServer.takeRequest();

            Buffer body1 = takeRequest.getBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(body1.inputStream()));
            String tmp = reader.readLine();
            System.out.println("==================================================");

            while (tmp != null) {
                System.out.println(tmp);
                tmp = reader.readLine();
            }
            System.out.println("==================================================");

            String fileName = takeRequest.getHeader("file-name");
            System.out.println(fileName);
            Buffer buffer = takeRequest.getBody();
            System.out.println(buffer.size() + " " + buffer);
            File file = new File("C:/Users/admin/Desktop/" + fileName);
            boolean newFile = file.createNewFile();
            if (newFile) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buffer.readByteArray());
                fileOutputStream.close();
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void postMultipleFile() throws InterruptedException, IOException {

        mockWebServer.enqueue(new MockResponse().setBody("hello"));

        HttpUrl url = mockWebServer.url("/postMultipleFile");
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("author", "李嘉浩")
                .addFormDataPart("file", "Git 基本使用考核.doc", RequestBody.create(new File("C:/Users/admin/Desktop/Git 基本使用考核.doc"), MediaType.parse("text/plain")))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);


        RecordedRequest takeRequest = mockWebServer.takeRequest();
        System.out.println("takeRequest:" + takeRequest);
        Buffer body = takeRequest.getBody();
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(body.inputStream()));
        String tmp;
        while ((tmp = inputStream.readLine()) != null) {
            System.out.println(tmp);
        }
    }


    @Test
    public void httpsConnect() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        okHttpClient.newCall(
                new Request.Builder()
                        .url("https://baidu.com")
                        .build()).enqueue(callback);
    }


}
