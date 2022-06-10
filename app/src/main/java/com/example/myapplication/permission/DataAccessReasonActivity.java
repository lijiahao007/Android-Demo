package com.example.myapplication.permission;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;

import java.util.List;

public class DataAccessReasonActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_reason);

        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        for (PackageInfo info: installedPackages) {
            Log.i("Package", "info :" + info);
        }

    }
}