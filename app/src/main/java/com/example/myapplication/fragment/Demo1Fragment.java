package com.example.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.statusinset.CustomSwitchCompat;

public class Demo1Fragment extends Fragment {

    private SwitchDemoActivity mActivity;

    private final String TAG = "Demo1Fragment";

    private CheckBox checkBox;
    private CustomSwitchCompat switchDemo1;

    public Demo1Fragment() {
        // Required empty public constructor
    }


    public static Demo1Fragment newInstance() {
        Demo1Fragment fragment = new Demo1Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (SwitchDemoActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Demo1Fragment", "onCreate [" + savedInstanceState + "]");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_demo1, container, false);

        View btnToFragment2 = inflate.findViewById(R.id.btn_to_fragment2);
        btnToFragment2.setOnClickListener(view -> {
            mActivity.replaceFragment(Demo2Fragment.newInstance());
        });
        checkBox = inflate.findViewById(R.id.checkbox_demo1);

        switchDemo1 = inflate.findViewById(R.id.switch_demo1);
        switchDemo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("Demo1Fragment", "switch onCheckedChanged [" + isChecked + "] + isPress=[" + buttonView.isPressed() + "]");
            }
        });
        View btnChangeSwitch = inflate.findViewById(R.id.btn_change_switch);
        btnChangeSwitch.setOnClickListener(view -> {
            switchDemo1.setChecked(!switchDemo1.isChecked());
            checkBox.setChecked(!checkBox.isChecked());
        });


//        View btnChangeSwitch1 = inflate.findViewById(R.id.btn_change_switch);
//        btnChangeSwitch1.setOnClickListener(view -> {
//            switchDemo1.setPressed(!switchDemo1.isChecked());
//        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("Demo1Fragment", "checkBox onCheckedChanged [" + isChecked + "] + isPress=[" + buttonView.isPressed() + "]");
            }
        });



        switchDemo1.setChecked(true);
        checkBox.setChecked(true);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated savedInstanceState=[" + savedInstanceState + "]");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored [" + savedInstanceState + "]");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState [" + outState + "]");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}