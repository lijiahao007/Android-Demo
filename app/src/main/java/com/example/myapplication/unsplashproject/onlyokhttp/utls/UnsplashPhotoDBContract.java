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

import android.provider.BaseColumns;

import java.util.HashMap;

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
        public static final String COLUMN_HEIGHT = "height";
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

    public static String[] getUnsplashPhotoTableColumn() {
        return COLUMNS;
    }

    private final static String[] COLUMNS = new String[]{
            COLUMN_ID,
            COLUMN_CREATED_TIME,
            COLUMN_UPDATED_TIME,
            COLUMN_PROMOTED_TIME,
            COLUMN_WIDTH,
            COLUMN_HEIGHT,
            COLUMN_COLOR,
            COLUMN_DESCRIPTION,
            COLUMN_CATEGORIES,
            COLUMN_LIKES,
            COLUMN_VIEW,
            COLUMN_DOWNLOAD,
            COLUMN_PIC_RAW_URL,
            COLUMN_PIC_FULL_URL,
            COLUMN_PIC_REGULAR_URL,
            COLUMN_PIC_SMALL_URL,
            COLUMN_PIC_THUMB_URL,
            COLUMN_PIC_SMALL_S3_URL,
    };

}
