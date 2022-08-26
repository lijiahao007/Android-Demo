package com.example.myapplication.thirdpartlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;

public class WxChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_chat);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left);
    }
}