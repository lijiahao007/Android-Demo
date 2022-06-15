package com.example.myapplication.album;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MyGlideAppModule;
import com.example.myapplication.R;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaBeanAdapter extends RecyclerView.Adapter<MediaBeanAdapter.MediaViewHolder> {

    List<MediaBean> list = new ArrayList<>();
    HashMap<String, List<MediaBean>> dateMediaMap = new HashMap<>();

    private RecyclerView recyclerView;

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<MediaBean> newList) {
        this.list.addAll(newList);

        for (MediaBean bean: newList) {
            long timeStamp = bean.getTimestamp();
            String date = bean.getDate();
            List<MediaBean> dateList;
            if (dateMediaMap.containsKey(date)) {
                dateList = dateMediaMap.get(date);
            } else {
                dateList = new ArrayList<>();
                dateMediaMap.put(date, dateList);
            }

            int insertPos = -1;
            for (int i = 0; i < dateList.size()-1; i++) {
                MediaBean bean1 = dateList.get(i);
                MediaBean bean2 = dateList.get(i + 1);
                if (bean1.getTimestamp() > timeStamp && bean2.getTimestamp() < timeStamp) {
                    insertPos = i+1;
                    break;
                }
            }
            if (dateList.size() == 0 || dateList.get(0).getTimestamp() < timeStamp) {
                insertPos = 0;
            } else if (dateList.get(dateList.size()-1).getTimestamp() > timeStamp) {
                insertPos = dateList.size();
            }
            dateList.add(insertPos, bean);
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.recyclerView = (RecyclerView) parent;
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

    public boolean isFirstInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        MediaBean bean = list.get(childAdapterPosition);
        List<MediaBean> mediaBeans = dateMediaMap.get(bean.getDate());
        if (mediaBeans != null) {
            int index = mediaBeans.indexOf(bean);
            if (index == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isLastInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        return isLastInDate(childAdapterPosition);
    }

    public boolean isLastInDate(int position) {
        MediaBean bean = list.get(position);
        List<MediaBean> mediaBeans = dateMediaMap.get(bean.getDate());
        if (mediaBeans != null) {
            int index = mediaBeans.indexOf(bean);
            if (index == mediaBeans.size()-1) {
                return true;
            }
        }
        return false;
    }

    public int getDateMediaSize(String date) {
        List<MediaBean> mediaBeans = dateMediaMap.get(date);
        if (mediaBeans != null) {
            return mediaBeans.size();
        }
        return -1;
    }

    public int getDateMediaSize(int position) {
        MediaBean bean = list.get(position);
        return getDateMediaSize(bean.getDate());
    }

    @Nullable
    public View getLowestView(String date) {
        // 获取可见View中，同一天最下方的View的
        int childCount = recyclerView.getChildCount();
        int maxBottom = -1;
        View targetView = null;
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            String childDate = (String) child.getTag();
            int bottom = child.getBottom();
            if (childDate.equals(date) && bottom < maxBottom) {
                maxBottom = bottom;
                targetView = child;
            }
        }
        return targetView;
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
                        .thumbnail(0.25f)
                        .into(ivPhoto);
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(getVideoDuration(bean.getDuration()));

            } else if (bean.getType() == MediaType.IMAGE) {
                Glide.with(itemView)
                        .load(bean.getUri())
                        .thumbnail(0.25f)
                        .into(ivPhoto);

                tvTime.setVisibility(View.GONE);
            }

            itemView.setTag(bean.getDate());
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
