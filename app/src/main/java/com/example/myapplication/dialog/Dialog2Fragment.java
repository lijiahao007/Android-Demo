package com.example.myapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.tablayout.LifecycleLogObserver;


public class Dialog2Fragment extends DialogFragment {


    private Button btnConfirm;
    private Button btnCancel;
    private View rootView;

    public Dialog2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new LifecycleLogObserver("Dialog2Fragment"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initView();
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleLogObserver("Dialog2Fragment View"));
        Log.i("Dialog2Fragment", "onCreateView " + rootView);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.i("Dialog2Fragment", "onCreateDialog");
        initView();
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(rootView)
                .setMessage("Helloworld")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void initView() {
        if (rootView == null) {
            rootView = getLayoutInflater().inflate(R.layout.fragment_dialog2, null);
            btnConfirm = rootView.findViewById(R.id.btn_confirm);
            btnCancel = rootView.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(view -> dismiss());
            btnConfirm.setOnClickListener(view -> dismiss());
        }
    }
}