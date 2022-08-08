package com.example.myapplication.multiThread;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myapplication.R;

import java.util.concurrent.ExecutionException;

public class MultipleThreadDemoActivity extends AppCompatActivity {

    private final String TAG = "MultipleThread";
    private AsyncTask<String, Integer, String> helloWorld;
    private ImageView ivLoading;
    private ObjectAnimator objectAnimator;
    private HandlerThread handlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_thread_demo);

        ivLoading = findViewById(R.id.ivLoading);

        ProgressBar progress = findViewById(R.id.progress_circular);
        objectAnimator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0, 359);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE); // 无限重复
        objectAnimator.setInterpolator(new LinearInterpolator());

        findViewById(R.id.btn_async_task).setOnClickListener(view -> {
            objectAnimator.start();
            DemoAsyncTask demoAsyncTask = new DemoAsyncTask(progress);
            helloWorld = demoAsyncTask.execute("HelloWorld");
            new Thread() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    super.run();
                    try {
                        String result = helloWorld.get(); // 会阻塞当前线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                objectAnimator.cancel();
                            }
                        });
                        Log.i("MultipleThreadDemoActivity", result);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });

        long startTime = System.currentTimeMillis();
        handlerThread = new HandlerThread("handlerThread") {
            @Override
            public void run() {
                Log.i(TAG, "before run: " + (System.currentTimeMillis() - startTime));
                super.run();
                Log.i(TAG, "after run: " + (System.currentTimeMillis() - startTime));

            }

            @Override
            protected void onLooperPrepared() {
                super.onLooperPrepared();
                long endTime = System.currentTimeMillis();
                Log.i(TAG, "onLooperPrepared: " + (endTime - startTime));
                Log.i(TAG, "onLooperPrepared:" + Thread.currentThread().getName());
            }

            @Override
            public synchronized void start() {
                super.start();
                getLooper(); // 会等待Looper初始化。（加个锁即可）
                long endTime = System.currentTimeMillis();
                Log.i(TAG, "getLopper: " + (endTime - startTime) + getLooper());
                Log.i(TAG, Thread.currentThread().getName());
            }
        };
        handlerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }
}