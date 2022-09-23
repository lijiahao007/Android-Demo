package com.example.myapplication.recycleview;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityRecyclerNormalMethodBinding;
import com.macrovideo.sdk.tools.LogUtils;

import java.util.ArrayList;

public class RecyclerNormalMethodActivity extends BaseActivity<ActivityRecyclerNormalMethodBinding> {

    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAdapter = new MyAdapter();
        binding.recyclerView.setAdapter(myAdapter);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        initData();
        initListener();

    }

    private void initListener() {
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.e(TAG, "layoutManager. findFirstCompletelyVisibleItemPosition = " + layoutManager.findFirstCompletelyVisibleItemPosition());
                    Log.i(TAG, "layoutManager. findFirstVisibleItemPosition = " + layoutManager.findFirstVisibleItemPosition());
                    Log.i(TAG, "layoutManager. findLastCompletelyVisibleItemPosition = " + layoutManager.findLastCompletelyVisibleItemPosition());
                    Log.i(TAG, "layoutManager. findLastVisibleItemPosition = " + layoutManager.findLastVisibleItemPosition());
                    Log.i(TAG, "layoutManager. getInitialPrefetchItemCount = " + layoutManager.getInitialPrefetchItemCount());
                    Log.i(TAG, "layoutManager. findViewByPosition = " + layoutManager.findViewByPosition(10) + "  " + (layoutManager.findViewByPosition(10) != null ? ((TextView) layoutManager.findViewByPosition(10).findViewById(R.id.tv_item_name)).getText().toString() : ""));
                    Log.i(TAG, "recyclerView. getChildAt = " + binding.recyclerView.getChildAt(10) + "  " + ((TextView) binding.recyclerView.getChildAt(10).findViewById(R.id.tv_item_name)).getText().toString());
                    Log.i(TAG, "recyclerView. findFocus = " + binding.recyclerView.findFocus());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private void scrollToCenter() {
        int centerPosition = binding.recyclerView.getHeight() / 2; // 中间位置

    }

    private void initData() {

        for (int i = 0; i < 100; i++) {
            mData.add("Item " + i);
        }
    }

    ArrayList<String> mData = new ArrayList<>();

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            if (mData.get(position) == null) {
                holder.itemView.setVisibility(View.INVISIBLE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.tvItemName.setText(mData.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvItemName;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItemName = itemView.findViewById(R.id.tv_item_name);
            }
        }
    }
}