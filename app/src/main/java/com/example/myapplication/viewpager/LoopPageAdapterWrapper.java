package com.example.myapplication.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class LoopPageAdapterWrapper extends PagerAdapter {
    PagerAdapter mAdapter;

    public LoopPageAdapterWrapper(PagerAdapter pagerAdapter) {
        this.mAdapter = pagerAdapter;
    }


    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0) {
            return 0;
        }

        int realPosition = (position - 1) % realCount;
        if (realPosition < 0) {
            realPosition += realCount;
        }

        return realPosition;
    }

    public int toInnerPosition(int position) {
        return position + 1;
    }

    /**
     * 获取mAdapter中第一个元素，在Inner中的位置
     *
     * @return
     */
    private int getRealFirstPosition() {
        return 1;
    }

    private int getRealLastPosition() {
        return getRealFirstPosition() + getRealCount() - 1;
    }

    public PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    private int getRealCount() {
        return mAdapter.getCount();
    }

    @Override
    public int getCount() {
        return mAdapter.getCount() + 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return mAdapter.isViewFromObject(view, object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = toRealPosition(position);

        return mAdapter.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        int realPosition = toRealPosition(position);
        super.destroyItem(container, realPosition, object);
    }
}
