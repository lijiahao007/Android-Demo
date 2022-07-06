package com.example.myapplication.setupnet;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileUtils {
    public static void saveVideoInMediaStore(ContentResolver contentResolver, File tmpFile, boolean isDeleteFile) {
        if (tmpFile == null) return;

        new Thread() {
            @Override
            public void run() {
                super.run();
                Uri newVideo = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                OutputStream outputStream;
                try {
                    outputStream = contentResolver.openOutputStream(newVideo);
                    FileInputStream fileInputStream = new FileInputStream(tmpFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    fileInputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (isDeleteFile) {
                        boolean delete = tmpFile.delete();
                    }
                }
            }
        };

    }
}
