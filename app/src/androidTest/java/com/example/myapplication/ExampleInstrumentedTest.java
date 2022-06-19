package com.example.myapplication;

import static android.os.storage.StorageManager.ACTION_CLEAR_APP_CACHE;
import static android.os.storage.StorageManager.ACTION_MANAGE_STORAGE;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context appContext;
    private ContentResolver contentResolver;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myapplication", appContext.getPackageName());
        Log.i("test", String.valueOf(appContext));
    }


    @Before
    public void init() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        contentResolver = appContext.getContentResolver();
    }

    @Test
    public void test6() {
        ArrayList<MediaBean> list1 = new ArrayList<>();
        list1.add(new MediaBean(null, "", MediaType.IMAGE));
        ArrayList<? extends Parcelable> list2 = (ArrayList<? extends Parcelable>) list1;
        ArrayList<MediaBean> list3 = (ArrayList<MediaBean>) list2;

        System.out.println(list3.get(0));
        Log.i("test6", list3.get(0).toString());
    }

    @Test
    public void fileTest() {
        File cacheDir = appContext.getCacheDir();
        File filesDir = appContext.getFilesDir();
        File externalCacheDir = appContext.getExternalCacheDir();
        File externalFilesDir = appContext.getExternalFilesDir("");
        File externalFilesDir1 = appContext.getExternalFilesDir("abcdefg");
        File externalFilesDir2 = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File externalStorageDirectory = Environment.getExternalStorageDirectory();
//        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(null);
        File externalStoragePublicDirectory1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        try {
            StorageManager storageManager = appContext.getSystemService(StorageManager.class);
            UUID uuidForPath = storageManager.getUuidForPath(appContext.getFilesDir());
            long allocatableBytes = storageManager.getAllocatableBytes(uuidForPath);
            Log.i("fileTest", "allocatableGB:" + allocatableBytes / 1024 / 1024 / 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(ACTION_CLEAR_APP_CACHE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);

        Log.i("fileTest", "cacheDir:" + cacheDir.getAbsolutePath());
        Log.i("fileTest", "filesDir:" + filesDir.getAbsolutePath());
        Log.i("fileTest", "externalCacheDir:" + externalCacheDir.getAbsolutePath());
        Log.i("fileTest", "externalFilesDir:" + externalFilesDir.getAbsolutePath());
        Log.i("fileTest", "externalFilesDir1:" + externalFilesDir1.getAbsolutePath());
        Log.i("fileTest", "externalFilesDir2:" + externalFilesDir2.getAbsolutePath());
        Log.i("fileTest", "externalStorageDirectory:" + externalStorageDirectory.getAbsolutePath());
        Log.i("fileTest", "externalStoragePublicDirectory:" + externalStoragePublicDirectory1.getAbsolutePath());
    }


    @Test
    public void mediaTest() {
        String[] columns = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.VOLUME_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.RELATIVE_PATH,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA

        };
        Cursor query = appContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null, null, null
        );

        Log.i("mediaTest", String.valueOf(query.getCount()));
        query.moveToFirst();
        do {
            String id = query.getString(query.getColumnIndex(MediaStore.Images.Media._ID));
            String AUTHOR = query.getString(query.getColumnIndex(MediaStore.Images.Media.VOLUME_NAME));
            String DESCRIPTION = query.getString(query.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String DURATION = query.getString(query.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH));
            String SIZE = query.getString(query.getColumnIndex(MediaStore.Images.Media.SIZE));
            String DATA = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));
            Log.i("mediaTest", id + "," + AUTHOR + "," + DESCRIPTION + "," + DURATION + "," + SIZE + ", " + DATA + ", " + uri);

        } while (query.moveToNext());
    }

    @Test
    public void insertImageTest() {
        Uri uri = null;
        Cursor query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.SIZE
                },
                MediaStore.Images.Media.DATA + " LIKE ?",
                new String[]{"%Screenshot_2022-06-18-10-24%"},
                null
        );
        while (query.moveToNext()) {
            String id = query.getString(query.getColumnIndex(MediaStore.Images.Media._ID));
            String DATA = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
            String DISPLAY_NAME = query.getString(query.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String EXPOSURE_TIME = query.getString(query.getColumnIndex(MediaStore.Images.Media.SIZE));
            uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));
            Log.i("mediaTest", id + "," + DATA + "," + DISPLAY_NAME + ", " + uri + "," + EXPOSURE_TIME);
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "hello.jpg");
        values.put(MediaStore.Images.Media.AUTHOR, "李嘉浩");

        Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.i("mediaTest", String.valueOf(insert));

        try {
            OutputStream outputStream = contentResolver.openOutputStream(insert);
            InputStream inputStream = contentResolver.openInputStream(uri);
            byte[] buffer = new byte[1024];
            int byteNum = 0;
            while ((byteNum = inputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, byteNum);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cursor query1 = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.SIZE
                },
                null, null, null
        );

        while (query1.moveToNext()) {
            String id = query1.getString(query1.getColumnIndex(MediaStore.Images.Media._ID));
            String DATA = query1.getString(query1.getColumnIndex(MediaStore.Images.Media.DATA));
            String DISPLAY_NAME = query1.getString(query1.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String EXPOSURE_TIME = query1.getString(query1.getColumnIndex(MediaStore.Images.Media.SIZE));
            uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));
            Log.i("mediaTest", id + "," + DATA + "," + DISPLAY_NAME + ", " + uri + "," + EXPOSURE_TIME);
        }

    }

    @Test
    public void documentApiTest() {
        Uri pickerInitialUri = Uri.parse("content://media/external/images/media");

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpg");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        appContext.startActivity(intent);

    }

}