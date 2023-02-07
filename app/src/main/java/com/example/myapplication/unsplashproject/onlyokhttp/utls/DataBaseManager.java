package com.example.myapplication.unsplashproject.onlyokhttp.utls;

import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_CATEGORIES;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_COLOR;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_CREATED_TIME;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_DESCRIPTION;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_DOWNLOAD;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_HEIGHT;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_ID;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_LIKES;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_FULL_URL;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_RAW_URL;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_REGULAR_URL;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_SMALL_S3_URL;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_SMALL_URL;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PIC_THUMB_URL;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_PROMOTED_TIME;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_UPDATED_TIME;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_VIEW;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.COLUMN_WIDTH;
import static com.example.myapplication.unsplashproject.onlyokhttp.utls.UnsplashPhotoDBContract.UnsplashPhotoEntry.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashPhoto;
import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DataBaseManager {
    private static final String TAG = "DataBaseManager";
    private static final String DB_NAME = "base_db";
    private static final int DB_VERSION = 1; // 所有数据库同步
    public static UnsplashPhotoDataBaseHelper unsplashPhotoDataBaseHelper = null;

    private DataBaseManager() {
    }


    public static void initDataBaseManager(Context context) {
        if (DataBaseManager.unsplashPhotoDataBaseHelper == null) {
            DataBaseManager.unsplashPhotoDataBaseHelper = new UnsplashPhotoDataBaseHelper(context, DB_NAME, null, DB_VERSION);
        }
    }

    public static long replaceUnsplashPhoto(List<UnsplashPhoto> photos) {
        try (SQLiteDatabase database = unsplashPhotoDataBaseHelper.getWritableDatabase()) {
            Iterator<UnsplashPhoto> iterator = photos.iterator();
            long res = 0;
            while (iterator.hasNext()) {
                UnsplashPhoto photo = iterator.next();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_ID, photo.getId());
                contentValues.put(COLUMN_CREATED_TIME, photo.getCreated_at());
                contentValues.put(COLUMN_UPDATED_TIME, photo.getUpdated_at());
                contentValues.put(COLUMN_PROMOTED_TIME, photo.getPromoted_at());
                contentValues.put(COLUMN_WIDTH, photo.getWidth());
                contentValues.put(COLUMN_HEIGHT, photo.getHeight());
                contentValues.put(COLUMN_COLOR, photo.getColor());
                contentValues.put(COLUMN_DESCRIPTION, photo.getDescription());
                contentValues.put(COLUMN_CATEGORIES, listToString(photo.getCategories()));
                contentValues.put(COLUMN_LIKES, photo.getLikes());
                contentValues.put(COLUMN_VIEW, photo.getViews());
                contentValues.put(COLUMN_DOWNLOAD, photo.getDownloads());
                contentValues.put(COLUMN_PIC_RAW_URL, photo.getUrls().getRaw());
                contentValues.put(COLUMN_PIC_FULL_URL, photo.getUrls().getRaw());
                contentValues.put(COLUMN_PIC_REGULAR_URL, photo.getUrls().getRaw());
                contentValues.put(COLUMN_PIC_SMALL_URL, photo.getUrls().getRaw());
                contentValues.put(COLUMN_PIC_THUMB_URL, photo.getUrls().getRaw());
                contentValues.put(COLUMN_PIC_SMALL_S3_URL, photo.getUrls().getRaw());
                long replace = database.replace(TABLE_NAME, null, contentValues);
                res += (replace == -1 ? 0 : 1);
            }
            return res;
        }
    }

    public static List<UnsplashPhoto> getAllUnsplashPhotoList() {
        ArrayList<UnsplashPhoto> res = new ArrayList<>();
        try (SQLiteDatabase database = unsplashPhotoDataBaseHelper.getReadableDatabase()) {
            String[] columns = UnsplashPhotoDBContract.getUnsplashPhotoTableColumn();
            Cursor query = database.query(TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while (query.moveToNext()) {
                int columnIndex1 = query.getColumnIndex(columns[0]);
                int columnIndex2 = query.getColumnIndex(columns[1]);
                int columnIndex3 = query.getColumnIndex(columns[2]);
                int columnIndex4 = query.getColumnIndex(columns[3]);
                int columnIndex5 = query.getColumnIndex(columns[4]);
                int columnIndex6 = query.getColumnIndex(columns[5]);
                int columnIndex7 = query.getColumnIndex(columns[6]);
                int columnIndex8 = query.getColumnIndex(columns[7]);
                int columnIndex9 = query.getColumnIndex(columns[8]);
                int columnIndex10 = query.getColumnIndex(columns[9]);
                int columnIndex11 = query.getColumnIndex(columns[10]);
                int columnIndex12 = query.getColumnIndex(columns[11]);
                int columnIndex13 = query.getColumnIndex(columns[12]);
                int columnIndex14 = query.getColumnIndex(columns[13]);
                int columnIndex15 = query.getColumnIndex(columns[14]);
                int columnIndex16 = query.getColumnIndex(columns[15]);
                int columnIndex17 = query.getColumnIndex(columns[16]);
                int columnIndex18 = query.getColumnIndex(columns[17]);

                String value1 = query.getString(columnIndex1);
                String value2 = query.getString(columnIndex2);
                String value3 = query.getString(columnIndex3);
                String value4 = query.getString(columnIndex4);
                int value5 = query.getInt(columnIndex5);
                int value6 = query.getInt(columnIndex6);
                String value7 = query.getString(columnIndex7);
                String value8 = query.getString(columnIndex8);
                String value9 = query.getString(columnIndex9);
                int value10 = query.getInt(columnIndex10);
                int value11 = query.getInt(columnIndex11);
                int value12 = query.getInt(columnIndex12);
                String value13 = query.getString(columnIndex13);
                String value14 = query.getString(columnIndex14);
                String value15 = query.getString(columnIndex15);
                String value16 = query.getString(columnIndex16);
                String value17 = query.getString(columnIndex17);
                String value18 = query.getString(columnIndex18);

                UnsplashPhoto unsplashPhoto = new UnsplashPhoto();
                unsplashPhoto.setId(value1);
                unsplashPhoto.setCreated_at(value2);
                unsplashPhoto.setUpdated_at(value3);
                unsplashPhoto.setPromoted_at(value4);
                unsplashPhoto.setWidth(value5);
                unsplashPhoto.setHeight(value6);
                unsplashPhoto.setColor(value7);
                unsplashPhoto.setDescription(value8);
                unsplashPhoto.setCategories(stringToList(value9));
                unsplashPhoto.setLikes(value10);
                unsplashPhoto.setViews(value11);
                unsplashPhoto.setDownloads(value12);
                UnsplashUrl url = new UnsplashUrl();
                url.setRaw(value13);
                url.setFull(value14);
                url.setRegular(value15);
                url.setSmall(value16);
                url.setThumb(value17);
                url.setSmall_s3(value18);
                unsplashPhoto.setUrls(url);
                res.add(unsplashPhoto);
            }
        }
        return res;
    }


    private static String listToString(List<String> list) {
        if (list == null) {
            return null;
        }

        Iterator<String> iterator = list.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            String next = iterator.next();
            stringBuilder.append(next);
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    private static List<String> stringToList(String str) {
        if (TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }

        String[] split = str.split(",");
        return Arrays.asList(split);
    }

}
