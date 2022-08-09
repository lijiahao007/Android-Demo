package com.example.myapplication.viewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.example.myapplication.R;

public class ViewPager1_2Activity extends AppCompatActivity {

    private ViewPager viewPager1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager12);

        viewPager1 = findViewById(R.id.view_pager1);

        View[] views = new View[]{
                getLayoutInflater().inflate(R.layout.view_page, null),
                getLayoutInflater().inflate(R.layout.view_page, null),
                getLayoutInflater().inflate(R.layout.view_page, null),
        };

        // 用这个boolean数组来处理只有三个页面的情况。
        // TODO: 只有两个页面的情况下，会有问题，从第二个页面右滑后，要等一下那个页面才会加载。
        // TODO: 只有一个页面的情况下，无法正常切换。
        boolean[] isAttached = new boolean[views.length];

        for (View view : views) {
            ImageView ivPage = view.findViewById(R.id.iv_page);
            ivPage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.anim_run_1, null));
        }

        PagerAdapter adapter = new PagerAdapter() {


            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                int newPosition = position % views.length;
                View view = views[newPosition];
                // 如果已经该页面已经存在了，就先删除再添加。
                if (container.indexOfChild(view) != -1) {
                    container.removeView(view);
                    isAttached[newPosition] = true;
                } else {
                    isAttached[newPosition] = false;
                }
                container.addView(view);

                Log.i("ViewPager", "instantiateItem:" + position + "  " + newPosition + " " + isAttached[newPosition]);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                int newPosition = position % views.length;
                Log.i("ViewPager", "destroyItem:" + position + " " + newPosition + " " + isAttached[newPosition]);
                if (!isAttached[newPosition]) {
                    container.removeView(views[newPosition]);
                }
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        };

        this.viewPager1.setAdapter(adapter);

    }
}