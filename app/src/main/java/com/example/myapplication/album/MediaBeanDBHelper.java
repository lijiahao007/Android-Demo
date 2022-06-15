package com.example.myapplication.album;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class MediaBeanDBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "MediaBean.db";

    // 创建表SQLite语句
    private static final String SQL_CREATE_TABLE = " CREATE TABLE " + MediaBean.Entry.TABLE_NAME + "(" +
            MediaBean.Entry._ID + " INTEGER PRIMARY KEY," +
            MediaBean.Entry.URI + " TEXT," +
            MediaBean.Entry.TIMESTAMP + " INTEGER, " +
            MediaBean.Entry.FILENAME + " TEXT," +
            MediaBean.Entry.TYPE + " TEXT, " +
            MediaBean.Entry.DURATION + " INTEGER," +
            MediaBean.Entry.DATE + " TEXT)";

    // 删除表SQLite语句
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MediaBean.Entry.TABLE_NAME;

    public MediaBeanDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
