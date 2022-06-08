package com.example.myapplication.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;

public class SQLiteDemoActivity extends AppCompatActivity {

    private LogView logView;
    private DemoTableDbHelper demoTableDbHelper;
    private SQLiteDatabase writableDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_demo);
        logView = findViewById(R.id.tv_log);

        demoTableDbHelper = new DemoTableDbHelper(this);
        //TODO: 后台线程获取数据库
        writableDatabase = demoTableDbHelper.getWritableDatabase();

        findViewById(R.id.btn_query).setOnClickListener(view -> {
            queryData();
        });

        findViewById(R.id.btn_insert).setOnClickListener(view -> {
            insertData();
        });

        findViewById(R.id.btn_update).setOnClickListener(view -> {
            updateData();
        });

        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            deleteData();
        });

        findViewById(R.id.btn_clear_log).setOnClickListener(view -> {
            logView.clearLog();
        });
    }

    private void deleteData() {
        String where = DemoTableContract.DemoEntry.COLUMN_NAME_VALUE + " = ?";
        String[] args = {"Jhon"};
        int delete = writableDatabase.delete(DemoTableContract.DemoEntry.TABLE_NAME, where, args);
        logView.addLog("成功删除：" + delete);

    }

    private void updateData() {
        ContentValues value = new ContentValues();
        value.put(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE, "Jhon");
        String where = DemoTableContract.DemoEntry.COLUMN_NAME_VALUE + " = ?";
        String[] args = {"James"};
        int row = writableDatabase.update(DemoTableContract.DemoEntry.TABLE_NAME, value, where, args);
        logView.addLog("成功修改：" + row);
    }

    private void insertData() {
        ContentValues value = new ContentValues();
        value.put(DemoTableContract.DemoEntry.COLUMN_NAME_FIELD, "name");
        value.put(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE, "James");
        long rowId = writableDatabase.insert(DemoTableContract.DemoEntry.TABLE_NAME, null, value);
        logView.addLog("成功插入：" + rowId);
    }

    @SuppressLint("Range")
    private void queryData() {

        String[] columns = {
                DemoTableContract.DemoEntry._ID,
                DemoTableContract.DemoEntry.COLUMN_NAME_FIELD,
                DemoTableContract.DemoEntry.COLUMN_NAME_VALUE
        };

        try (Cursor result = writableDatabase.query(DemoTableContract.DemoEntry.TABLE_NAME, columns, null, null, null, null, null)) {
            if (result != null && result.getCount() > 0) {
                result.moveToFirst();
                do {
                    String id = result.getString(result.getColumnIndex(DemoTableContract.DemoEntry._ID));
                    String field = result.getString(result.getColumnIndex(DemoTableContract.DemoEntry.COLUMN_NAME_FIELD));
                    String value = result.getString(result.getColumnIndex(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE));
                    logView.addLog("id: " + id);
                    logView.addLog("field: " + field);
                    logView.addLog("value: " + value);
                    logView.addLog("\n");
                } while (result.moveToNext());
            } else if (result == null) {
                logView.addLog("查询失败");
            } else {
                logView.addLog("查询结果为空");
            }
            logView.addLog("\n");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writableDatabase.close();
        demoTableDbHelper.close();
    }
}