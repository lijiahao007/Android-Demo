package com.example.myapplication.recycleview.adaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoViewHolder> {

    public ArrayList<String> list = new ArrayList<>();

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DemoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DemoViewHolder extends RecyclerView.ViewHolder {

        private View root;

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
