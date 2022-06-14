package com.example.myapplication.album;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class PhotoVideoDemoActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final String[] permissions = {
            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private final int PERMISSION_REQUEST_CODE = 1;
    private Button btnVideo;
    private Button btnPhoto;
    private ImageView ivShow1;
    private ImageView ivShow;
    private VideoView vvShow;
    private String curImageFilePath = null;
    private Uri curImageUri = null;

    ActivityResultLauncher<Void> photoBtnLaunch = registerForActivityResult(new ActivityResultContract<Void, Bitmap>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
                    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File tmpFile = File.createTempFile(timeStamp, ".jpg", storageDir);
                    Uri uriForImage = FileProvider.getUriForFile(PhotoVideoDemoActivity.this, "com.android.application.fileprovider", tmpFile);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForImage);
                    curImageFilePath = tmpFile.getAbsolutePath();
                    curImageUri = uriForImage;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return takePhotoIntent;
        }

        @Override
        @Nullable
        public Bitmap parseResult(int resultCode, @Nullable Intent intent) {
            if (intent != null) {
                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            }
            return null;
        }
    }, new ActivityResultCallback<Bitmap>() {
        @Override
        public void onActivityResult(Bitmap result) {
            if (result != null) {
                // 图片没有存储
                ivShow.setImageBitmap(result);
            } else {
                Bitmap scaleBitmap = BitmapTools.getScaleBitmap(curImageFilePath, 550, 550);
                ivShow1.setImageBitmap(scaleBitmap);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_video_demo);
        if (EasyPermissions.hasPermissions(this, permissions)) {
            init();
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, PERMISSION_REQUEST_CODE, permissions)
                            .setNegativeButtonText("取消")
                            .setPositiveButtonText("设置")
                            .setRationale("需要【相机】、【读写拓展存储空间】权限")
                            .build()
            );
        }
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    private void init() {
        getView();
        initVideoView();
        initVideoBtn();
        initPhotoBtn();
    }

    private void getView() {
        vvShow = findViewById(R.id.vv_show);
        ivShow = findViewById(R.id.iv_show);
        ivShow1 = findViewById(R.id.iv_show1);
        btnPhoto = findViewById(R.id.btn_photo);
        btnVideo = findViewById(R.id.btn_video);
    }

    private void initVideoView() {
        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vvShow);
    }

    private void initVideoBtn() {

    }

    private void initPhotoBtn() {
        btnPhoto.setOnClickListener(view -> {
            photoBtnLaunch.launch(null);
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 永久拒绝的话就弹窗提醒
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("需要【相机】、【读写拓展存储空间】权限才可以完成本页面功能")
                    .setNegativeButton("拒绝")
                    .setPositiveButton("设置")
                    .build()
                    .show();
        }
    }
}