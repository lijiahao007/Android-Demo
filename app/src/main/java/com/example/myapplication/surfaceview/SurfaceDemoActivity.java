package com.example.myapplication.surfaceview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.myapplication.R;

import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SurfaceDemoActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private static final String TAG = "SurfaceDemoActivity";
    private final int RC_CAMERA_PERM = 123;

    String[] permission = {
            Manifest.permission.CAMERA
    };
    private Surface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_demo);

        if (EasyPermissions.hasPermissions(this, permission)) {
            init();
        } else {
            EasyPermissions.requestPermissions(this, "需要打开相机权限", RC_CAMERA_PERM, permission);
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void init () {
        surfaceView = findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        Camera camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SurfaceHolder.Callback2 callback = new SurfaceHolder.Callback2() {
            @Override
            public void surfaceRedrawNeeded(@NonNull SurfaceHolder holder) {
                Log.i(TAG, "surfaceRedrawNeeded:" + holder);
            }

            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated:" + holder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG, "surfaceChanged:" + holder + "," + format + "," + width + "," + height);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                Log.i(TAG, "surfaceDestroyed:" + holder);
            }
        };
        holder.addCallback(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}