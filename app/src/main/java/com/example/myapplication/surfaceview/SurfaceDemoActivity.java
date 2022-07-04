package com.example.myapplication.surfaceview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
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
    private TextureView textureView;

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
    private void init() {

        // 1. SurfaceView 简单使用
        surfaceView = findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        SurfaceHolder.Callback2 callback = new SurfaceHolder.Callback2() {
            Camera camera = null;

            @Override
            public void surfaceRedrawNeeded(@NonNull SurfaceHolder holder) {
                Log.i(TAG, "surfaceRedrawNeeded:" + holder);
            }

            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated:" + holder);
                camera = Camera.open();
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG, "surfaceChanged:" + holder + "," + format + "," + width + "," + height);
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                Log.i(TAG, "surfaceDestroyed:" + holder);
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                }
            }
        };
        holder.addCallback(callback);


        // 2. TextureView 简单使用
        textureView = findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            Camera camera = null;
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                camera = Camera.open();
                try {
                    camera.setPreviewTexture(surface);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                camera.stopPreview();
                camera.release();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}