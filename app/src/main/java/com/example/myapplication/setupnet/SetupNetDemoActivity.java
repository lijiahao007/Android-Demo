package com.example.myapplication.setupnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.wifi.WifiDemoActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class SetupNetDemoActivity extends AppCompatActivity{

    private String[] permissions;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button btnWifi;
    private Button btnAP;
    private Button btnPreview;

    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO
            };
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO
            };
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_net_demo);
        if (EasyPermissions.hasPermissions(this, permissions)) {
            init();
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, PERMISSION_REQUEST_CODE, permissions)
                            .setRationale("需要位置权限、读写拓展存储空间权限、录音权限")
                            .setNegativeButtonText("取消")
                            .setPositiveButtonText("授权")
                            .build()
            );
        }
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    private void init() {
        initView();
    }

    private void initView() {
        btnWifi = findViewById(R.id.btn_wifi);
        btnAP = findViewById(R.id.btn_AP);
        btnPreview = findViewById(R.id.btn_preview);

        btnWifi.setOnClickListener(view -> {
            Intent intent = new Intent(this, WifiConnectActivity.class);
            startActivity(intent);
        });

        btnAP.setOnClickListener(view -> {
            Intent intent = new Intent(this, APConnectActivity.class);
            startActivity(intent);
        });

        btnPreview.setOnClickListener(view -> {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}