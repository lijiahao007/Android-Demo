package com.example.myapplication.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2PActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       WifiP2PActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @SuppressLint({"LongLogTag", "MissingPermission"})
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Log.i("WifiP2PActivity", "Wifi P2P is enabled");
                // 获取设备列表
                if (mManager != null) {
                    mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            Log.i("WifiP2PActivity", "onPeersAvailable");
                            Log.i("WifiP2PActivity", "onPeersAvailable: " + wifiP2pDeviceList.getDeviceList().size());
                            for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList.getDeviceList()) {
                                Log.i("WifiP2PActivity", "onPeersAvailable: " + wifiP2pDevice.deviceAddress);
                                Log.i("WifiP2PActivity", "onPeersAvailable: " + wifiP2pDevice.deviceName);
                            }

//                            WifiP2pConfig config = new WifiP2pConfig();
//                            config.deviceAddress = "12:5d:93:a9:29:a6";
//                            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
//                                @Override
//                                public void onSuccess() {
//                                    Log.i("WifiP2PActivity", "connect success");
//                                }
//
//                                @Override
//                                public void onFailure(int i) {
//                                    Log.i("WifiP2PActivity", "connect failure:" + i);
//                                }
//                            });


                        }
                    });
                }
            } else {
                // Wi-Fi P2P is not enabled
                Log.i("WifiP2PActivity", "Wifi P2P is not enabled");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}