package com.example.myapplication.contentprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;

import java.util.concurrent.ExecutorService;

public class SQLiteDemoActivity extends AppCompatActivity {

    private LogView logView;
    private DemoTableDbHelper demoTableDbHelper;
    private SQLiteDatabase writableDatabase;

    private static final int STRING_MESSAGE = 1;
    private final Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            logView.addLog((String) msg.obj);
        }
    };
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_demo);
        logView = findViewById(R.id.tv_log);
        executorService = ((MyApplication) getApplication()).executorService;

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

        executorService.execute(() -> {
            int delete = writableDatabase.delete(DemoTableContract.DemoEntry.TABLE_NAME, where, args);
            uiHandler.obtainMessage(STRING_MESSAGE, "成功删除：" + delete).sendToTarget();
        });


    }

    private void updateData() {
        ContentValues value = new ContentValues();
        value.put(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE, "Jhon");
        String where = DemoTableContract.DemoEntry.COLUMN_NAME_VALUE + " = ?";
        String[] args = {"James"};
        executorService.execute(() -> {
            int row = writableDatabase.update(DemoTableContract.DemoEntry.TABLE_NAME, value, where, args);
            uiHandler.obtainMessage(STRING_MESSAGE,"成功修改：" + row ).sendToTarget();
        });
    }

    private void insertData() {
        ContentValues value = new ContentValues();
        value.put(DemoTableContract.DemoEntry.COLUMN_NAME_FIELD, "name");
        value.put(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE, "James");
        executorService.execute(() -> {
            long rowId = writableDatabase.insert(DemoTableContract.DemoEntry.TABLE_NAME, null, value);
            uiHandler.obtainMessage(STRING_MESSAGE,"成功插入：" + rowId ).sendToTarget();
        });
    }

    @SuppressLint("Range")
    private void queryData() {

        String[] columns = {
                DemoTableContract.DemoEntry._ID,
                DemoTableContract.DemoEntry.COLUMN_NAME_FIELD,
                DemoTableContract.DemoEntry.COLUMN_NAME_VALUE
        };

        executorService.execute(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            try (Cursor result = writableDatabase.query(DemoTableContract.DemoEntry.TABLE_NAME, columns, null, null, null, null, null)) {
                if (result != null && result.getCount() > 0) {
                    result.moveToFirst();
                    do {
                        String id = result.getString(result.getColumnIndex(DemoTableContract.DemoEntry._ID));
                        String field = result.getString(result.getColumnIndex(DemoTableContract.DemoEntry.COLUMN_NAME_FIELD));
                        String value = result.getString(result.getColumnIndex(DemoTableContract.DemoEntry.COLUMN_NAME_VALUE));
                        stringBuilder.append("id: ").append(id).append("\n");
                        stringBuilder.append("field: ").append(field).append("\n");
                        stringBuilder.append("value: ").append(value).append("\n\n");
                    } while (result.moveToNext());
                } else if (result == null) {
                    stringBuilder.append("查询失败");
                } else {
                    stringBuilder.append("查询结果为空");
                }
                stringBuilder.append("\n");
            }
            uiHandler.obtainMessage(STRING_MESSAGE, stringBuilder.toString()).sendToTarget();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writableDatabase.close();
        demoTableDbHelper.close();
    }
}