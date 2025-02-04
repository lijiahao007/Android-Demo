package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.album.bean.MediaType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void listTest() {
        ArrayList<String> list = new ArrayList<>(10);
        System.out.println(list.size());
    }

    @Test
    public void test() {
        float[] a = {1, 2, 3, 4};
        float[] b = a.clone();
        b[0] = b[0] * 10;
        for (int i = 0; i < 4; i++) {
            System.out.println("a:" + a[i] + " b:" + b[i]);
        }
    }

    @Test
    public void test1() {
        MediaType type = MediaType.IMAGE;
        System.out.println(type);
        MediaType type1 = MediaType.valueOf("IMAGE");
        System.out.println(type1);
    }

    @Test
    public void test2() {
        int time = 3500000;
        Duration duration = Duration.ofMillis(time);
        long hour = duration.toHours();
        long minute = duration.toMinutes() % 60;
        long second = duration.getSeconds() % 60;
        System.out.println(hour + ":" + minute + ":" + second);
    }

    @Test
    public void test3() {
        int a = 3;
        String format = String.format("%02d:%02d", a, 5);
        System.out.println(format);
    }

    @Test
    public void test4() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.print(random.nextInt(10) + " ");
            if (i % 10 == 0) {
                System.out.println();
            }
        }
    }


    @Test
    public void test5() {
        boolean[] a = new boolean[10];
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    @Test
    public void test6() {
        String a = null;

        assert a != null;
    }

    @Test
    public void test7() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        list.clear();
        System.out.println(list.size());
    }

    @Test
    public void test8() {
        String s = "关注“小亮助手”公众号";
        int index = s.indexOf("V380小亮助手");
        System.out.println(index);
        int index1 = s.indexOf("小亮助手");
        System.out.println(index1);

    }
}