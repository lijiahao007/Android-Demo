package com.example.myapplication.utils;

import android.content.Context;
import android.util.TypedValue;

public class DimenUtil {
    public static float dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static float sp2px(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
