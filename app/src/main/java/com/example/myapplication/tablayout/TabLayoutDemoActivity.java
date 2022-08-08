package com.example.myapplication.tablayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class TabLayoutDemoActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerFragmentAdapter adapter;
    private ViewPager2.PageTransformer pageTransformer;
    private ArrayList<Integer> iconId;

    private DemoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_demo);
        viewModel = new ViewModelProvider(this).get(DemoViewModel.class);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager2);

        // 1. 设置Adapter
        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        // 2. 使用TabLayoutMediator关联tablayout、viewPager2
        iconId = new ArrayList<Integer>() {{
            add(R.drawable.ic_one);
            add(R.drawable.ic_two);
            add(R.drawable.ic_three);
            add(R.drawable.ic_four);
            add(R.drawable.ic_five);
            add(R.drawable.ic_six);
        }};

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // 2.1 设置自定义tab布局。
                View root = LayoutInflater.from(TabLayoutDemoActivity.this).inflate(R.layout.tab_custom_view, null);
                ImageView icon = root.findViewById(R.id.iv_tab_icon);
                icon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), iconId.get(position), null));
                TextView text = root.findViewById(R.id.tv_tab_text);
                text.setText("Object" + (position + 1));
                tab.setCustomView(root);
            }
        });
        tabLayoutMediator.attach();


        // 3. 添加动画
        pageTransformer = new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                // position 是页面相对于当前页导航器中心位置的距离
                // 0是前面和中心
                // 1表示page在当前页右侧一页以上。
                // -1表示page在当前页左侧一页以上。
                float absPos = Math.abs(position);
                float scale;
                if (absPos > 1) {
                    scale = 0;
                } else {
                    scale = 1 - absPos;
                }
                page.setScaleX(scale);
                page.setScaleY(scale);
                Log.i("ViewPager2", "page:" + page.hashCode() + "  position:" + position);
            }
        };
        viewPager2.setPageTransformer(pageTransformer);
        // 4. 设置移动方向
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);



        viewModel.isChange.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("TabLayoutDemoActivity", "value:" + aBoolean);
            }
        });


        findViewById(R.id.btn_change_live_data).setOnClickListener(view -> {
            viewModel.isChange.setValue(Boolean.FALSE.equals(viewModel.isChange.getValue()));
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.i("ScreenSize", "height:" + metrics.heightPixels + " width:" + metrics.widthPixels);


    }

    public void pagerJumpTo(int index) {
        if (index >= 0 && index < adapter.getItemCount()) {
            viewPager2.setCurrentItem(index, true);
        }
    }
}