package com.example.myapplication.statusinset;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityScrollViewScrollDemoBinding;
import com.example.myapplication.utils.DimenUtil;

public class ScrollViewScrollDemoActivity extends BaseActivity<ActivityScrollViewScrollDemoBinding> {

    @Override
    protected int[] bindClick() {
        return new int[]{
                R.id.btn_to_positive,
                R.id.btn_to_minus,
                R.id.btn_by_positive,
                R.id.btn_by_minus,
        };
    }

    private int dp2px(int dp) {
        return (int) DimenUtil.dp2px(this, dp);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_to_positive:
                binding.scrollView.scrollTo(dp2px(10), dp2px(10));
                break;
            case R.id.btn_to_minus:
                binding.scrollView.scrollTo(dp2px(-10), dp2px(-10));
                break;
            case R.id.btn_by_positive:
                binding.scrollView.scrollBy(dp2px(10), dp2px(10));
                break;
            case R.id.btn_by_minus:
                binding.scrollView.scrollBy(dp2px(-10), dp2px(-10));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int childCount = binding.scrollView.getChildCount();
        ViewGroup linearLayout = (ViewGroup) binding.scrollView.getChildAt(0);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "isButton = " + (v instanceof Button));

                    Button btn = (Button) v;
                    String text = btn.getText().toString();
                    Log.i(TAG, "text = " + text);

                    showToast(text);
                }
            });

        }
    }
}