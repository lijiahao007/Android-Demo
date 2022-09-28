package com.example.myapplication.unsplashproject.onlyokhttp.utls;

import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UnsplashPhotoDataBaseHelper extends SQLiteOpenHelper {

    public UnsplashPhotoDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER," +
                COLUMN_CREATED_TIME + " TEXT," +
                COLUMN_UPDATED_TIME + " TEXT," +
                COLUMN_PROMOTED_TIME + " TEXT," +
                COLUMN_WIDTH + "INTEGER," +
                COLUMN_HEIGHT + "INTEGER," +
                COLUMN_COLOR + "TEXT," +
                COLUMN_DESCRIPTION + "TEXT," +
                COLUMN_CATEGORIES + "TEXT," +
                COLUMN_LIKES + "INTEGER," +
                COLUMN_VIEW + "INTEGER," +
                COLUMN_DOWNLOAD + "INTEGER," +
                COLUMN_PIC_RAW_URL + "TEXT," +
                COLUMN_PIC_FULL_URL + "TEXT," +
                COLUMN_PIC_REGULAR_URL + "TEXT," +
                COLUMN_PIC_SMALL_URL + "TEXT," +
                COLUMN_PIC_THUMB_URL + "TEXT," +
                COLUMN_PIC_SMALL_S3_URL + "TEXT)";

        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
