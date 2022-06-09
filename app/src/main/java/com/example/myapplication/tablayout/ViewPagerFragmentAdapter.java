package com.example.myapplication.tablayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.tablayout.fragment.FiveFragment;
import com.example.myapplication.tablayout.fragment.FourFragment;
import com.example.myapplication.tablayout.fragment.OneFragment;
import com.example.myapplication.tablayout.fragment.SixFragment;
import com.example.myapplication.tablayout.fragment.ThreeFragment;
import com.example.myapplication.tablayout.fragment.TwoFragment;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
        add(new OneFragment());
        add(new TwoFragment());
        add(new ThreeFragment());
        add(new FourFragment());
        add(new FiveFragment());
        add(new SixFragment());
    }};

    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
