package com.example.myapplication.databinding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DataBindingMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding_menu);

        findViewById(R.id.view_bind_demo).setOnClickListener(view -> {
            startActivity(new Intent(this, DataBindingDemoActivity.class));
        });



    }
}