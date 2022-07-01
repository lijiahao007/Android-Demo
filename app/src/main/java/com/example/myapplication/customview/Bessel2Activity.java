package com.example.myapplication.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.setupnet.CountdownTextView;

public class Bessel2Activity extends AppCompatActivity {

    private CountdownTextView ctv;
    private Button btnStop;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bessel2);

        ctv = findViewById(R.id.ctv);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        btnStart.setOnClickListener(view ->{
            ctv.startCountDown();
        });
        btnStart.setOnClickListener(view ->{
            ctv.stopCountDown();
        });



    }
}