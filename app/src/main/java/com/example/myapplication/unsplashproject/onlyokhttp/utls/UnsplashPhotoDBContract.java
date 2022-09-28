package com.example.myapplication.unsplashproject.onlyokhttp.utls;

import android.provider.BaseColumns;

public final class UnsplashPhotoDBContract {
    private UnsplashPhotoDBContract() {
    }

    public static class UnsplashPhotoEntry implements BaseColumns {
        public static final String TABLE_NAME = "unsplash_photo";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CREATED_TIME = "created_at";
        public static final String COLUMN_UPDATED_TIME = "updated_at";
        public static final String COLUMN_PROMOTED_TIME = "promoted_at";
        public static final String COLUMN_WIDTH = "width";
        public static final String COLUMN_HEIGHT = "updated_at";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CATEGORIES = "categories";
        public static final String COLUMN_LIKES = "likes";
        public static final String COLUMN_VIEW = "views";
        public static final String COLUMN_DOWNLOAD = "downloads";
        public static final String COLUMN_PIC_RAW_URL = "raw";
        public static final String COLUMN_PIC_FULL_URL = "full";
        public static final String COLUMN_PIC_REGULAR_URL = "regular";
        public static final String COLUMN_PIC_SMALL_URL = "small";
        public static final String COLUMN_PIC_THUMB_URL = "thumb";
        public static final String COLUMN_PIC_SMALL_S3_URL = "small_s3";
    }
}
