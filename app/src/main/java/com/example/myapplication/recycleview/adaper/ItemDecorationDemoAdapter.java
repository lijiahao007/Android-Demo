package com.example.myapplication.recycleview.adaper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemDecorationDemoAdapter extends RecyclerView.Adapter<ItemDecorationDemoAdapter.DemoViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private HashMap<Integer, Long> comeInTime = new HashMap<>();

    public void submitList(ArrayList<String> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void addItem(String msg) {
        list.add(msg);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DemoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_decoration, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {
        holder.bind(list.get(position));
        holder.itemView.setOnClickListener(view -> {
            ObjectAnimator zoomY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f, 1f);
            ObjectAnimator zoomX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f, 1f);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(200);
            set.play(zoomX).with(zoomX);
            set.start();

            TextView textView = view.findViewById(R.id.text);
            String text = textView.getText().toString();
            if (holder.isCap) {
                text = text.toUpperCase();
                holder.isCap = false;
            } else {
                text = text.toLowerCase();
                holder.isCap = true;
            }
            textView.setText(text);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull DemoViewHolder holder) { // 监听view的移入
        super.onViewAttachedToWindow(holder);
        long time = System.currentTimeMillis();
        Log.i("DemoAdapter", "attached" + holder.getAdapterPosition() + " time: " + time);
        comeInTime.put(holder.getAdapterPosition(), time);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull DemoViewHolder holder) { // 监听view的移出
        super.onViewDetachedFromWindow(holder);
        long detachTime = System.currentTimeMillis();
        Long attachTime = comeInTime.get(holder.getAdapterPosition());
        double duration = -1;
        if (attachTime != null) {
            duration = (detachTime - attachTime) / 1000.0;
        }
        Log.i("DemoAdapter", "detached " + holder.getAdapterPosition() + " time: " + detachTime + "  duration:" + duration + "s");
    }

    static class DemoViewHolder extends RecyclerView.ViewHolder {

        private final View root;
        public boolean isCap = true;

        public DemoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.root = itemView;
        }

        public void bind(String msg) {
            TextView text = root.findViewById(R.id.text);
            text.setText(msg);
        }
    }
}
