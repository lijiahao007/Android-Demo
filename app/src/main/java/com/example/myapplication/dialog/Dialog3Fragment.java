package com.example.myapplication.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myapplication.R;

public class Dialog3Fragment extends DialogFragment {


    private View rootView;
    private Button btnDrag;

    public Dialog3Fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initView();
        Log.i("Dialog3Fragment", "onCreateView");
        return rootView;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initView();
        Log.i("Dialog3Fragment", "onCreateDialog");
        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.AlertDialogFullWidthTheme)
                .setView(rootView)
                .create();
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(attributes);
        return dialog;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        if (rootView == null) {
            rootView = getLayoutInflater().inflate(R.layout.fragment_dialog3, null);
            Log.i("Dialog3Fragment", "initView: " + rootView);

            btnDrag = rootView.findViewById(R.id.btn_drag);
            btnDrag.setOnTouchListener(new View.OnTouchListener() {
                int lastY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Dialog dialog = getDialog();
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            if (lastY == 0) {
                                lastY = (int) event.getRawY();
                            } else {
                                int dis = (int) (event.getRawY() - lastY);
                                lastY = (int) event.getRawY();
                                attributes.height -= dis;
                                window.setAttributes(attributes);
                            }
                            break;
                        case MotionEvent.ACTION_DOWN:
                            lastY = 0;
                            break;
                        case MotionEvent.ACTION_UP:
                            btnDrag.performClick();
                            break;
                    }

                    return true;
                }
            });
        }
    }
}