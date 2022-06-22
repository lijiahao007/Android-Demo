package com.example.myapplication.multiThread;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.myapplication.R;

import java.util.concurrent.ExecutionException;

public class MultipleThreadDemoActivity extends AppCompatActivity {


    private AsyncTask<String, Integer, String> helloWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_thread_demo);

        ProgressBar progress = findViewById(R.id.progress_circular);
        findViewById(R.id.btn_async_task).setOnClickListener(view -> {
            DemoAsyncTask demoAsyncTask = new DemoAsyncTask(progress);
            helloWorld = demoAsyncTask.execute("HelloWorld");
            new Thread() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    super.run();
                    try {
                        String result = helloWorld.get(); // 会阻塞当前线程
                        Log.i("MultipleThreadDemoActivity", result);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });

    }
}