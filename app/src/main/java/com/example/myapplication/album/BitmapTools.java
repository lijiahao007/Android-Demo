package com.example.myapplication.album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapTools {
    public static Bitmap getScaleBitmap(String path, int destWidth, int destHeight) {
        // 读取并压缩bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 解码器会返回null，会query位图，但是不会为其分配内存。位图信息会存在out中
        BitmapFactory.decodeFile(path, options); // 因为上面设置为true，所以会返回null
        double srcHeight = options.outHeight;
        double srcWidth = options.outWidth;
        int inSampleSize = 1; // inSampleSize:outSampleSize的比例
        if (srcHeight > destHeight || srcWidth > destWidth) {
            double heightScale = srcHeight / (destHeight * 1.0);
            double widthScale = srcWidth / (destWidth * 1.0);
            double sampleScale = Math.max(heightScale, widthScale);
            inSampleSize = (int) Math.round(sampleScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;  // 通过设置inSampleSize来设置压缩比例，如果inSampleSize = 2, 则输出bitmap的长宽都是原来一半。
        return BitmapFactory.decodeFile(path, options);
    }
}
