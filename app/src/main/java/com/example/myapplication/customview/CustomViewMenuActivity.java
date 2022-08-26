package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.MenuAdapter;
import com.example.myapplication.MenuRecyclerView;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CustomViewMenuActivity extends AppCompatActivity {

    private MenuRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_menu);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setMenuList(new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("canvas普通绘图操作", CustomViewDemoActivity.class));
            add(new MenuAdapter.MenuInfo("贝塞尔二阶曲线", Bessel2Activity.class));
            add(new MenuAdapter.MenuInfo("贝塞尔三阶曲线", Bessel3Activity.class));
            add(new MenuAdapter.MenuInfo("弹动的球", ElasticBallActivity.class));
            add(new MenuAdapter.MenuInfo("Matrix变换", MatrixPolyActivity.class));
            add(new MenuAdapter.MenuInfo("事件分发", ClickEventDispatcherActivity.class));
            add(new MenuAdapter.MenuInfo("圆角图片", RoundCornerActivity.class));
            add(new MenuAdapter.MenuInfo("TabLayout", TabLayoutDemoActivity.class));
        }});
    }
}