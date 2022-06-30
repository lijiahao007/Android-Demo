package com.example.myapplication.setupnet;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

public class WifiUtils {


    public static String getWifiSSID(@NonNull final WifiManager wifiManager) {
        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }
        return wifiInfo.getSSID();
    }
}
