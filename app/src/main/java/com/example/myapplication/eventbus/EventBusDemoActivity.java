package com.example.myapplication.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusDemoActivity extends AppCompatActivity {

    private TextView tvMessage;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_demo);
        // 1. 注册
        EventBus.getDefault().register(this);
        tvMessage = findViewById(R.id.tv_message);
        btnSend = findViewById(R.id.btn_send_event_in_activity);
        btnSend.setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent("activity message"));
            EventBus.getDefault().postSticky(new MessageEvent("activity sticky message"));
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageEvent(MessageEvent messageEvent) {
        tvMessage.setText(messageEvent.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}