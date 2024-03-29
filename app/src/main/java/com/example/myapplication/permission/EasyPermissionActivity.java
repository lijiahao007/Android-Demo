package com.example.myapplication.permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class EasyPermissionActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private static final int RC_CAMERA_AND_LOCATION = 1;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_permission);

        btn = findViewById(R.id.btn_location_camera);
        btn.setOnClickListener(view -> {
            methodRequiresTwoPermission();
        });

    }

    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            Toast.makeText(this, "授权完成", Toast.LENGTH_SHORT).show();
        } else {
            // 1.
//            EasyPermissions.requestPermissions(this, "我要摄像机和定位权限",
//                    RC_CAMERA_AND_LOCATION, perms);

//            2.
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
                            .setRationale("我要摄像机和定位权限")
                            .setPositiveButtonText("设置")
                            .setNegativeButtonText("取消")
                            .build());
        }
    }


    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION) // 在权限全部获取后自动调用该注解注释的函数 (只会在全部授权后调用一次)
    private void action () {
        Snackbar.make(btn, "AfterPermissionGranted", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 每个权限允许后，会回调该方法
        for (String perm : perms) {
            Log.i("PermissionActivity", "granted:" + perm);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // 每个权限拒绝后，会回调该方法
        for (String perm : perms) {
            Log.i("PermissionActivity", "denied:" + perm);
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("somePermissionPermanentlyDenied")
                    .setRationale("!!!")
                    .build()
                    .show();
        }
    }


    @Override
    public void onRationaleAccepted(int requestCode) {
        // 第一次拒绝后再次请求的请求框，点击确定后
        Log.i("PermissionActivity", "onRationaleAccepted:" + requestCode);

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        // 第一次拒绝后再次请求的请求框，点击取消后
        Log.i("PermissionActivity", "onRationaleDenied:" + requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 对话框的返回值
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "从设置返回", Toast.LENGTH_SHORT)
                    .show();
        }
    }


}