package com.example.myapplication.viewpager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.R;
import com.example.myapplication.viewpager.fragment.FourFragment;
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
        ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
//                add(new ThreeFragment());
            add(new TwoFragment());
            add(new OneFragment());
            add(new TwoFragment());
            add(new OneFragment());
//                add(new ThreeFragment());
//                add(new OneFragment());
        }};
        FragmentPagerAdapter fragmentAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

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
                        fragmentViewPager.setCurrentItem(fragments.size()-2, false);
                    } else if (curPosition == fragments.size()-1) {
                        fragmentViewPager.setCurrentItem(0, false);
                    }
                }
            }
        });
    }

    private void initFragmentViewPager1() {

        // FragmentStatePagerAdapter 只会缓存当前显示页。其他只会保存状态。
        ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
            add(new OneFragment());
            add(new TwoFragment());
            add(new ThreeFragment());
        }};
        FragmentStatePagerAdapter fragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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

        fragmentViewPager1.setAdapter(fragmentStatePagerAdapter);
        fragmentViewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int curPosition = 0;
            boolean isLast = true;

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
                curPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // state
                // SCROLL_STATE_IDLE  不动
                // SCROLL_STATE_DRAGGING 拖拽
                // SCROLL_STATE_SETTLING 自动滑动
                Log.i(TAG, "onPageScrollStateChanged--" + "state:" + state);

                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    isLast = false;
                } else if (state == ViewPager.SCROLL_STATE_IDLE && isLast) {
                    if (fragments.size() >= 1 && curPosition == fragments.size() - 1) {
                        fragmentViewPager1.setCurrentItem(0);
                    } else if (fragments.size() >= 1 && curPosition == 0) {
                        fragmentViewPager1.setCurrentItem(fragments.size() - 1);
                    }
                } else if (state == ViewPager.SCROLL_STATE_DRAGGING){
                    isLast = true;
                }
            }
        });

    }

    private void initFragmentViewPager2() {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
                add(new OneFragment());
                add(new TwoFragment());
                add(new ThreeFragment());
                add(new FourFragment());
            }};


            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                Log.i("Fragment", "instantiateItem--" + "position:" + position);
                int newPosition = position % fragments.size();
                return super.instantiateItem(container, newPosition);
            }

        };

        fragmentViewPager2.setAdapter(fragmentPagerAdapter);
        fragmentViewPager2.setCurrentItem(1000, false);

        fragmentViewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

}