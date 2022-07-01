package com.example.myapplication.wifi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.setupnet.WifiAssistant;
import com.example.myapplication.utils.LogView;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class WifiDemoActivity extends AppCompatActivity {

    private LogView logView;

    private static final String[] wifiScanPerms = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int RC_WIFI_SCAN = 123;
    private WifiManager wifiManager;
    private ConnectivityManager systemService;
    private NetworkInfo activeNetworkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_demo);
        // TODO: wifi管理工具

        systemService = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = systemService.getActiveNetworkInfo();
        Log.i("WifiDemoActivity", "activeNetworkInfo: " + activeNetworkInfo);
        Log.i("WifiDemoActivity", "activeNetworkInfo: " + (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) + " wifi");
        Log.i("WifiDemoActivity", "activeNetworkInfo: " + activeNetworkInfo.isConnected() + " isConnected");

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Log.i("WifiDemoActivity", "wifiManager: " + wifiManager);
        Log.i("WifiDemoActivity", "wifiManager: " + wifiManager.getWifiState() + " wifi state");

        logView = findViewById(R.id.log_view);


        // 1. 扫描获取WIFI列表
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    logView.addLog("wifi scan success");
                    List<ScanResult> results = wifiManager.getScanResults();
                    for (ScanResult result : results) {
                        logView.addLog(result.SSID + " " + result.capabilities);
                    }
                } else {
                    logView.addLog("wifi scan failed");

                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

        findViewById(R.id.btn_wlan_scan).setOnClickListener(view -> {
            if (EasyPermissions.hasPermissions(this, wifiScanPerms)) {
                scanWifi();
            } else {
                EasyPermissions.requestPermissions(this, "wifi scan", RC_WIFI_SCAN, wifiScanPerms);
            }
        });


        // 2. P2P wifi 链接
        findViewById(R.id.btn_wlan_p2p).setOnClickListener(view -> {
            Intent intent = new Intent(WifiDemoActivity.this, WifiP2PActivity.class);
            startActivity(intent);
        });

        EditText etSSID = findViewById(R.id.etSSID);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnConnectSpecialWifi = findViewById(R.id.btnConnectSpecialWifi);
        btnConnectSpecialWifi.setOnClickListener(view -> {
            String SSID = etSSID.getText().toString();
            String password = etPassword.getText().toString();

            WifiAssistant.connectWifi(this, SSID, password, false, new WifiAssistant.WifiAutoConnectListener() {
                @Override
                public void onSuccess() {
                    logView.addLog("connect wifi success");
                }

                @Override
                public void onFailure() {
                    logView.addLog("connect wifi failed");
                }
            });
        });

        String bssid = wifiManager.getConnectionInfo().getBSSID();
        Log.i("WifiDemoActivity", "bssid: " + bssid);

    }


    private void scanWifi() {
        boolean success = wifiManager.startScan();
        if (success) {
            // scan failure handling
            logView.addLog("wifi start scan success");
        } else {
            logView.addLog("wifi start scan failed");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}