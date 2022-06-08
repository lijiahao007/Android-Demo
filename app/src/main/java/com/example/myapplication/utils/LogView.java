package com.example.myapplication.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.example.myapplication.R;

import java.util.Objects;

public class LogView extends NestedScrollView {

    private Context mContext;
    private TextView tvLog;

    public LogView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public LogView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public LogView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public void init() {
        LayoutInflater.from(mContext).inflate(R.layout.view_log,this,true);
    }

    public void addLog(String msg) {
        tvLog = findViewById(R.id.tv_log_text);
        String prevLog = tvLog.getText().toString();
        prevLog += msg + "\n";
        tvLog.setText(prevLog);
        fullScroll(View.FOCUS_DOWN);
    }

    public void clearLog() {
        tvLog.setText("");
    }

}
