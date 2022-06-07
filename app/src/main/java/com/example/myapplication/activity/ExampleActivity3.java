package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

public class ExampleActivity3 extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example3);
        this.textView = findViewById(R.id.info);

        String name = getIntent().getStringExtra("name");
        if (name != null) {
            textView.setText(name);
        }

        Student student = (Student) getIntent().getSerializableExtra("student");
        if (student != null) {
            textView.setText(student.toString());
        }

        Parcelable[] teacher = getIntent().getParcelableArrayExtra("teacher");
        if (teacher != null) {
            StringBuilder sb = new StringBuilder();
            for (Parcelable t : teacher) {
                sb.append(((Teacher) t).toString()).append("\n");
            }
            textView.setText(sb.toString());
        }


    }
}