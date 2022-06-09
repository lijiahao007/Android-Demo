package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.recycleview.adaper.DemoAdapter;
import com.example.myapplication.recycleview.adaper.StaggeredGridDemoAdapter;

import java.util.ArrayList;
import java.util.Random;

public class StaggeredGridRecyclerviewActivity extends AppCompatActivity {

    private StaggeredGridDemoAdapter adapter;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid_recyclervie);

        // 1. recycleView 设置 Adapter （必须）
        adapter = new StaggeredGridDemoAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        // 2. 设置LayoutManager
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        // 3. adapter设置数据 （必须）
        ArrayList<String> list = new ArrayList<>();
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            int rand = random.nextInt(7);
            for (int j = 0; j <= rand; j++) {
                stringBuilder.append("hello\n");
            }
            Log.i("Staggered", stringBuilder + "  rand:" + rand);
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            list.add(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
        }

        adapter.submitList(list);
    }
}