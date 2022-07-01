package com.example.myapplication.setupnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.MacAddress;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.PatternMatcher;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

public class WifiAssistant {

    /**
     * 连接指定WIFI
     *
     * @param context
     * @param ssid     Wi-Fi 设备 SSID
     * @param password Wi-Fi 设备密码
     * @param maunal   跳转到系统设置页进行连接
     * @param listener
     */
    public static void connectWifi(Context context, String ssid, String password, boolean maunal, WifiAutoConnectListener listener) {
        Log.i("WifiAssistant", "connectWifi ssid=" + ssid + " password=" + password);

        if (listener == null) {
            return;
        }
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            return;
        }
        if (maunal) {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            return;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo.getSSID().equals(ssid)) {
            return;
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            WifiConfiguration config = new WifiConfiguration();
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.SSID = "\"" + ssid + "\"";
            config.preSharedKey = "\"" + password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;

            // 判断wifi是否连结果
            boolean hasConnected = false;
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration configuration : configuredNetworks) {
                Log.i("WifiAssistant", "configuredNetworks configuration.SSID=" + configuration.SSID + " netId:" + configuration.networkId + " BSSID" + configuration.BSSID + " target.SSID:" + config.SSID);
                if (configuration.SSID.equals(config.SSID)) {
                    config = configuration;
                    hasConnected = true;
                    break;
                }
            }

            boolean result = false;
            if (hasConnected) {
                Log.i("WifiAssistant", "addNetwork netId=" + config.networkId);
                result = wifiManager.enableNetwork(config.networkId, true);
            } else {
                int netId = wifiManager.addNetwork(config);
                Log.i("WifiAssistant", "addNetwork netId=" + netId);
                result = wifiManager.enableNetwork(netId, true);
            }
            if (result) {
                listener.onSuccess();
            } else {
                listener.onFailure();
            }

        } else {
            WifiNetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .setBssidPattern(MacAddress.fromString("10:03:23:00:00:00"), MacAddress.fromString("ff:ff:ff:00:00:00"))
                    .build();

            NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .setNetworkSpecifier(specifier).build();
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    cm.bindProcessToNetwork(network);
                    listener.onSuccess();
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    listener.onFailure();
                }
            };
            cm.requestNetwork(request, networkCallback);
        }
    }

    public interface WifiAutoConnectListener {

        //链接成功
        void onSuccess();

        //链接失败
        void onFailure();
    }


}
