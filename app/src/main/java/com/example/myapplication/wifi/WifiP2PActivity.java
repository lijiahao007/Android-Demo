package com.example.myapplication.wifi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class WifiP2PActivity extends AppCompatActivity {

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private WiFiDirectBroadcastReceiver wifiDirectBroadcastReceiver;
    private IntentFilter intentFilter;
    private String[] perms = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2_pactivity);

        if (EasyPermissions.hasPermissions(this, perms)) {
            init();
        } else {
            EasyPermissions.requestPermissions(this, "需要获取位置权限", 123, perms);
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(123)
    private void init() {
        wifiP2pManager = (WifiP2pManager) getSystemService(WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        wifiDirectBroadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i("WifiP2PActivity", "Discover peers success");
            }

            @Override
            public void onFailure(int i) {
                Log.i("WifiP2PActivity", "Discover peers failure：" + i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiDirectBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiDirectBroadcastReceiver);
    }
}