package com.example.myapplication.polygonseldemo;

import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityPolyGonSelDemoBinding;

public class PolyGonSelDemoActivity extends BaseActivity<ActivityPolyGonSelDemoBinding> {

    boolean isDeleteMode = false;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.btnChanMode.setOnClickListener(view -> {
            nextState();
        });
    }

    // view -> edit -> delete -> view
    private void nextState() {
        if (!isDeleteMode && !isEditMode) {
            isEditMode = true;
            binding.btnChanMode.setText("编辑模式");
            binding.polyView.setEditMode(true);
        } else if (isEditMode && !isDeleteMode) {
            binding.btnChanMode.setText("删除模式");
            isEditMode = false;
            isDeleteMode = true;
            binding.polyView.setEditMode(false);
            binding.polyView.setDeleteMode(true);
        } else if (!isEditMode && isDeleteMode) {
            binding.btnChanMode.setText("观察模式");
            isEditMode = false;
            isDeleteMode = false;
            binding.polyView.setEditMode(false);
            binding.polyView.setDeleteMode(false);
        }
    }
}