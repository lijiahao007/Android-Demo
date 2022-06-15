package com.example.myapplication.album;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Random;

public class MediaBean {
    private static final Random random = new Random();
    long id;
    Uri uri;
    long timestamp;
    String fileName;
    MediaType type;
    int duration = 0; // 视频长度 单位：毫秒
    String date;

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "media_bean";
        public static final String URI = "uri";
        public static final String TIMESTAMP = "timestamp";
        public static final String FILENAME = "filename";
        public static final String TYPE = "type";
        public static final String DURATION = "duration";
        public static final String DATE = "date";
        public static final String[] ALL_COLUMN = {
                _ID,
                URI,
                TIMESTAMP,
                FILENAME,
                TYPE,
                DURATION,
                DATE
        };
    }

    public MediaBean(Uri uri, String fileName, MediaType type) {
        this.uri = uri;
        this.fileName = fileName;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
//        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
        this.date = "2022-06-" + String.format("%02d", (random.nextInt(15) + 1));
        Log.i("MediaBean", date);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "id=" + id +
                ", uri=" + uri +
                ", timestamp=" + timestamp +
                ", fileName='" + fileName + '\'' +
                ", type=" + type +
                ", duration=" + duration +
                '}';
    }
}
