package com.example.myapplication.wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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

                            WifiP2pConfig config = new WifiP2pConfig();
                            config.deviceAddress = "12:5d:93:a9:29:a6"; // 设置连接的设备地址
                            // 连接设备
                            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                                @Override
                                public void onSuccess() {
                                    Log.i("WifiP2PActivity", "connect success");

                                    new Thread() {
                                        @Override
                                        public void run() {
                                            super.run();
                                            try {
                                                // 发送数据
                                                Socket socket = new Socket();
                                                socket.bind(null);
                                                socket.connect(new InetSocketAddress(config.deviceAddress, 8888)); // 连接设备
                                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                                writer.write("hello123123");
                                                writer.flush();
                                                socket.close();
                                                writer.close();
                                                Log.i("WifiP2PActivity", "send success");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();

                                    // 获取链接信息
                                    mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                                        @Override
                                        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) { // 获取自身IP
                                            Log.i("WifiP2PActivity", "onConnectionInfoAvailable:" + wifiP2pInfo);
                                            Log.i("WifiP2PActivity", "onConnectionInfoAvailable: " + wifiP2pInfo.groupOwnerAddress);
                                            Log.i("WifiP2PActivity", "onConnectionInfoAvailable: " + wifiP2pInfo.isGroupOwner);
                                            Log.i("WifiP2PActivity", "onConnectionInfoAvailable: " + wifiP2pInfo.groupOwnerAddress.getHostAddress());
                                            Log.i("WifiP2PActivity", "onConnectionInfoAvailable: " + Arrays.toString(wifiP2pInfo.groupOwnerAddress.getAddress()));
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    try {
                                                        // 发送数据
                                                        Socket socket = new Socket();
                                                        socket.bind(null);
                                                        socket.connect(new InetSocketAddress(wifiP2pInfo.groupOwnerAddress.getHostAddress(), 8888)); // wifiP2pInfo.groupOwnerAddress.getHostAddress() 获取自身IP
                                                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                                        writer.write("hello");
                                                        writer.flush();
                                                        socket.close();
                                                        writer.close();
                                                        Log.i("WifiP2PActivity", "send success");
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }.start();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(int i) {
                                    Log.i("WifiP2PActivity", "connect failure:" + i);
                                    // 获取链接信息
                                }
                            });

                            new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    try {
                                        ServerSocket serverSocket = new ServerSocket(8888);
                                        Log.i("WifiP2PActivity", "serverSocket: " + serverSocket + " listening");
                                        Socket client = serverSocket.accept();
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                        String str = reader.readLine();
                                        Log.i("WifiP2PActivity", "收到消息: " + str);
                                        mActivity.runOnUiThread(() -> {
                                            Toast.makeText(context, "str", Toast.LENGTH_LONG).show();
                                        });
                                        serverSocket.close();
                                        client.close();
                                        reader.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();


                            // TODO: 添加设备选择列表、数据传输、（只能传输数据给自己。）

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