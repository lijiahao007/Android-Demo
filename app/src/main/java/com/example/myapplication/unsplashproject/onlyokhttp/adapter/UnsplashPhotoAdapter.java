package com.example.myapplication.unsplashproject.onlyokhttp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ItemUnsplashPhotoBinding;
import com.example.myapplication.unsplashproject.onlyokhttp.entity.UnsplashPhoto;

import java.util.List;

public class UnsplashPhotoAdapter extends RecyclerView.Adapter<UnsplashPhotoAdapter.UnsplashPhotoViewHolder> {

    private List<UnsplashPhoto> mData;
    private Context mContext;

    public UnsplashPhotoAdapter(List<UnsplashPhoto> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public UnsplashPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new UnsplashPhotoViewHolder(ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnsplashPhotoViewHolder holder, int position) {
        UnsplashPhoto unsplashPhoto = mData.get(position);
        String full = unsplashPhoto.getUrls().getFull();
        Glide.with(holder.binding.ivPhoto)
                .load(full)
                .error(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.error, null))
                .into(holder.binding.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class UnsplashPhotoViewHolder extends RecyclerView.ViewHolder {

        ItemUnsplashPhotoBinding binding;

        public UnsplashPhotoViewHolder(@NonNull ItemUnsplashPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
