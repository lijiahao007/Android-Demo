package com.example.myapplication.jvm;

import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class WeakReferenceTest {
    @Test
    public void test1() {
        WeakReference<Object> weakReference = new WeakReference<>(new Object());
        Object object = new Object();
        WeakReference<Object> weakReference1 = new WeakReference<>(object);
        System.out.println(weakReference.get() + " " + weakReference1.get());
        System.gc();
        System.out.println(weakReference.get() + " " + weakReference1.get());
    }

    @Test
    public void test2() {
        SoftReference<Object> softReference = new SoftReference<>(new Object());
        Object object = new Object();
        SoftReference<Object> softReference1 = new SoftReference<>(object);
        System.out.println(softReference.get() + " " + softReference1.get());
        System.gc();
        System.out.println(softReference.get() + " " + softReference1.get());
    }
}
