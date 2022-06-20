package com.example.myapplication.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class CustomQRCodeView extends RelativeLayout {

    private ImageView ivQrCode;
    private TextView tvAttention;
    private final static String TAG = "CustomQRCodeView";
    private TextView tvScan;
    private RelativeLayout rootLayout;
    private int distance;
    private boolean isFirst = true;

    public CustomQRCodeView(Context context) {
        super(context);
        init();
    }

    public CustomQRCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomQRCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomQRCodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_qrcode, this, true);
        ivQrCode = findViewById(R.id.iv_qrcode);
        tvAttention = findViewById(R.id.tv_attention);
        tvScan = findViewById(R.id.tv_scan);
        rootLayout = findViewById(R.id.root_view);
        String text = tvAttention.getText().toString();
        int start = text.indexOf("V380小亮助手");
        int end = start + 8;
        if (start == -1) {
            start = text.indexOf("小亮助手");
            end = start + 4;
        }

        // 设置颜色
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.YELLOW);
        stringBuilder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAttention.setText(stringBuilder);
        isFirst = true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isFirst) {
            // 高度自适应
            int multipleLines = tvAttention.getHeight();
            int singleLine = tvScan.getHeight();
            distance = multipleLines - singleLine;
            ViewGroup.LayoutParams layoutParams = rootLayout.getLayoutParams();
            layoutParams.height += distance;
            rootLayout.setLayoutParams(layoutParams);
            isFirst = false;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        ivQrCode.setImageBitmap(bitmap);
        invalidate();
    }

    public Bitmap loadBitmapFromViewBySystem() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        return this.getDrawingCache();
    }
}
