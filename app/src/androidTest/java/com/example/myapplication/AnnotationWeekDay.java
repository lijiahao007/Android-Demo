package com.example.myapplication;

import android.util.Log;

import androidx.annotation.IntDef;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RunWith(AndroidJUnit4.class)
public class AnnotationWeekDay {


    private static final String TAG = "AnnotationWeekDay";

    static class WeekDays {
        public static final int MON = 0;
        public static final int TUE = 1;
        public static final int WED = 2;
        public static final int THU = 3;
        public static final int FRI = 4;
        public static final int SAT = 5;
        public static final int SUN = 6;


        public WeekDays(@WeekDay int day) {
            Log.i(TAG, day + "");
        }

        @IntDef({MON, TUE, WED, THU, FRI, SAT, SUN})
        @Retention(RetentionPolicy.SOURCE)
        public @interface WeekDay {
        }
    }

    @Test
    public void test1() {
        WeekDays weekDays = new WeekDays(WeekDays.MON);
    }
}
