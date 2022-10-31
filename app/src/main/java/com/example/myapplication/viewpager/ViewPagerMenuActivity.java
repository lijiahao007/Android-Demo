package com.example.myapplication.viewpager;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MenuAdapter;
import com.example.myapplication.MenuRecyclerView;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ViewPagerMenuActivity extends AppCompatActivity {

    private MenuRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_menu);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Loop ViewPager1 with Fragment", ViewPager1Activity.class));
            add(new MenuAdapter.MenuInfo("Loop ViewPager1 with View", ViewPager1_2Activity.class));
            add(new MenuAdapter.MenuInfo("Loop ViewPager", LoopViewPagerDemoActivity.class));
        }});
    }
}