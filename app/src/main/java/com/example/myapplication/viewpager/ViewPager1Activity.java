package com.example.myapplication.viewpager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.example.myapplication.viewpager.fragment.OneFragment;
import com.example.myapplication.viewpager.fragment.ThreeFragment;
import com.example.myapplication.viewpager.fragment.TwoFragment;

import java.util.ArrayList;

public class ViewPager1Activity extends AppCompatActivity {

    private ViewPager fragmentViewPager;
    private ViewPager fragmentViewPager1;
    private String TAG = "ViewPager1Activity";
    private ViewPager fragmentViewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager1);

        fragmentViewPager = findViewById(R.id.fragment_view_pager);
        fragmentViewPager1 = findViewById(R.id.fragment_view_pager1);
        fragmentViewPager2 = findViewById(R.id.fragment_view_pager2);


        initFragmentViewPager();
        initFragmentViewPager1();
        initFragmentViewPager2();
    }



    private void initFragmentViewPager() {

        // 下面的FragmentPagerAdapter会缓存左右页
        FragmentPagerAdapter fragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
                add(new ThreeFragment());
                add(new OneFragment());
                add(new TwoFragment());
                add(new ThreeFragment());
                add(new OneFragment());
            }};

            @NonNull
            @Override
            public Fragment getItem(int position) {
                // 这个方法中，我们必须返回一个全局唯一的Fragment对象
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        fragmentViewPager.setAdapter(fragmentAdapter);
        fragmentViewPager.setCurrentItem(1, false);
        fragmentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int curPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 手往左滑的时候，positionOffset 由 0 -> 1
                // 手往右滑的时候，positionOffset 由 1 -> 0
                Log.i(TAG, "onPageScrolled--" + "position:" + position + " positionOffset:" + positionOffset + " positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                // 当页面被选中时调用
                Log.i(TAG, "onPageSelected--" + "position:" + position);
                this.curPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // state
                // SCROLL_STATE_IDLE  不动
                // SCROLL_STATE_DRAGGING 拖拽
                // SCROLL_STATE_SETTLING 自动滑动
                Log.i(TAG, "onPageScrollStateChanged--" + "state:" + state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (curPosition == 0) {
                        fragmentViewPager.setCurrentItem(3, false);
                    } else if (curPosition == 4) {
                        fragmentViewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
    }

    private void initFragmentViewPager1() {

        // FragmentStatePagerAdapter 只会缓存当前显示页。其他只会保存状态。
        FragmentStatePagerAdapter fragmentPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
                add(new OneFragment());
                add(new TwoFragment());
                add(new ThreeFragment());
            }};


            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.destroyItem(container, position, object);
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

        };

        fragmentViewPager1.setAdapter(fragmentPagerAdapter);
        fragmentViewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int curPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 手往左滑的时候，positionOffset 由 0 -> 1
                // 手往右滑的时候，positionOffset 由 1 -> 0
                Log.i(TAG, "onPageScrolled--" + "position:" + position + " positionOffset:" + positionOffset + " positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                // 当页面被选中时调用
                Log.i(TAG, "onPageSelected--" + "position:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // state
                // SCROLL_STATE_IDLE  不动
                // SCROLL_STATE_DRAGGING 拖拽
                // SCROLL_STATE_SETTLING 自动滑动
                Log.i(TAG, "onPageScrollStateChanged--" + "state:" + state);
            }
        });

    }

    private void initFragmentViewPager2() {
        PagerAdapter pagerAdapter = new PagerAdapter() {

            ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
                add(new OneFragment());
                add(new TwoFragment());
                add(new ThreeFragment());
            }};

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };
    }

}