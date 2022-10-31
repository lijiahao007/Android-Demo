package com.example.myapplication.viewpager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LoopViewPager extends ViewPager {

    LoopPageAdapterWrapper adapterWrapper;

    public LoopViewPager(@NonNull Context context) {
        super(context);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        adapterWrapper = new LoopPageAdapterWrapper(adapter);
        super.setAdapter(adapterWrapper);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        int innerPosition = adapterWrapper.toInnerPosition(item);
        super.setCurrentItem(innerPosition, smoothScroll);
    }


    @Override
    public void setCurrentItem(int item) {
        int innerPosition = adapterWrapper.toInnerPosition(item);
        super.setCurrentItem(innerPosition);
    }
}
