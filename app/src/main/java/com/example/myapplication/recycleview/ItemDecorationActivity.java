package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.recycleview.adaper.DemoAdapter;
import com.example.myapplication.recycleview.adaper.ItemDecorationDemoAdapter;
import com.example.myapplication.recycleview.itemdecoration.PaddingItemDecoration;
import com.example.myapplication.recycleview.itemdecoration.SpecialItemDecoration;

import java.util.ArrayList;

public class ItemDecorationActivity extends AppCompatActivity {

    private ItemDecorationDemoAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private int count = 1;
    private PaddingItemDecoration paddingItemDecoration;
    private boolean isPaddingItemDecoration = false;
    private boolean isSpecialItemDecoration = false;
    private SpecialItemDecoration specialItemDecoration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_decoration);

        // 1. recycleView 设置 Adapter （必须）
        adapter = new ItemDecorationDemoAdapter();
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

        // 4. 设置ItemDecoration
        paddingItemDecoration = new PaddingItemDecoration(this);
        findViewById(R.id.btn_border).setOnClickListener(view -> {
            if (!isPaddingItemDecoration) {
                recyclerView.addItemDecoration(paddingItemDecoration);
                isPaddingItemDecoration = true;
            }
        });

        specialItemDecoration = new SpecialItemDecoration(this);
        findViewById(R.id.btn_style_2).setOnClickListener(view -> {
            if (!isSpecialItemDecoration) {
                recyclerView.addItemDecoration(specialItemDecoration);
                isSpecialItemDecoration = true;
            }
        });

        findViewById(R.id.btn_clear).setOnClickListener(view -> {
            recyclerView.removeItemDecoration(paddingItemDecoration);
            recyclerView.removeItemDecoration(specialItemDecoration);
            isPaddingItemDecoration = false;
            isSpecialItemDecoration = false;
        });



    }
}