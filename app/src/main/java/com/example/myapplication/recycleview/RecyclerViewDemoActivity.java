package com.example.myapplication.recycleview;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.databinding.ActivityRecycleView1Binding;

import java.util.ArrayList;

public class RecyclerViewDemoActivity extends BaseActivity<ActivityRecycleView1Binding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.menuRecyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Linear Recyclerview", LinearLayoutRecyclerviewActivity.class));
            add(new MenuAdapter.MenuInfo("Grid Recyclerview", GridRecyclerviewActivity.class));
            add(new MenuAdapter.MenuInfo("StaggeredGrid Recyclerview", StaggeredGridRecyclerviewActivity.class));
            add(new MenuAdapter.MenuInfo("Recyclerview Item Decoration", ItemDecorationActivity.class));
            add(new MenuAdapter.MenuInfo("RotateRecyclerView", RotateRecyclerViewActivity.class));
            add(new MenuAdapter.MenuInfo("常用方法", RecyclerNormalMethodActivity.class));
        }});

    }
}