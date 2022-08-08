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
                Log.i("ViewPager", "instantiateItem:" + position + "  " + newPosition);
                View view = views[newPosition];
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                int newPosition = position % views.length;
                Log.i("ViewPager", "destroyItem:" + position + " " + newPosition);
                container.removeView(views[newPosition]);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        };

        this.viewPager1.setAdapter(adapter);

    }
}