package com.example.myapplication.album;

import android.net.Uri;
import android.provider.BaseColumns;

public class MediaBean {
    long id;
    Uri uri;
    long timestamp;
    String fileName;
    MediaType type;
    int duration = 0; // 视频长度 单位：毫秒

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "media_bean";
        public static final String URI = "uri";
        public static final String TIMESTAMP = "timestamp";
        public static final String FILENAME = "filename";
        public static final String TYPE = "type";
        public static final String DURATION = "duration";
        public static final String[] ALL_COLUMN = {
                _ID,
                URI,
                TIMESTAMP,
                FILENAME,
                TYPE,
                DURATION
        };
    }

    public MediaBean(Uri uri, String fileName, MediaType type) {
        this.uri = uri;
        this.fileName = fileName;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
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