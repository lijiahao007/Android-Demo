package com.example.myapplication.media;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.example.myapplication.R;

import java.io.File;

public class MediaDemoActivity extends AppCompatActivity {
    private static final int CREATE_FILE = 1;
    private static final int OPEN_FILE = 1;
    private static final int OPEN_DIR = 1;


    private static final String TAG = "MediaDemoActivity";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_demo);
        Uri pickerInitialUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        File appExternalFileDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Uri appExternalFileDirUri = FileProvider.getUriForFile(this, "com.android.application.fileprovider", appExternalFileDir);

        findViewById(R.id.btn_create_file).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");

            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

            startActivityForResult(intent, CREATE_FILE); // 返回结果 intent.getData() 可以获取新建文件的uri
        });

        findViewById(R.id.btn_open_file).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

            startActivityForResult(intent, OPEN_FILE); // 返回结果 intent.getData() 可以获取选择的文件
        });

        findViewById(R.id.btn_open_dir).setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
            startActivityForResult(intent, OPEN_DIR);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.i(TAG, "requestCode:" + requestCode + ", resultCode:" + resultCode + "," + data.getData());
        } else {
            Log.i(TAG, "requestCode:" + requestCode + ", resultCode:" + resultCode + ", null" );

        }

    }
}