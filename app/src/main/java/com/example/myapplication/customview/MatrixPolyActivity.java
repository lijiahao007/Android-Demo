package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.myapplication.R;

public class MatrixPolyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_poly);

        final PolySettingView poly = (PolySettingView) findViewById(R.id.poly);

        RadioGroup group = (RadioGroup) findViewById(R.id.group);
        assert group != null;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.point0: poly.setTestPoint(0); break;
                    case R.id.point1: poly.setTestPoint(1); break;
                    case R.id.point2: poly.setTestPoint(2); break;
                    case R.id.point3: poly.setTestPoint(3); break;
                    case R.id.point4: poly.setTestPoint(4); break;
                }
            }
        });
    }
}