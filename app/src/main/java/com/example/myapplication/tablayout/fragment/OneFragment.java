package com.example.myapplication.tablayout.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.tablayout.TabLayoutDemoActivity;

import java.util.ArrayList;


public class OneFragment extends Fragment {

    private TabLayoutDemoActivity tabLayoutDemoActivity;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayoutDemoActivity = (TabLayoutDemoActivity) requireActivity();

        ArrayList<Integer> jumpBtn = new ArrayList<Integer>() {{
            add(R.id.btn_jump_two);
            add(R.id.btn_jump_three);
            add(R.id.btn_jump_four);
            add(R.id.btn_jump_five);
            add(R.id.btn_jump_six);
        }};

        for (int i = 0; i < jumpBtn.size(); i++) {
            Integer btn = jumpBtn.get(i);
            int finalI = i+1;
            view.findViewById(btn).setOnClickListener(v -> {
                tabLayoutDemoActivity.pagerJumpTo(finalI);
            });
        }
    }
}