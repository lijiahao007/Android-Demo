package com.example.myapplication.album;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class ScreenShotRecordActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PHOTO_REQUEST_CODE = 1;
    private static final int RECORD_REQUEST_CODE = 2;
    private ImageView ivShow;
    private VideoView vvShow;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private final static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private ImageView ivShow1;
    String currentPhotoPath = null;
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot_record);


        if (EasyPermissions.hasPermissions(this, permissions)) {
            init();
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, PERMISSION_REQUEST_CODE, permissions)
                            .setRationale("需要权限")
                            .setPositiveButtonText("设置")
                            .setNegativeButtonText("取消")
                            .build());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    private void init() {
        ivShow = findViewById(R.id.iv_show);
        vvShow = findViewById(R.id.vv_show);
        ivShow1 = findViewById(R.id.iv_show1);

        findViewById(R.id.btn_screen_record).setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            ComponentName componentName = intent.resolveActivity(getPackageManager());
            if (componentName != null) {
                startActivityForResult(intent, RECORD_REQUEST_CODE);
            }
        });


        findViewById(R.id.btn_screen_shoot).setOnClickListener(view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {  // 返回可以处理该Intent的第一个activity名字，如果没有能够处理的Activity，则返回null
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(this,
                            "com.android.application.fileprovider",
                            photoFile);
                    Log.i("ScreenShotRecordActivit", "photoUri:" + photoURI);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
                }
            }
        });

        initVideoView();
    }


    private void initVideoView() {
        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(vvShow);
        vvShow.setMediaController(mediacontroller);

        vvShow.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        vvShow.setMediaController(mediacontroller);
                        mediacontroller.setAnchorView(vvShow);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null && requestCode == PHOTO_REQUEST_CODE) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            ivShow1.setImageBitmap(bitmap);
            Log.i("ScreenShotRecordActivit", "byteCount:" + String.valueOf(bitmap.getByteCount()));
            return;
        } else if (data == null) {
            return;
        }

        if (requestCode == PHOTO_REQUEST_CODE) {
            handlePhoto(data);
            Toast.makeText(this, "photo activity result", Toast.LENGTH_SHORT).show();
        } else if (requestCode == RECORD_REQUEST_CODE) {
            handleVideo(data);
            Toast.makeText(this, "video activity result", Toast.LENGTH_SHORT).show();
        } else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "权限设置完成", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, "else activity result", Toast.LENGTH_SHORT).show();
        }
    }


    private void handlePhoto(Intent data) {
        // 如果设置了保存位置，就不会返回缩略图
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data"); // 缩略图
//        Log.i("ScreenShotRecordActivit", " photo data:" + bitmap);
//        ivShow.setImageBitmap(bitmap);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); // 该路径是系统相册路径
//        File storageDIr = getExternalFilesDir(Environment.DIRECTORY_PICTURES); // 该路径在应用私有空间内
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void handleVideo(Intent data) {
        Uri data1 = data.getData();
        Log.i("ScreenShotRecordActivit", "  video data:" + data1);
        vvShow.setVideoURI(data1);
        vvShow.start();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String perm : perms) {
            stringBuilder.append(perm).append("\n");
        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("注意")
                    .setRationale("需要以下权限：" + stringBuilder.toString())
                    .build().show();
        }
    }

}