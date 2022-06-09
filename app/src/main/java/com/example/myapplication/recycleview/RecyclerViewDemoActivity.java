package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;

public class RecyclerViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view1);
        findViewById(R.id.linear_recycler_view).setOnClickListener(view -> {
            startActivity(new Intent(this, LinearLayoutRecyclerviewActivity.class));
        });

        findViewById(R.id.grid_recycler_view).setOnClickListener(view -> {
            startActivity(new Intent(this, GridRecyclerviewActivity.class));
        });

        findViewById(R.id.staggered_recycler_view).setOnClickListener(view -> {
            startActivity(new Intent(this, StaggeredGridRecyclerviewActivity.class));
        });

        findViewById(R.id.item_decoration_view).setOnClickListener(view -> {
            startActivity(new Intent(this, ItemDecorationActivity.class));
        });
    }
}