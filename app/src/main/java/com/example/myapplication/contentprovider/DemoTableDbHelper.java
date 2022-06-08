package com.example.myapplication.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DemoTableDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DemoTable.db";

    // 创建表SQLite语句
    private static final String SQL_CREATE_TABLE = " CREATE TABLE " + DemoTableContract.DemoEntry.TABLE_NAME + "(" +
            DemoTableContract.DemoEntry._ID +" INTEGER PRIMARY KEY," +
            DemoTableContract.DemoEntry.COLUMN_NAME_FIELD + " TEXT," +
            DemoTableContract.DemoEntry.COLUMN_NAME_VALUE + " TEXT)";

    // 删除表SQLite语句
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DemoTableContract.DemoEntry.TABLE_NAME;

    public DemoTableDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // 更新操作
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
