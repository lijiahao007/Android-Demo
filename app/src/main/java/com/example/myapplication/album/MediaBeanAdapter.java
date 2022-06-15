package com.example.myapplication.album;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MyGlideAppModule;
import com.example.myapplication.R;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MediaBeanAdapter extends RecyclerView.Adapter<MediaBeanAdapter.MediaViewHolder> {

    List<MediaBean> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<MediaBean> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final ImageView ivPhoto;
        private View itemView;

        public MediaViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPhoto = itemView.findViewById(R.id.iv_media_photo);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

        public void bind(MediaBean bean) {
            if (bean.getType() == MediaType.VIDEO) {
                Glide.with(itemView)
                        .setDefaultRequestOptions(new RequestOptions().frame(0).centerCrop())
                        .load(bean.getUri())
                        .into(ivPhoto);
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(getVideoDuration(bean.getDuration()));

            } else if (bean.getType() == MediaType.IMAGE) {
                Glide.with(itemView)
                        .load(bean.getUri())
                        .into(ivPhoto);

                tvTime.setVisibility(View.GONE);
            }
        }

        private String getVideoDuration(int mills) {
            Duration duration = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                duration = Duration.ofMillis(mills);
                long hour = duration.toHours();
                long minute = duration.toMinutes() % 60;
                long second = (duration.toMillis() / 1000) % 60;
                String res = "";
                if (hour == 0) {
                    res = String.format("%02d:%02d", minute, second);
                } else {
                    res = String.format("%02d:%02d", hour, minute);
                }
                return res;
            } else {
              return null;
            }
        }
    }
}
