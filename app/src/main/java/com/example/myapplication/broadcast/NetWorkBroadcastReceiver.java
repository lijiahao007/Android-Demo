package com.example.myapplication.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.TextView;

public class NetWorkBroadcastReceiver extends BroadcastReceiver {
    private TextView logView;
    private String msg;
    public NetWorkBroadcastReceiver(TextView logView, String msg) {
        this.logView = logView;
        this.msg = msg;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                if (isConnected) {
                    logView.setText(logView.getText() + "\n" + "网络连接成功 " + networkInfo.getTypeName() + " " + msg);
                } else {
                    logView.setText(logView.getText() + "\n" + "网络断开" + " " + msg);
                }

            }
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    logView.setText(logView.getText() + "\n" + "Wifi断开" + " " + msg);

                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    logView.setText(logView.getText() + "\n" + "Wifi正在断开" + " " + msg);

                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    logView.setText(logView.getText() + "\n" + "Wifi打开" + " " + msg);

                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    logView.setText(logView.getText() + "\n" + "Wifi正在打开" + " " + msg);

                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    logView.setText(logView.getText() + "\n" + "Wifi WIFI_STATE_UNKNOWN " + " " + msg);
                    break;
            }
        }
    }
}
