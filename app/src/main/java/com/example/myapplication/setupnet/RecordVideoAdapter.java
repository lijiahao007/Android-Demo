package com.example.myapplication.setupnet;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.macrovideo.sdk.objects.RecSegment;
import com.macrovideo.sdk.objects.RecordVideoInfo;

import java.util.ArrayList;
import java.util.List;

public class RecordVideoAdapter extends RecyclerView.Adapter<RecordVideoAdapter.RecordVideoViewHolder> {

    private ArrayList<RecordVideoInfo> recordVideoInfos = new ArrayList<>();
    private ItemClickListener itemClickListener = null;
    private RecordVideoDownloadClickListener recordVideoDownloadClickListener = null;


    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<RecordVideoInfo> recordVideoInfos) {
        this.recordVideoInfos.clear();
        this.recordVideoInfos.addAll(recordVideoInfos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_file, parent, false);
        return new RecordVideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordVideoViewHolder holder, int position) {
        RecordVideoInfo recordVideoInfo = recordVideoInfos.get(position);
        holder.bind(recordVideoInfo);
    }

    @Override
    public int getItemCount() {
        return recordVideoInfos.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setRecordVideoDownloadClickListener(RecordVideoDownloadClickListener listener) {
        this.recordVideoDownloadClickListener = listener;
    }

    class RecordVideoViewHolder extends RecyclerView.ViewHolder {

        private final TextView id;
        private final TextView tvBeginTime;
        private final TextView tvEndTime;
        private final ImageView ivDownload;
        private final ProgressBar progressBar;
        private final ImageView ivLoading;
        private final ObjectAnimator objectAnimator;
        private final ImageView ivFailed;
        private final ImageView ivFinish;

        View itemView;

        public RecordVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            id = itemView.findViewById(R.id.tvID);
            tvBeginTime = itemView.findViewById(R.id.tvBeginTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            ivDownload = itemView.findViewById(R.id.ivDownload);
            progressBar = itemView.findViewById(R.id.progress);
            ivLoading = itemView.findViewById(R.id.ivLoading);
            ivFailed = itemView.findViewById(R.id.ivFailed);
            ivFinish = itemView.findViewById(R.id.ivFinish);
            objectAnimator = ObjectAnimator.ofFloat(ivLoading, "rotation", 0, 359);
            objectAnimator.setInterpolator(new LinearInterpolator()); // 线性插值
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE); // 无限重复
            objectAnimator.setDuration(2000); // 每次间隔

        }

        public void bind(RecordVideoInfo recordVideoInfo) {
            int id = recordVideoInfo.getnFileID();
            RecSegment recSegment = recordVideoInfo.getRecSegment();
            short beginHour = recSegment.getNvtStartTime().getuHour();
            short beginMinute = recSegment.getNvtStartTime().getuMin();
            short endHour = recSegment.getNvtEndTime().getuHour();
            short endMinute = recSegment.getNvtEndTime().getuMin();
            String beginTime = String.format("%02d:%02d", beginHour, beginMinute);
            String endTime = String.format("%02d:%02d", endHour, endMinute);
            this.id.setText(String.valueOf(id));
            this.tvBeginTime.setText(beginTime);
            this.tvEndTime.setText(endTime);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(0);
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(recordVideoInfo);
                }
            });

            ivDownload.setOnClickListener(view -> {
                if (recordVideoDownloadClickListener != null) {
                    ivDownload.setVisibility(View.GONE);
                    ivLoading.setVisibility(View.VISIBLE);
                    objectAnimator.start();
                    recordVideoDownloadClickListener.onDownLoad(recordVideoInfo, progressBar, new IDownloadState() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onFailed() {
                            objectAnimator.cancel();
                            ivLoading.setVisibility(View.GONE);
                            ivFailed.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onDownloading() {
                        }

                        @Override
                        public void onFinish() {
                            ivLoading.setVisibility(View.GONE);
                            ivFinish.setVisibility(View.VISIBLE);
                            objectAnimator.cancel();
                        }
                    });
                }
            });
        }
    }


    interface ItemClickListener {
        void onItemClick(RecordVideoInfo recordVideoInfo);
    }

    interface RecordVideoDownloadClickListener {
        void onDownLoad(RecordVideoInfo recordVideoInfo, ProgressBar progressBar, IDownloadState downloadState);
    }

    interface IDownloadState {
        void onStart();

        void onFailed();

        void onDownloading();

        void onFinish();
    }
}
