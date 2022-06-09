package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.recycleview.adaper.DemoAdapter;

import java.util.ArrayList;

public class GridRecyclerviewActivity extends AppCompatActivity {


    private DemoAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_recyclerview);

        // 1. recycleView 设置 Adapter （必须）
        adapter = new DemoAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        // 2. 设置LayoutManager
        layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        // 2.1 设置每个元素的spanSize
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        return 2;
                }
                return 1;
            }
        });

        // 2.2 增加列、减少列、增加元素、删除元素
        findViewById(R.id.btn_add_column).setOnClickListener(view -> {
            int spanCount = layoutManager.getSpanCount();
            layoutManager.setSpanCount(spanCount + 1);
        });

        findViewById(R.id.btn_sub_column).setOnClickListener(view -> {
            int spanCount = layoutManager.getSpanCount();
            if (spanCount > 2) {
                layoutManager.setSpanCount(spanCount - 1);
            }
        });

        findViewById(R.id.btn_add_item).setOnClickListener(view -> {
            adapter.addItem("new" + count++);
        });

        findViewById(R.id.btn_delete_item).setOnClickListener(view -> {
            adapter.removeItem(adapter.getItemCount()-1);
        });

        // 3. adapter设置数据 （必须）
        adapter.submitList(new ArrayList<String>() {{
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
        }});
    }
}