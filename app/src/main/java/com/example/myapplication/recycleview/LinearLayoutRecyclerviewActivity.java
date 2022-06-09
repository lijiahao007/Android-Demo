package com.example.myapplication.recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;
import com.example.myapplication.recycleview.adaper.DemoAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class LinearLayoutRecyclerviewActivity extends AppCompatActivity {

    private DemoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_layout_recyclerview);

        // 1. recycleView 设置 Adapter （必须）
        adapter = new DemoAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        // 2. 设置LayoutManager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        // 3. adapter设置数据 （必须）
        adapter.submitList(new ArrayList<String>() {{
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello");add("kitty");
            add("hello");add("world");add("hello"); add("kitty");
        }});



        // 4. 设置滚动事件监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            final HashMap<Integer, String> state = new HashMap<Integer, String>() {{
                put(0, "静止没有滚动");
                put(1, "跟随手指滚动");
                put(2, "自动滚动");
            }};

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i("RecyclerviewActivity", "newState: " + state.get(newState));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.i("RecyclerviewActivity", "dx:" + dx + "\t" + "dy:" + dy);

                // 3.1 判断是否能够向上下滑动
                boolean down = recyclerView.canScrollVertically(1); // 向下滚动
                boolean up = recyclerView.canScrollVertically(-1); // 向上滚动
                boolean right = recyclerView.canScrollHorizontally(1); // 向右滚动
                boolean left = recyclerView.canScrollHorizontally(-1); // 向左滚动
                Log.i("RecyclerviewActivity", "down:" + down + "  up:" + up + "  right:" + right + "  left:" + left);

                int visibleItemCount = layoutManager.getChildCount(); // 可见item
                int totalItemCount = layoutManager.getItemCount(); // 总的item
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition(); // 可见第一个item的位置
                Log.i("RecyclerviewActivity", "visibleItemCount:" + visibleItemCount + "  totalItemCount:" + totalItemCount + " pastVisiblesItems:" + pastVisiblesItems);

            }
        });




    }
}