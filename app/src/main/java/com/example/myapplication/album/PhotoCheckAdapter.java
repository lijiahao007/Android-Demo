package com.example.myapplication.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoCheckAdapter extends RecyclerView.Adapter<PhotoCheckAdapter.PhotoCheckViewHolder> {

    List<MediaBean> list = new ArrayList<>();
    private Context mContext;

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<MediaBean> beans) {
        list.clear();
        list.addAll(beans);
        notifyDataSetChanged();
    }

    public void deleteItem(MediaBean bean) {
        int index = list.indexOf(bean);
        if (index != -1) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    @NonNull
    @Override
    public PhotoCheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new PhotoCheckViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.page_check_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoCheckViewHolder holder, int position) {
        Glide.with(mContext).load(list.get(position).getUri()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class PhotoCheckViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        View itemView;

        public PhotoCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            image = itemView.findViewById(R.id.iv_media_photo);
        }
    }
}
