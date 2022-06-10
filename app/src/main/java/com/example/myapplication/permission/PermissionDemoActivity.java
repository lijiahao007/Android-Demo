package com.example.myapplication.permission;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Map;

public class PermissionDemoActivity extends AppCompatActivity {

    static final String[] MMSPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    static final int MMSRequestCode = 1;

    static final String[] LocationPermission = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    static final int LocationRequestCode = 2;
    private ActivityResultLauncher<String[]> locationPermissionLauncher;


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        locationPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                boolean flag = true;
                for (String key : result.keySet()) {
                    Boolean isGrant = result.get(key);
                    if (Boolean.FALSE.equals(isGrant)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    locationAction();
                } else {
                    Log.i("PermissionDemoActivity", "MMS权限未授权完成");
                }
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_demo);

        findViewById(R.id.btn_data_access_reason).setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW_PERMISSION_USAGE));
        });

        findViewById(R.id.btn_MMS).setOnClickListener(view -> {
            ArrayList<String> unGrantedPermission = new ArrayList<>();
            ArrayList<String> shouldShowPermission = new ArrayList<>();
            for (String permission : MMSPermission) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    // 显示权限使用原因Dialog
                    shouldShowPermission.add(permission);
                } else if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    // 申请的权限
                    unGrantedPermission.add(permission);
                }
            }

            if (unGrantedPermission.size() == 0 && shouldShowPermission.size() == 0) {
                mMSAction();
            } else if (unGrantedPermission.size() != 0){
                Log.i("PermissionDemoActivity", "ungrant");
                String[] permissions = new String[unGrantedPermission.size()];
                requestPermissions(unGrantedPermission.toArray(permissions), MMSRequestCode);
            } else  {
                new MaterialAlertDialogBuilder(this).
                        setMessage("需要读取手机存储空间权限！")
                        .setNegativeButton("拒绝", (dialog, which) -> {
                            dialog.cancel();
                        })
                        .setPositiveButton("设置", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + this.getPackageName()));
                            startActivity(intent);
                        })
                        .show();
            }
        });

        findViewById(R.id.btn_location).setOnClickListener(view -> {
            ArrayList<String> shouldShowPermission = new ArrayList<>();
            ArrayList<String> denyPermission = new ArrayList<>();

            for (String permission: LocationPermission) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    shouldShowPermission.add(permission);
                } else if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    denyPermission.add(permission);
                }
            }
            if (shouldShowPermission.size() == 0 && denyPermission.size() == 0) {
                locationAction();
            } else if (denyPermission.size() != 0) {
                locationPermissionLauncher.launch(denyPermission.toArray(new String[0]));
            } else {
                new MaterialAlertDialogBuilder(this).
                        setMessage("需要定位权限！")
                        .setNegativeButton("拒绝", (dialog, which) -> {
                            dialog.cancel();
                        })
                        .setPositiveButton("设置", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + this.getPackageName()));
                            startActivity(intent);
                        })
                        .show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MMSRequestCode) {
            boolean flag = true;
            for (int grantResult: grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                mMSAction();
            } else {
               Log.i("PermissionDemoActivity", "MMS权限未授权完成");
            }
        }
    }

    private void mMSAction() {
        Toast.makeText(this, "MMS", Toast.LENGTH_SHORT).show();
    }

    private void locationAction() {
        Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();

    }
}