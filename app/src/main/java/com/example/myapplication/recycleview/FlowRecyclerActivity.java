package com.example.myapplication.recycleview;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityFlowRecyclerBinding;
import com.example.myapplication.recycleview.adaper.DemoAdapter;
import com.example.myapplication.utils.DimenUtil;
import com.macrovideo.sdk.tools.LogUtils;

import java.util.ArrayList;
import java.util.Random;

public class FlowRecyclerActivity extends BaseActivity<ActivityFlowRecyclerBinding> {

    private DemoAdapter adapter;
    private ArrayList<String> data = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new DemoAdapter();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            int length = random.nextInt(20) + 1;
            for (int j = 0; j < length; j++) {
                stringBuilder.append((char) ('a' + random.nextInt(26)));
            }
            data.add(stringBuilder.toString());
        }

        adapter.submitList(data);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new FlowLayoutManager());

    }
}