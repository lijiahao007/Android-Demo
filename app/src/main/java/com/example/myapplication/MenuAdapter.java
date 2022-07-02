package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private ArrayList<MenuInfo> mMenuList;

    public MenuAdapter(ArrayList<MenuInfo> mMenuList) {
        this.mMenuList = mMenuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuInfo menuInfo = mMenuList.get(position);
        holder.tvMenu.setText(menuInfo.menuName);
        holder.itemView.setOnClickListener(menuInfo.listener);
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public void setMenuList(ArrayList<MenuInfo> menuList) {
        this.mMenuList = menuList;
        notifyDataSetChanged();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMenu;

        public MenuViewHolder(View itemView) {
            super(itemView);
            tvMenu = itemView.findViewById(R.id.tv_menu);
        }
    }

    public static class MenuInfo {
        String menuName;
        View.OnClickListener listener;

        public MenuInfo(String menuName, View.OnClickListener listener) {
            this.menuName = menuName;
            this.listener = listener;
        }

        public MenuInfo(String menuName, Class<? extends Activity> activityClass) {
            this.menuName = menuName;
            this.listener = v -> {
                Activity activity = (Activity) v.getContext();
                activity.startActivity(new Intent(activity, activityClass));
            };
        }
    }
}
