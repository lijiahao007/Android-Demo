package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;

public class ExampleActivity4 extends AppCompatActivity {

    private EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example4);

        text = findViewById(R.id.edit_text);

        findViewById(R.id.btn_return).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("msg", text.getText().toString());
            setResult(200, intent);
            finish();
        });
    }
}