package com.example.myapplication.tablayout.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.tablayout.DemoViewModel;
import com.example.myapplication.tablayout.LifecycleLogObserver;

public class TwoFragment extends Fragment {
    private DemoViewModel viewModel;

    public TwoFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new LifecycleLogObserver("TwoFragment"));
        viewModel = new ViewModelProvider(requireActivity()).get(DemoViewModel.class);
        viewModel.isChange.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("TwoFragment", "value:" + aBoolean);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleLogObserver("TwoFragmentView"));

        return inflater.inflate(R.layout.fragment_two, container, false);
    }
}