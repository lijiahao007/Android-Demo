package com.example.myapplication.statusinset;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;

public class MyAlertDialog1 extends AlertDialog {
    protected MyAlertDialog1(Context context) {
        super(context);
    }

    protected MyAlertDialog1(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected MyAlertDialog1(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_round_alert);

        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.RIGHT);
            View decorView = window.getDecorView();

            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.GREEN);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flag = decorView.getSystemUiVisibility();
                    decorView.setSystemUiVisibility(flag | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }


    }
}
