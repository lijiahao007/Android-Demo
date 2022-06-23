package com.example.myapplication.wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;

public class WifiDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_demo);
        // TODO: wifi管理工具

        ConnectivityManager systemService = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = systemService.getActiveNetworkInfo();
        Log.i("WifiDemoActivity", "activeNetworkInfo: " + activeNetworkInfo);
        Log.i("WifiDemoActivity", "activeNetworkInfo: " + (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) + " wifi");
        Log.i("WifiDemoActivity", "activeNetworkInfo: " + activeNetworkInfo.isConnected() + " isConnected");

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.i("WifiDemoActivity", "wifiManager: " + wifiManager);
        Log.i("WifiDemoActivity", "wifiManager: " + wifiManager.getWifiState() + " wifi state");

        findViewById(R.id.btn_socket_connect).setOnClickListener(view -> {

        });


    }
}