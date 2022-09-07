package com.example.myapplication.recycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.MenuAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityRecycleView1Binding;

import java.util.ArrayList;

public class RecyclerViewDemoActivity extends BaseActivity<ActivityRecycleView1Binding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view1);

        binding.menuRecyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Linear Recyclerview", LinearLayoutRecyclerviewActivity.class));
            add(new MenuAdapter.MenuInfo("Grid Recyclerview", GridRecyclerviewActivity.class));
            add(new MenuAdapter.MenuInfo("StaggeredGrid Recyclerview", StaggeredGridRecyclerviewActivity.class));
            add(new MenuAdapter.MenuInfo("Recyclerview Item Decoration", ItemDecorationActivity.class));

        }});
    }
}