package com.example.myapplication.multithread;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureDemo {

    @Test
    void test1() throws ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            final String msg = "hello";
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return msg.toUpperCase();
            }
        };
        FutureTask<String> task = new FutureTask<String>(callable);
        new Thread(task).start();
        String res = task.get();
        System.out.println(res);

    }
}
