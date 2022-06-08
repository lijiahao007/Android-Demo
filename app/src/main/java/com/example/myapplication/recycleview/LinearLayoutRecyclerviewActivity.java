package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.recycleview.adaper.DemoAdapter;

public class LinearLayoutRecyclerviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_recyclerview);

        DemoAdapter adapter = new DemoAdapter();

    }
}