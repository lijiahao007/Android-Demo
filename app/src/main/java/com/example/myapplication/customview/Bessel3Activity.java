package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.myapplication.R;

public class Bessel3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bessel3);

        RadioGroup group = findViewById(R.id.rg_select_mode);
        BesselCustomView2 bessel = findViewById(R.id.bessel_three);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.control_1: {
                        bessel.setMode(true);
                        break;
                    }
                    case R.id.control_2: {
                        bessel.setMode(false);
                        break;
                    }
                }
            }
        });
    }
}