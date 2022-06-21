package com.example.myapplication.qrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;

public class CustomScanQrcodeActivity extends AppCompatActivity {

    private static final int SCAN_REQUEST_CODE = 1;
    private static final String TAG = "CustomScanQrcodeActivity";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan_qrcode);
//        TODO: 自定义扫码界面
        findViewById(R.id.btn_scan).setOnClickListener(view -> {
            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator()
                    .setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE)
                    .create();
            ScanUtil.startScan(this, SCAN_REQUEST_CODE, options);
        });


        findViewById(R.id.btn_scan1).setOnClickListener(view -> {
            // bitmap 数据扫码

            try {
                Bitmap bitmap = ScanUtil.buildBitmap("helloworld", HmsScan.QRCODE_SCAN_TYPE, 300, 300, null);
                HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator()
                        .setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE)
                        .setPhotoMode(true)
                        .create();
                HmsScan[] hmsScans = ScanUtil.decodeWithBitmap(this, bitmap, options);
                for (HmsScan scan : hmsScans) {
                    Log.i(TAG, String.valueOf(scan.getOriginalValue()));
                    Log.i(TAG, String.valueOf(scan.getScanType() == HmsScan.QRCODE_SCAN_TYPE));
                    Log.i(TAG, String.valueOf(scan.getScanTypeForm()));
                    Log.i(TAG, String.valueOf(scan.getBorderRect()));
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.btn_scan2).setOnClickListener(view -> {
            startActivity(new Intent(this, ScanActivity.class));
        });


    }


    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode);
        if (data != null && requestCode == SCAN_REQUEST_CODE) {
            HmsScan scan = data.getParcelableExtra(ScanUtil.RESULT);
            Log.i(TAG, String.valueOf(scan.getOriginalValue()));
            Log.i(TAG, String.valueOf(scan.getScanType() == HmsScan.QRCODE_SCAN_TYPE));
            Log.i(TAG, String.valueOf(scan.getScanTypeForm()));
            Log.i(TAG, String.valueOf(scan.getBorderRect()));
        }
    }
}