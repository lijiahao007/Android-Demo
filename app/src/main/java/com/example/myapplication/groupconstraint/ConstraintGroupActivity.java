package com.example.myapplication.groupconstraint;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityConstraintGroupBinding;

public class ConstraintGroupActivity extends BaseActivity<ActivityConstraintGroupBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int[] bindClick() {
        return new int[] {
            R.id.btn_v1, R.id.btn_v2, R.id.btn_group
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_v1:
                if (binding.btnV1.getText().equals("View1 Visible")) {
                    binding.view1.setVisibility(View.GONE);
                    binding.btnV1.setText("View1 Gone");
                } else {
                    binding.view1.setVisibility(View.VISIBLE);
                    binding.btnV1.setText("View1 Visible");
                }


                break;
            case R.id.btn_v2:
                if (binding.btnV2.getText().equals("View2 Visible")) {
                    binding.view2.setVisibility(View.GONE);
                    binding.btnV2.setText("View2 Gone");
                } else {
                    binding.view2.setVisibility(View.VISIBLE);
                    binding.btnV2.setText("View2 Visible");
                }

                break;

            case R.id.btn_group:
                if (binding.btnGroup.getText().equals("Group Visible")) {
                    binding.groupView.setVisibility(View.GONE);
                    binding.btnGroup.setText("Group Gone");
                } else {
                    binding.groupView.setVisibility(View.VISIBLE);
                    binding.btnGroup.setText("Group Visible");
                }
                break;

        }
    }
}