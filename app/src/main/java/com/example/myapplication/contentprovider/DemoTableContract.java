package com.example.myapplication.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DemoTableContract {
    // 协定类，用来定义表示表名、列名

    private DemoTableContract() {}

    public static class DemoEntry implements BaseColumns {
        public static final String TABLE_NAME = "demo_table"; // 表名
        public static final String COLUMN_NAME_FIELD = "field"; // 列名1 field
        public static final String COLUMN_NAME_VALUE = "value"; // 列名2 value
        public static final Uri TABLE_URI = Uri.parse("content://com.example.myapplication.provider/" + TABLE_NAME);

    }




}
