package com.example.myapplication.polygonseldemo;

import android.content.Context;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.example.myapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomViewUtil {

    public static SpannableStringBuilder setPortionClickText(Context context, TextView textView,
                                                             String content, String clickContent, ClickableSpan clickableSpan) {
        SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(content);
        Pattern p = Pattern.compile(clickContent);
        Matcher m = p.matcher(style);
        int start = 0;
        int end = 2;
        while (m.find()) {
            start = m.start();
            end = m.end();
        }
        style.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
        if (textView != null) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(style);
        }
        return style;
    }

    public static void setPortionClickText(Context context, SpannableStringBuilder style, String clickContent, ClickableSpan clickableSpan) {
        Pattern p = Pattern.compile(clickContent);
        Matcher m = p.matcher(style);
        int start = 0;
        int end = 2;
        while (m.find()) {
            start = m.start();
            end = m.end();
        }
        style.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置部分文字颜色
    }

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     */
    public static int[] getScreenHW2(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int[] HW = new int[]{width, height};
        return HW;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenW(Context context) {
        return getScreenHW2(context)[0];
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenH(Context context) {
        return getScreenHW2(context)[1];
    }


    // 计算两点之间的距离
    public static float lineSpace(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static void setFilters(EditText editText) {
        if (editText == null) return;
        editText.setFilters(new InputFilter[]{new InputFilter() {
            //Pattern pattern = Pattern.compile("[^ a-zA-Z0-9\\u4E00-\\u9FA5_]");
            Pattern pattern = Pattern.compile("[^：？、“”‘’！（）《》【】；￥。 -~\\u4E00-\\u9FA5_]");

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = pattern.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {
                    return "";
                }
            }
        }, new InputFilter.LengthFilter(16)});
    }

    public static void setFilters(EditText editText, int length) {
        if (editText == null) return;
        editText.setFilters(new InputFilter[]{new InputFilter() {
            //Pattern pattern = Pattern.compile("[^ a-zA-Z0-9\\u4E00-\\u9FA5_]");
            Pattern pattern = Pattern.compile("[^：？、“”‘’！（）《》【】；￥。 -~\\u4E00-\\u9FA5_]");

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = pattern.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {
                    return "";
                }
            }
        }, new InputFilter.LengthFilter(length)});
    }
}