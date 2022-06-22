package com.example.myapplication.eventbus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class RightFragment extends Fragment {


    private Button btnSend;
    private TextView tvMessage;

    public RightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_right, container, false);
        EventBus.getDefault().register(this);

        btnSend = root.findViewById(R.id.btn_send_event_right_fragment);
        btnSend.setOnClickListener(view -> {
            EventBus.getDefault().post(new MessageEvent("RightFragment message"));

        });
        tvMessage = root.findViewById(R.id.tv_message);

        return root;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageEvent(MessageEvent messageEvent) {
        tvMessage.setText(messageEvent.getMessage());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}