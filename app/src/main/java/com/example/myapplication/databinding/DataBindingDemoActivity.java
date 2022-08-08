package com.example.myapplication.databinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import com.example.myapplication.R;

public class DataBindingDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDataBindingDemoBinding inflate = ActivityDataBindingDemoBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
    }
}