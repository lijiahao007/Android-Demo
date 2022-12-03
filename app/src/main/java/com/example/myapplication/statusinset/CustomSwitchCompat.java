package com.example.myapplication.statusinset;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import java.lang.reflect.Field;

public class CustomSwitchCompat extends SwitchCompat {
    public CustomSwitchCompat(@NonNull Context context) {
        super(context);
    }

    public CustomSwitchCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchCompat(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            Field switchWidth = SwitchCompat.class.getDeclaredField("mSwitchWidth");
            switchWidth.setAccessible(true);

            // Using 120 below as example width to set
            // We could use attr to pass in the desire width
            switchWidth.setInt(this, this.getMeasuredWidth());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
