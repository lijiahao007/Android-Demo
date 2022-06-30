package com.example.myapplication.setupnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.tools.DeviceScanner;

import java.util.ArrayList;
import java.util.List;

import kotlin.random.Random;

public class WifiConnectActivity extends AppCompatActivity {
    private WifiManager mWifiManager;
    private LocationManager mLocationManager;
    private static final String TAG = "WifiConnectActivity";
    private WifiBroadcastReceiver mWifiBroadcastReceiver;
    private EditText etPwd;
    private EditText etSSID;
    private Button btnWifiConnect;
    private volatile int mConfigId;
    volatile int lanScanLabel = 0;
    final int lanScanMsgWhat = 0;
    private ArrayList<DeviceInfo> connectDevices  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);

        etSSID = findViewById(R.id.etSSID);
        etPwd = findViewById(R.id.etPwd);

        initWifi();
        initGPS();

        btnWifiConnect = findViewById(R.id.btnWifiConnect);
        btnWifiConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2. 判断账号密码是否为空
                String SSID = etSSID.getText().toString();
                String pwd = etPwd.getText().toString();
                if (SSID.isEmpty() || pwd.isEmpty()) {
                    showDialog("请输入账号密码");
                    return;
                }

                // 3. 将账号密码存入SharePreferences
                SharedPreferences sp = getSharedPreferences("wifi", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ssid", SSID);
                editor.putString("pwd", pwd);
                editor.apply();

                // 4. 开始smarkLink
                connectDevices = null;
                mConfigId = Random.Default.nextInt(1, 10);
                int result = DeviceScanner.startSmartConnection(mConfigId, SSID, pwd); // 0: success, else: fail

                if (result == 0) {
                    Toast.makeText(WifiConnectActivity.this, "开始smartLink  configId" + mConfigId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WifiConnectActivity.this, "开始smartLink失败", Toast.LENGTH_SHORT).show();
                }
                lanScanLabel++;
                LanScanThread lanScanThread = new LanScanThread(lanScanLabel);
                lanScanThread.start();
            }
        });
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case lanScanMsgWhat:
                    Toast.makeText(WifiConnectActivity.this, "配置完成", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "handleMessage: 配置完成");
                    DeviceScanner.stopSmartConnection(); // 连接完成结束快配
                    break;
            }
        }
    };

    class LanScanThread extends Thread {
        private final int mLanScanLabel;

        public LanScanThread(int lanScanLabel) {
            this.mLanScanLabel = lanScanLabel;
        }

        @Override
        public void run() {
            super.run();
            while (mLanScanLabel == lanScanLabel) {
                DeviceScanner.reset();
                Log.i(TAG, "lanScan: start device scan , mConfigId:" + mConfigId);
                ArrayList<DeviceInfo> deviceListFromLan = DeviceScanner.getDeviceListFromLan(mConfigId);
                Log.i(TAG, "lanScan: scan finish, deviceListFromLan size = " + deviceListFromLan.size() + " mConfigId:" + mConfigId);
                // 如果连接上了，这里的deviceListFromLan列表会存放匹配设备的信息
                for (DeviceInfo deviceInfo : deviceListFromLan) {
                    Log.d(TAG, "getStrName: " + deviceInfo.getStrName());
                    Log.d(TAG, "getnDevID: " + deviceInfo.getnDevID());
                    Log.d(TAG, "getnDevID: " + deviceInfo.getnDevID());
                    // 上面的三个属性都是设备ID
                    Log.d(TAG, "getnID: " + deviceInfo.getnID());
                }
                if (deviceListFromLan.size() > 0) {
                    connectDevices = deviceListFromLan;
                    handler.obtainMessage(lanScanMsgWhat).sendToTarget();
                    break;
                }
            }
            Log.d(TAG, "LanScanThread: " + mLanScanLabel + "  end");
        }
    }


    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void initWifi() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int wifiState = mWifiManager.getWifiState();
        WifiInfo connectionInfo = mWifiManager.getConnectionInfo();
        if (wifiState != WifiManager.WIFI_STATE_ENABLED && connectionInfo != null) {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }
        }

        // 注册wifi广播监听器
        mWifiBroadcastReceiver = new WifiBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mWifiBroadcastReceiver, intentFilter);
    }



    private void initGPS() {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d(TAG, "是否开启gps：" + providerEnabled);
        if (!providerEnabled) {
            new AlertDialog.Builder(this)
                    .setMessage("请开启GPS")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
    }

    class WifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION: {

                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    Log.d(TAG, "WifiBroadcastReceiver: NETWORK_STATE_CHANGED_ACTION 网络状态改变:" + networkInfo.getState());
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        int type = networkInfo.getType();
                        Log.d(TAG, "WifiBroadcastReceiver: 网络连接成功 " + type);
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            String ssid = mWifiManager.getConnectionInfo().getSSID().replace("\"", "");
                            etSSID.setText(ssid);

                            // 从SharePreferences中获取账号密码
                            SharedPreferences sp = getSharedPreferences("wifi", MODE_PRIVATE);
                            String ssidSp = sp.getString("ssid", "");
                            String pwdSp = sp.getString("pwd", "");
                            if (ssid.equals(ssidSp)) {
                                etPwd.setText(pwdSp);
                            }
                        }
                    } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        Log.d(TAG, "WifiBroadcastReceiver: 网络断开");
                    }
                    break;
                }

                case WifiManager.WIFI_STATE_CHANGED_ACTION: { // wifi开关
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                    if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                        Log.d(TAG, "WifiBroadcastReceiver: WIFI_STATE_ENABLED");
                    } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                        Log.d(TAG, "WifiBroadcastReceiver: WIFI_STATE_DISABLED");
                    } else if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
                        Log.d(TAG, "WifiBroadcastReceiver: WIFI_STATE_DISABLING");
                    } else if (wifiState == WifiManager.WIFI_STATE_UNKNOWN) {
                        Log.d(TAG, "WifiBroadcastReceiver: WIFI_STATE_UNKNOWN");
                    } else {
                        Log.d(TAG, "WifiBroadcastReceiver: " + wifiState);
                    }
                    break;
                }

                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION: { // 扫描结果
                    Log.d(TAG, "WifiBroadcastReceiver: SCAN_RESULTS_AVAILABLE_ACTION");
                    List<ScanResult> scanResults = mWifiManager.getScanResults();
                    for (ScanResult scanResult : scanResults) {
                        Log.d(TAG, "WifiBroadcastReceiver: " + scanResult.SSID);
                    }
                    break;
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWifiBroadcastReceiver);

    }
}