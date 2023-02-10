package com.example.myapplication.polygonseldemo;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityPolyGonSelDemoBinding;

import java.util.ArrayList;

public class PolyGonSelDemoActivity extends BaseActivity<ActivityPolyGonSelDemoBinding> {

    @Override
    protected int[] bindClick() {
        return new int[]{
                R.id.add_poly_region,
                R.id.add_rectangle_region,
                R.id.delete_curRegion,
                R.id.change_region,
        };
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.add_poly_region:
                binding.polyView.addPolygonRegionByUser();
                break;
            case R.id.add_rectangle_region:
                binding.polyView.addRectangleRegionByUser();
                break;
            case R.id.delete_curRegion:
                binding.polyView.deleteCurRegion();
                break;
            case R.id.change_region:
                binding.polyView.selectNextRegion();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}