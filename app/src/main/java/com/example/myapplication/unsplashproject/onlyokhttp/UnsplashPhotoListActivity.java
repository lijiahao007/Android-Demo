package com.example.myapplication.unsplashproject.onlyokhttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityUsplashPhotoListBinding;
import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashPhoto;
import com.example.myapplication.unsplashproject.onlyokhttp.adapter.UnsplashPhotoAdapter;
import com.example.myapplication.unsplashproject.onlyokhttp.utls.DataBaseManager;
import com.example.myapplication.unsplashproject.onlyokhttp.utls.OkHttpUtils;
import com.macrovideo.sdk.tools.LogUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;

public class UnsplashPhotoListActivity extends BaseActivity<ActivityUsplashPhotoListBinding> {

    private ArrayList<UnsplashPhoto> unsplashPhotos;
    private UnsplashPhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        binding.rvPhotoList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        unsplashPhotos = new ArrayList<>();
        adapter = new UnsplashPhotoAdapter(unsplashPhotos);
        binding.rvPhotoList.setAdapter(adapter);
    }

    private void loadData() {
        OkHttpUtils.getRandomPhotos(3, new OkHttpUtils.UnsplashPhotoListCallback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull List<UnsplashPhoto> photos) {
                for (UnsplashPhoto photo : photos) {
                    Log.i(TAG, "photo = 【" + photo + "】");
                }
                long res = DataBaseManager.replaceUnsplashPhoto(photos);
                Log.i(TAG, "保存结果：size=" + res + "  photos.size=" + photos.size());

                mBaseActivityHandler.post(() -> {
                    // 将这些图片放到数据库中
                    unsplashPhotos.addAll(photos);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(@NonNull Call call, @NonNull Exception e) {
                mBaseActivityHandler.post(() -> {
                    showToast("Error");
                });
                e.printStackTrace();
                Log.e(TAG, "", e);
            }
        });
    }
}