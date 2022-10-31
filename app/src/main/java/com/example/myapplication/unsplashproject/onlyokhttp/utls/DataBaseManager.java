package com.example.myapplication.unsplashproject.onlyokhttp.utls;

import android.content.Context;

public class DataBaseManager {
    private static final String TAG = "DataBaseManager";
    private static final String DB_NAME = "base_db";
    private static final int DB_VERSION = 1; // 所有数据库同步
    private static UnsplashPhotoDataBaseHelper unsplashPhotoDataBaseHelper = null;

    public static UnsplashPhotoDataBaseHelper getUnsplashPhotoDataBaseHelper(Context context) {
        if (unsplashPhotoDataBaseHelper == null) {
            unsplashPhotoDataBaseHelper = new UnsplashPhotoDataBaseHelper(context, DB_NAME, null, DB_VERSION);
        }
        return unsplashPhotoDataBaseHelper;
    }



}
