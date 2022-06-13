package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
        float[] a = {1,2,3,4};
        float[] b = a.clone();
        b[0] = b[0] * 10;
        for (int i = 0; i < 4; i++) {
            System.out.println("a:" + a[i] + " b:" + b[i]);
        }
    }

}