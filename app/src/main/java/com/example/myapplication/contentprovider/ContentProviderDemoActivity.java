package com.example.myapplication.contentprovider;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.utils.LogView;

import java.util.TimeZone;

public class ContentProviderDemoActivity extends AppCompatActivity {



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider_demo);
        findViewById(R.id.calender_provider_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, CalenderProviderActivity.class));
        });
        findViewById(R.id.content_provider_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, CustomContentProviderActivity.class));
        });
        findViewById(R.id.sqlite_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, SQLiteDemoActivity.class));
        });
    }

}