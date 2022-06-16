package com.example.myapplication.album;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MyGlideAppModule;
import com.example.myapplication.R;

import java.io.File;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaBeanAdapter extends RecyclerView.Adapter<MediaBeanAdapter.MediaViewHolder> {

    private final List<MediaBean> list = new ArrayList<>();
    HashMap<String, List<MediaBean>> dateMediaMap = new HashMap<>();
    boolean[] isCheck;

    private RecyclerView recyclerView;
    private boolean isEditMode = false;
    private boolean isSelectAll = false;
    public static final String MEDIA_BEAN_LIST = "media_bean_list";
    public static final String CLICK_POSITION = "click_position";


    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<MediaBean> newList) {
        this.list.clear();
        this.list.addAll(newList);

        for (MediaBean bean : newList) {
            long timeStamp = bean.getTimestamp();
            String date = bean.getDate();
            List<MediaBean> dateList;
            if (dateMediaMap.containsKey(date)) {
                dateList = dateMediaMap.get(date);
            } else {
                dateList = new ArrayList<>();
                dateMediaMap.put(date, dateList);
            }

            dateList.add(bean);
        }
        isCheck = new boolean[newList.size()];
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.recyclerView = (RecyclerView) parent;
        Log.i("MediaViewHolder", String.valueOf(recyclerView.getWidth()));
        return new MediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        holder.bind(list.get(position));
        View itemView = holder.itemView;
        MediaBean bean = list.get(position);

        itemView.setOnClickListener(view -> {
            if (isEditMode) {
                holder.rbSelected.setChecked(!holder.rbSelected.isChecked());
                isCheck[position] = holder.rbSelected.isChecked();
            } else {
                // 点击跳转图片查看Activity
                File file = new File(bean.getFileName());
                boolean exists = file.exists();
                if (!exists) {
                    Toast.makeText(recyclerView.getContext(), "该文件不存在", Toast.LENGTH_SHORT).show();
                    int index = list.indexOf(bean);
                    list.remove(index);
                    notifyItemRemoved(index);
                    return;
                }

                Intent intent = new Intent(recyclerView.getContext(), PhotoCheckActivity.class);
                intent.putParcelableArrayListExtra(MEDIA_BEAN_LIST, (ArrayList<? extends Parcelable>) list);
                intent.putExtra(CLICK_POSITION, position);
                recyclerView.getContext().startActivity(intent);
            }
        });

        if (isEditMode) {
            holder.rbSelected.setVisibility(View.VISIBLE);
        } else {
            holder.rbSelected.setVisibility(View.GONE);
            isSelectAll = false;
        }

        if (isSelectAll) {
            holder.rbSelected.setChecked(true);
        } else {
            holder.rbSelected.setChecked(false);
        }


    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyVisibleItemChange();
    }

    public void setSelectAll(boolean isSelectAll) {
        this.isSelectAll = isSelectAll;
        if (isSelectAll) {
            for (int i = 0; i < list.size(); i++) {
                isCheck[i] = true;
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                isCheck[i] = false;
            }
        }
        notifyVisibleItemChange();
    }

    public void deleteItems(List<MediaBean> beans) {
        for (MediaBean bean : beans) {
            int index = list.indexOf(bean);
            String date = bean.getDate();
            List<MediaBean> mediaBeans = dateMediaMap.get(date);

            if (mediaBeans == null) return;

            mediaBeans.remove(bean);
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final ImageView ivPhoto;
        private final RadioButton rbSelected;
        private View itemView;

        public MediaViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPhoto = itemView.findViewById(R.id.iv_media_photo);
            tvTime = itemView.findViewById(R.id.tv_time);
            rbSelected = itemView.findViewById(R.id.rb_selected);
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


    private void notifyVisibleItemChange() {
        View firstChild = recyclerView.getChildAt(0);
        int childCount = recyclerView.getChildCount();
        int firstPos = recyclerView.getChildAdapterPosition(firstChild);
        notifyItemRangeChanged(firstPos, childCount);
    }


    public boolean isFirstInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        if (childAdapterPosition == -1) { // 在view对应的数据删除后，这个view仍会存在一小部分时间。
            return false;
        }

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

    public boolean isInFirstLineInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        if (childAdapterPosition == -1) {
            return false;
        }

        MediaBean bean = list.get(childAdapterPosition);
        List<MediaBean> mediaBeans = dateMediaMap.get(bean.getDate());
        if (mediaBeans != null) {
            int index = mediaBeans.indexOf(bean);
            return index <= 2;
        }
        return false;
    }

    public boolean isLastInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        if (childAdapterPosition == -1) {
            return false;
        }

        return isLastInDate(childAdapterPosition);
    }

    public boolean isLastInDate(int position) {
        MediaBean bean = list.get(position);
        List<MediaBean> mediaBeans = dateMediaMap.get(bean.getDate());
        if (mediaBeans != null) {
            int index = mediaBeans.indexOf(bean);
            if (index == mediaBeans.size() - 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isLastLineInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        if (childAdapterPosition == -1) {
            return false;
        }
        MediaBean bean = list.get(childAdapterPosition);
        List<MediaBean> mediaBeans = dateMediaMap.get(bean.getDate());
        int size = mediaBeans.size();
        int index = mediaBeans.indexOf(bean);
        if (size % 3 == 0) {
            if (index >= size - 3 && index <= size - 1) {
                return true;
            }
        } else if (size % 3 == 1) {
            if (index == size - 1) {
                return true;
            }
        } else if (size % 3 == 2) {
            if (index == size - 1 || index == size - 2) {
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

    public List<MediaBean> getCheckedBean() {
        List<MediaBean> res = new ArrayList<>();
        for (int i = 0; i < isCheck.length; i++) {
            if (isCheck[i]) {
                res.add(list.get(i));
            }
        }
        return res;
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
            if (childDate.equals(date) && bottom > maxBottom) {
                maxBottom = bottom;
                targetView = child;
            }
        }
        return targetView;
    }
}
