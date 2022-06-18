package com.example.myapplication.album.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.album.VideoCheckActivity;
import com.example.myapplication.album.bean.MediaBean;
import com.example.myapplication.album.bean.MediaType;
import com.example.myapplication.album.PhotoCheckActivity;
import com.example.myapplication.album.viewmodel.AlbumViewModel;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class MediaBeanAdapter extends RecyclerView.Adapter<MediaBeanAdapter.MediaViewHolder> {

    private final List<MediaBean> list = new ArrayList<>();
    private final AlbumViewModel viewModel;
    HashMap<String, List<MediaBean>> dateMediaMap = new HashMap<>();
    ArrayList<Boolean> isCheck = new ArrayList<>();

    private RecyclerView recyclerView;
    private boolean isEditMode = false;
    private boolean isSelectAll = false;
    private boolean isDeselectAll = false;
    public static final String MEDIA_BEAN_LIST = "media_bean_list";
    public static final String CLICK_POSITION = "click_position";
    public static final int CHECK_PHOTO = 10086;
    public static final int CHECK_VIDEO = 10087;

    private Fragment fragment;

    public MediaBeanAdapter(Fragment fragment) {
        this.fragment = fragment;
        viewModel = new ViewModelProvider(fragment.requireActivity()).get(AlbumViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<MediaBean> newList) {
        this.list.clear();
        this.list.addAll(newList);
        dateMediaMap.clear();
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
        isCheck.clear();
        for (int i = 0; i < newList.size(); i++) {
            isCheck.add(Boolean.FALSE);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Recyclerview", "onCreateViewHolder");
        this.recyclerView = (RecyclerView) parent;
        Log.i("MediaViewHolder", String.valueOf(recyclerView.getWidth()));
        return new MediaViewHolder(fragment, LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Log.i("Recyclerview", "onBindViewHolder:" + position);
        holder.bind(list.get(position));

        View itemView = holder.itemView;
        MediaBean bean = list.get(position);
        itemView.setOnClickListener(view -> {
            if (isEditMode) {
                holder.rbSelected.setChecked(!holder.rbSelected.isChecked());
                isCheck.set(holder.getAdapterPosition(), holder.rbSelected.isChecked());
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

                if (bean.getType() == MediaType.IMAGE) {
                    Intent intent = new Intent(fragment.requireContext(), PhotoCheckActivity.class);
                    intent.putParcelableArrayListExtra(MEDIA_BEAN_LIST, (ArrayList<? extends Parcelable>) list);
                    intent.putExtra(CLICK_POSITION, list.indexOf(bean));
                    fragment.requireActivity().startActivityForResult(intent, CHECK_PHOTO);
                } else if (bean.getType() == MediaType.VIDEO) {
                    Intent intent = new Intent(fragment.requireContext(), VideoCheckActivity.class);
                    intent.putParcelableArrayListExtra(MEDIA_BEAN_LIST, (ArrayList<? extends Parcelable>) list);
                    intent.putExtra(CLICK_POSITION, list.indexOf(bean));
                    fragment.requireActivity().startActivityForResult(intent, CHECK_VIDEO);
                }
            }
        });

        if (isEditMode) {
            holder.rbSelected.setVisibility(View.VISIBLE);
        } else {
            holder.rbSelected.setVisibility(View.GONE);
            holder.rbSelected.setChecked(false);
        }

        if (isSelectAll) {
            holder.rbSelected.setChecked(true);
        }

        if (isDeselectAll) {
            holder.rbSelected.setChecked(false);
        }

        holder.rbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("onCheckedChanged", MediaBeanAdapter.this.hashCode() + ":" + holder.getAdapterPosition() + " " + isChecked);
                isCheck.set(holder.getAdapterPosition(), isChecked);
                if (isChecked) {
                    isDeselectAll = false;
                    viewModel.isDeselectAll.setValue(false);
                } else {
                    isSelectAll = false;
                    viewModel.isSelectAll.setValue(false);
                }
            }
        });

    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        for (int i = 0; i < isCheck.size(); i++) {
            isCheck.set(i, false);
        }
        notifyItemRangeChanged(0, list.size());
    }


    public void selectAll() {
        this.isSelectAll = true;
        this.isDeselectAll = false;
        for (int i = 0; i < list.size(); i++) {
            isCheck.set(i, Boolean.TRUE);
        }
        notifyItemRangeChanged(0, list.size());
    }

    public void deSelectAll() {
        this.isDeselectAll = true;
        this.isSelectAll = false;
        for (int i = 0; i < list.size(); i++) {
            isCheck.set(i, Boolean.FALSE);
        }
        notifyItemRangeChanged(0, list.size());
    }


    public void deleteItems(List<MediaBean> beans) {
        for (MediaBean bean : beans) {
            String date = bean.getDate();
            List<MediaBean> dateBeans = dateMediaMap.get(date);

            if (dateBeans == null) continue;

            for (int i = 0; i < dateBeans.size(); i++) {
                if (dateBeans.get(i).getId() == bean.id) {
                    MediaBean removeBean = dateBeans.remove(i);
                    int index = list.indexOf(removeBean);
                    list.remove(index);
                    isCheck.remove(index);
                    notifyItemRemoved(index);
                    break;
                }
            }
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
        private RequestOptions glideImageOptions;
        private RequestOptions glideVideoImageOptions;


        public MediaViewHolder(Fragment fragment, View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivPhoto = itemView.findViewById(R.id.iv_media_photo);
            tvTime = itemView.findViewById(R.id.tv_time);
            rbSelected = itemView.findViewById(R.id.rb_selected);

            // 根据屏幕宽度设置view的宽度
            DisplayMetrics outMetrics = new DisplayMetrics();
            fragment.requireActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            int screenWidth = outMetrics.widthPixels;
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            int oldWidth = layoutParams.width;
            int oneDp = fragment.getResources().getDimensionPixelOffset(R.dimen.one_dp);
            int maxWidth = (screenWidth - oneDp * 32) / 3;
            if (oldWidth > maxWidth) {
                layoutParams.width = maxWidth;
                itemView.setLayoutParams(layoutParams);
            }

            glideVideoImageOptions = new RequestOptions().frame(0).centerCrop();
            glideImageOptions = new RequestOptions().centerCrop();
        }

        public void bind(MediaBean bean) {
            if (bean.getType() == MediaType.VIDEO) {
                Glide.with(itemView)
                        .setDefaultRequestOptions(glideVideoImageOptions)
                        .load(bean.getUri())
                        .thumbnail(0.25f)
                        .into(ivPhoto);
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(getVideoDuration(bean.getDuration()));
            } else if (bean.getType() == MediaType.IMAGE) {
                Glide.with(itemView)
                        .setDefaultRequestOptions(glideImageOptions)
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
                    if (minute == second & second == 0) { // 如果小于1s就当是1s的。
                        res = "00:01";
                    }
                } else {
                    res = String.format("%02d:%02d", hour, minute);
                }
                return res;
            } else {
                return null;
            }
        }
    }


    public boolean isFirstInDate(View view) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);

        if (childAdapterPosition <= -1 || childAdapterPosition >= list.size()) { // 在view对应的数据删除后，这个view仍会存在一小部分时间。
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
        StringBuilder stringBuilder = new StringBuilder("checkBean:").append(isCheck.size()).append("--").append(this.hashCode()).append("---");
        for (int i = 0; i < isCheck.size(); i++) {
            stringBuilder.append(isCheck).append("、");
            if (isCheck.get(i)) {
                res.add(list.get(i));
            }
        }
        Log.i("MediaBeanAdapter", stringBuilder.toString());
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
