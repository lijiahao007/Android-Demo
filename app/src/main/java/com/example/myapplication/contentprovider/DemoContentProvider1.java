package com.example.myapplication.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
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
        uriMatcher.addURI("com.example.myapplication.provider", "/" + DemoTableContract.DemoEntry.TABLE_NAME + "/#", 2);
    }

    private SQLiteDatabase writableDatabase;

    @Override
    public boolean onCreate() {
        DemoTableDbHelper demoTableDbHelper = new DemoTableDbHelper(getContext());
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
                String selection1 = selection + " AND " + DemoTableContract.DemoEntry._ID + "=" + id;
                return writableDatabase.query(DemoTableContract.DemoEntry.TABLE_NAME, projection, selection1, null, null, null, null);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1: {
                return "vnd.android.cursor.dir/vnd.com.example.myapplication." + DemoTableContract.DemoEntry.TABLE_NAME;
            }
            case 2: {
                return "vnd.android.cursor.item/vnd.com.example.myapplication." + DemoTableContract.DemoEntry.TABLE_NAME;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long insertId = writableDatabase.insert(DemoTableContract.DemoEntry.TABLE_NAME, null, values);
        return ContentUris.withAppendedId(uri, insertId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (uriMatcher.match(uri)) {
            case 1: {
                return writableDatabase.delete(DemoTableContract.DemoEntry.TABLE_NAME, selection, selectionArgs);
            }
            case 2: {
                String id = uri.getLastPathSegment();
                if (selection == null) {
                    selection = DemoTableContract.DemoEntry._ID + " = " + id;
                } else {
                    selection = selection + " AND " + DemoTableContract.DemoEntry._ID + " = " + id;
                }
                return writableDatabase.delete(DemoTableContract.DemoEntry.TABLE_NAME, selection, null);
            }
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case 1: {
                return writableDatabase.update(DemoTableContract.DemoEntry.TABLE_NAME, values, selection, selectionArgs);
            }
            case 2: {
                String id = uri.getLastPathSegment();
                String tmpSelection = DemoTableContract.DemoEntry._ID + " = " + id;
                return writableDatabase.update(DemoTableContract.DemoEntry.TABLE_NAME, values, tmpSelection, null);
            }
        }
        return 0;
    }

}
