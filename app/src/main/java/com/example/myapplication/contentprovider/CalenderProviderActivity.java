package com.example.myapplication.contentprovider;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.UserDictionary;

import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalenderProviderActivity extends AppCompatActivity {

    static private final String[] calenderPermission = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };
    static private final int CALENDER_PERMISSION_REQUEST_CODE = 1;

    private ContentResolver contentResolver;
    private LogView logView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_provider);
        // 权限申请
        boolean flag = true;
        for (String permission : calenderPermission) {
            int result = checkSelfPermission(permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                flag = false;
            }
        }
        if (!flag) {
            requestPermissions(calenderPermission, CALENDER_PERMISSION_REQUEST_CODE);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALENDER_PERMISSION_REQUEST_CODE) {
            boolean flag = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                init();
            }
        }
    }

    private void init() {

        contentResolver = getContentResolver();
        logView = findViewById(R.id.tv_log);

        findViewById(R.id.btn_insert_calender).setOnClickListener(view -> {
            insertCalender();
        });

        findViewById(R.id.btn_query_calender).setOnClickListener(view -> {
            queryCalender();
        });

        findViewById(R.id.btn_delete_calender).setOnClickListener(view -> {
            deleteCalender();
        });
        findViewById(R.id.btn_clear_log).setOnClickListener(view -> {
            logView.clearLog();
        });

        findViewById(R.id.btn_update_calender).setOnClickListener(view -> {
            updateCalender();
        });

    }


    private void insertCalender() {
        // 插入日历
        ContentValues eventValues = new ContentValues();
        String catId = "1";
        long beginTimeMillis = System.currentTimeMillis();
        long endTimeMillis = System.currentTimeMillis() + 10000;
        String title = "title";
        String description = "日历事件！！";
        String location = "广州";
        eventValues.put(CalendarContract.Events.CALENDAR_ID, catId); // 日历账号 ID
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID()); // 时区
        eventValues.put(CalendarContract.Events.DTSTART, beginTimeMillis); // 开始时间
        eventValues.put(CalendarContract.Events.DTEND, endTimeMillis); // 结束时间
        eventValues.put(CalendarContract.Events.TITLE, title); // 标题
        eventValues.put(CalendarContract.Events.DESCRIPTION, description); // 描述
        eventValues.put(CalendarContract.Events.EVENT_LOCATION, location); // 地点
        Uri result = contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues);
        logView.addLog("插入成功：" + result.toString());
    }

    @SuppressLint("Range")
    private void queryCalender() {
        String[] projections = {
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.EVENT_TIMEZONE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.EVENT_LOCATION
        };

        long limitTime = System.currentTimeMillis() + 50000000;
        String selection = CalendarContract.Events.DTEND + "<= ?";
        String[] selectionArgs = {String.valueOf(limitTime)};
        String sortOrder = CalendarContract.Events.DTSTART;

        try (Cursor query = contentResolver.query(CalendarContract.Events.CONTENT_URI, projections, null, null, sortOrder)) {
            if (query != null && query.getCount() > 0) {
                query.moveToFirst();
                do {
                    String id = query.getString(query.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
                    String timeZone = query.getString(query.getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE));
                    String startTime = query.getString(query.getColumnIndex(CalendarContract.Events.DTSTART));
                    String endTime = query.getString(query.getColumnIndex(CalendarContract.Events.DTEND));
                    String title = query.getString(query.getColumnIndex(CalendarContract.Events.TITLE));
                    String description = query.getString(query.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                    String location = query.getString(query.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                    logView.addLog(timeZone);
                    logView.addLog(millsToLocalDateTimeStr(startTime));
                    logView.addLog(millsToLocalDateTimeStr(endTime));
                    logView.addLog(description);
                    logView.addLog(location);
                    logView.addLog(title);
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

    private void deleteCalender() {
        String where = CalendarContract.Events.EVENT_LOCATION + " = ?";
        String[] args = { "广州" };
        int delete = contentResolver.delete(CalendarContract.Events.CONTENT_URI, where, args);
        logView.addLog("删除" + delete + "条记录");
    }

    private void updateCalender() {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.EVENT_LOCATION, "广州广州");
        String where = CalendarContract.Events.EVENT_LOCATION + " = ?";
        String[] args = {"广州"};
        int update = contentResolver.update(CalendarContract.Events.CONTENT_URI, values, where, args);
        logView.addLog("更新" + update + "条记录");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String millsToLocalDateTimeStr(String millis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(millis)), ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}