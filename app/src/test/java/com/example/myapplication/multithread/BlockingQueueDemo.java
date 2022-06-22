package com.example.myapplication.multithread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueDemo {
    @Test
    public void test1() throws InterruptedException {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10, true);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread("生产者-- " + i) {
                int count = 0;
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            Thread.sleep(random.nextInt(1000));
                            blockingQueue.put(getName() + " " + ++count);
                            System.out.println(getName() + " put message " + count + "    remain:" + blockingQueue.size());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }.start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread("\t\t\t\t\t\t\t\t\t\t消费者-- " + i) {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            Thread.sleep(random.nextInt(1000));
                            String take = blockingQueue.take();
                            System.out.println(getName() + " take message " + take  + "    remain:" + blockingQueue.size());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }.start();
        }

        Thread.sleep(1000000);
    }
}
