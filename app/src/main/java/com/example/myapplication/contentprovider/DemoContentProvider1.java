package com.example.myapplication.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DemoContentProvider1 extends ContentProvider {

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // root uri对应的数字是-1。

    static {
        uriMatcher.addURI("com.example.myapplication.provider", "/" + DemoTableContract.DemoEntry.TABLE_NAME, 1);
        uriMatcher.addURI("com.example.myapplication.provider", "/" + DemoTableContract.DemoEntry.TABLE_NAME+ "/#", 2);
    }

    private DemoTableDbHelper demoTableDbHelper;
    private SQLiteDatabase writableDatabase;

    @Override
    public boolean onCreate() {
        demoTableDbHelper = new DemoTableDbHelper(getContext());
        writableDatabase = demoTableDbHelper.getWritableDatabase();

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        switch (uriMatcher.match(uri)) {
            case 1: {
                //搜索全表
                return writableDatabase.query(DemoTableContract.DemoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case 2: {
                String id = uri.getLastPathSegment();
                String selection1 = DemoTableContract.DemoEntry._ID + "=" + id;
                return writableDatabase.query(DemoTableContract.DemoEntry.TABLE_NAME, projection, selection1, null, null, null, null );
            }
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
