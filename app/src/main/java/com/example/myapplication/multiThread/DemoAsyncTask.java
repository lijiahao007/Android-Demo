package com.example.myapplication.multiThread;

import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class DemoAsyncTask extends AsyncTask<String, Integer, String> {

    WeakReference<ProgressBar> progressBar;

    public DemoAsyncTask(ProgressBar progressBar) {
        super();
        this.progressBar = new WeakReference<>(progressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("DemoAsyncTask", "onPreExecute " + Thread.currentThread());
        progressBar.get().setProgress(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        Log.i("DemoAsyncTask", "doInBackground " + Thread.currentThread());
        for (int i = 0; i < 100; i++) {
            publishProgress(i);
            Log.i("DemoAsyncTask", String.valueOf(i));
            SystemClock.sleep(100);
        }

        Optional<String> reduce = Arrays.stream(strings).reduce(new BinaryOperator<String>() {
            @Override
            public String apply(String s, String s2) {
                return s + " " + s2;
            }
        });
        return reduce.get() + " finish";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.i("DemoAsyncTask", "onProgressUpdate " + Thread.currentThread());
        Log.i("DemoAsyncTask", "onProgressUpdate " + Arrays.toString(values));

        ProgressBar progressBar = this.progressBar.get();
        progressBar.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.i("DemoAsyncTask", "onPostExecute " + Thread.currentThread());

    }
}
