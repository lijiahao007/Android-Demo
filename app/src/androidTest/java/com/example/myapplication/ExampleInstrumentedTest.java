package com.example.myapplication;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.myapplication.album.MediaBean;
import com.example.myapplication.album.MediaType;

import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myapplication", appContext.getPackageName());
    }


    @Test
    public void test6() {
        ArrayList<MediaBean> list1 = new ArrayList<>();
        list1.add(new MediaBean(null,  "", MediaType.IMAGE));
        ArrayList<? extends Parcelable> list2 = (ArrayList<? extends Parcelable>)list1;
        ArrayList<MediaBean> list3 = (ArrayList<MediaBean>) list2;

        System.out.println(list3.get(0));
        Log.i("test6", list3.get(0).toString());
    }
}