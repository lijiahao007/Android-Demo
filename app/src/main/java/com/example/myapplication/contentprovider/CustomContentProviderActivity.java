package com.example.myapplication.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;

import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;

public class CustomContentProviderActivity extends AppCompatActivity {

    private ContentResolver contentResolver;
    private LogView logView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_content_provider);

        contentResolver = getContentResolver();
        logView = findViewById(R.id.tv_log);
        findViewById(R.id.btn_clear_log).setOnClickListener(view -> {
            logView.clearLog();
        });

        findViewById(R.id.btn_query).setOnClickListener(view -> {
            queryData();
        });

        findViewById(R.id.btn_insert).setOnClickListener(view -> {
            insertData();
        });

        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            deleteData();
        });

        findViewById(R.id.btn_update).setOnClickListener(view -> {
            updateData();
        });


    }

    private void updateData() {
        ContentValues values = new ContentValues();
        values.put(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE, "helloworld");
        String where = DemoTableContract.DemoEntry.COLUMN_NAME_FIELD + " = ?";
        String[] args = {"phone"};
        int update = contentResolver.update(DemoTableContract.DemoEntry.TABLE_URI, values, where, args);
        logView.addLog("更新" + update + "条记录");
    }

    @SuppressLint("Range")
    private void deleteData() {

        try (Cursor query = contentResolver.query(DemoTableContract.DemoEntry.TABLE_URI, new String[]{DemoTableContract.DemoEntry._ID}, null, null, DemoTableContract.DemoEntry._ID + " DESC")) {
            if (query.getCount() > 0) {
                query.moveToFirst();
                long id = query.getLong(query.getColumnIndex(DemoTableContract.DemoEntry._ID));
                Uri uri = ContentUris.withAppendedId(DemoTableContract.DemoEntry.TABLE_URI, id);
                int delete = contentResolver.delete(uri, null, null);
                logView.addLog("删除" + delete + "条记录");
            } else {
                logView.addLog("内容已空，无法删除");
            }
        }
    }

    private void insertData() {
        ContentValues values = new ContentValues();
        values.put(DemoTableContract.DemoEntry.COLUMN_NAME_FIELD, "phone");
        values.put(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE, "13535853646");
        Uri insert = contentResolver.insert(DemoTableContract.DemoEntry.TABLE_URI, values);
        logView.addLog(insert.toString());
    }

    @SuppressLint("Range")
    private void queryData() {
        String[] projection = {
                DemoTableContract.DemoEntry._ID,
                DemoTableContract.DemoEntry.COLUMN_NAME_FIELD,
                DemoTableContract.DemoEntry.COLUMN_NAME_VALUE
        };
        try (Cursor query = contentResolver.query(DemoTableContract.DemoEntry.TABLE_URI, projection, null, null, null)) {
            if (query != null && query.getCount() > 0) {
                query.moveToFirst();
                do {
                    String id = query.getString(query.getColumnIndex(DemoTableContract.DemoEntry._ID));
                    String field = query.getString(query.getColumnIndex(DemoTableContract.DemoEntry.COLUMN_NAME_FIELD));
                    String value = query.getString(query.getColumnIndex(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE));
                    logView.addLog("id:" + id);
                    logView.addLog("field:" + field);
                    logView.addLog("value:" + value);
                    logView.addLog("\n");
                } while (query.moveToNext());
            } else if (query == null) {
                logView.addLog("查询失败");
            } else {
                logView.addLog("查询结果为空");
            }
            logView.addLog("\n");
        }
    }
}