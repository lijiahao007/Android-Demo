package com.example.myapplication.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;


public class Dialog1Fragment extends DialogFragment {

    private Button btnConfirm;
    private Button btnCancel;
    private TextView tvTitle;

    public Dialog1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dialog1, container, false);
        btnConfirm = rootView.findViewById(R.id.btn_confirm);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        tvTitle = rootView.findViewById(R.id.tv_title);

        btnConfirm.setOnClickListener(view -> {
            dismiss();
        });

        btnCancel.setOnClickListener(view -> {
            dismiss();
        });

        tvTitle.setText("Dialog1");

        Log.i("Dialog1Fragment", "onCreateView");

        return rootView;
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        ;
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i("Dialog1Fragment", "onCreateDialog");
        return dialog;
    }

}