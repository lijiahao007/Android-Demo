package com.example.myapplication.unsplashproject.onlyokhttp.utls;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashPhoto;

public class DataBaseManager {
    private static final String TAG = "DataBaseManager";
    private static final String DB_NAME = "base_db";
    private static final int DB_VERSION = 1; // 所有数据库同步
    public static UnsplashPhotoDataBaseHelper unsplashPhotoDataBaseHelper = null;

    private DataBaseManager() {
    }


    public static void initDataBaseManager(Context context) {
        if (DataBaseManager.unsplashPhotoDataBaseHelper == null) {
            DataBaseManager.unsplashPhotoDataBaseHelper = new UnsplashPhotoDataBaseHelper(context, DB_NAME, null, DB_VERSION);
        }
    }

    public static long replaceUnsplashPhoto(UnsplashPhoto photo) {
        try (SQLiteDatabase database = unsplashPhotoDataBaseHelper.getWritableDatabase()){
            ContentValues contentValues = new ContentValues();
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_ID, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_CREATED_TIME, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_UPDATED_TIME, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PROMOTED_TIME, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_WIDTH, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_HEIGHT, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_COLOR, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_DESCRIPTION, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_CATEGORIES, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_LIKES, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_VIEW, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_DOWNLOAD, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_RAW_URL, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_FULL_URL, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_REGULAR_URL, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_SMALL_URL, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_THUMB_URL, photo.getId());
            contentValues.put(UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_SMALL_S3_URL, photo.getId());
            return database.replace(UnsplashPhotoDBContract.UnsplashPhotoEntry.TABLE_NAME, null, contentValues);
        }
    }


}
