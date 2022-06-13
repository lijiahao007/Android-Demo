package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CustomViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_demo);
        PieChartView pieChart = findViewById(R.id.pie_chart);
        pieChart.setStartAngle(30);
        ArrayList<PieData> pieData = new ArrayList<PieData>() {{
            add(new PieData("a", 10));
            add(new PieData("b", 20));
            add(new PieData("c", 30));
            add(new PieData("d", 40));
            add(new PieData("e", 40));
            add(new PieData("f", 40));
        }};
        pieChart.setData(pieData);
    }
}