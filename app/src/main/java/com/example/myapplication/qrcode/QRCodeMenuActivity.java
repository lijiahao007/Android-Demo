package com.example.myapplication.qrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

public class QRCodeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_menu);
        findViewById(R.id.demo1).setOnClickListener(view -> {
            startActivity(new Intent(this, HuaweiQRCodeActivity.class));
        });

        findViewById(R.id.demo2).setOnClickListener(view -> {
            startActivity(new Intent(this, HuaweiQRCode2Activity.class));
        });

        findViewById(R.id.scan_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, CustomScanQrcodeActivity.class));
        });
    }
}