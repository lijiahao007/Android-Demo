package com.example.myapplication.utils;

import android.content.Context;

public class DimenUtil {
    public static float dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }
}
