package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.recycleview.adaper.DemoAdapter;

import java.util.ArrayList;

public class LinearLayoutRecyclerviewActivity extends AppCompatActivity {

    private DemoAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_recyclerview);

        adapter = new DemoAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        adapter.submitList(new ArrayList<String>() {{
            add("hello");
            add("world");
            add("hello");
            add("kitty");
        }});


    }
}