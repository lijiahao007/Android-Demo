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
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.defines.ResultCode;
import com.macrovideo.sdk.media.ILoginDeviceCallback;
import com.macrovideo.sdk.media.LoginHandle;
import com.macrovideo.sdk.media.LoginHelper;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.objects.LoginParam;
import com.macrovideo.sdk.setting.AccountConfigInfo;
import com.macrovideo.sdk.setting.DeviceAccountSetting;
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
    private ArrayList<DeviceInfo> connectDevices = null;
    private EditText etUserPassword;
    private EditText etUserName;

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
                // 2. ??????????????????????????????
                String SSID = etSSID.getText().toString();
                String pwd = etPwd.getText().toString();
                if (SSID.isEmpty() || pwd.isEmpty()) {
                    showDialog("?????????????????????");
                    return;
                }

                // 3. ?????????????????????SharePreferences
                SharedPreferences sp = getSharedPreferences("wifi", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("ssid", SSID);
                editor.putString("pwd", pwd);
                editor.apply();

                // 4. ??????smarkLink
                connectDevices = null;
                mConfigId = Random.Default.nextInt(1, 10);
                int result = DeviceScanner.startSmartConnection(mConfigId, SSID, pwd); // 0: success, else: fail

                if (result == 0) {
                    Toast.makeText(WifiConnectActivity.this, "??????smartLink  configId" + mConfigId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WifiConnectActivity.this, "??????smartLink??????", Toast.LENGTH_SHORT).show();
                }
                lanScanLabel++;
                LanScanThread lanScanThread = new LanScanThread(lanScanLabel);
                lanScanThread.start();
            }
        });

        // ??????????????????????????????
        etUserName = findViewById(R.id.etUserName);
        etUserPassword = findViewById(R.id.etUserPassword);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case lanScanMsgWhat:
                    Toast.makeText(WifiConnectActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "handleMessage: ????????????");
                    DeviceScanner.stopSmartConnection(); // ????????????????????????
                    // ?????????????????????????????????
                    LoginParam loginParam = new LoginParam(SetupNetDemoActivity.getDeviceInfo(), Defines.LOGIN_FOR_SETTING);
                    DeviceInfo deviceInfo = SetupNetDemoActivity.getDeviceInfo();
                    LoginHelper.loginDevice(WifiConnectActivity.this, loginParam, new ILoginDeviceCallback() {
                        @Override
                        public void onLogin(LoginHandle loginHandle) {
                            if (loginHandle != null && loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                                Log.i(TAG, "handleMessage: ????????????");
                                AccountConfigInfo accountConfig = DeviceAccountSetting.getAccountConfig(deviceInfo, loginHandle);
                                AccountConfigInfo newAccountConfig = DeviceAccountSetting.setAccountConfig(deviceInfo, etUserName.getText().toString(), etUserPassword.getText().toString(), accountConfig.getnUserID(), loginHandle);
                                if (newAccountConfig != null && newAccountConfig.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                                    Log.i(TAG, "handleMessage: ????????????????????????");
                                    deviceInfo.setStrUsername(etUserName.getText().toString());
                                    deviceInfo.setStrPassword(etUserPassword.getText().toString());
                                    SetupNetDemoActivity.setDeviceInfo(deviceInfo);
                                    Toast.makeText(WifiConnectActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                    WifiConnectActivity.this.finish();
                                } else {
                                    Log.i(TAG, "handleMessage: ????????????????????????");
                                    Toast.makeText(WifiConnectActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.i(TAG, "handleMessage: ????????????");
                                Toast.makeText(WifiConnectActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                // ??????????????????????????????deviceListFromLan????????????????????????????????????
                for (DeviceInfo deviceInfo : deviceListFromLan) {
                    Log.d(TAG, "getStrName: " + deviceInfo.getStrName());
                    Log.d(TAG, "getnDevID: " + deviceInfo.getnDevID());
                    Log.d(TAG, "getnDevID: " + deviceInfo.getnDevID());
                    // ?????????????????????????????????ID
                    Log.d(TAG, "getnID: " + deviceInfo.getnID());
                }
                if (deviceListFromLan.size() > 0) {
                    connectDevices = deviceListFromLan;
                    SetupNetDemoActivity.setDeviceInfo(deviceListFromLan.get(0));
                    handler.obtainMessage(lanScanMsgWhat).sendToTarget();
                    break;
                }
            }
            Log.d(TAG, "LanScanThread: " + mLanScanLabel + "  end");
        }
    }


    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage(msg);
        builder.setPositiveButton("??????", (dialog, which) -> {
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

        // ??????wifi???????????????
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
        Log.d(TAG, "????????????gps???" + providerEnabled);
        if (!providerEnabled) {
            new AlertDialog.Builder(this)
                    .setMessage("?????????GPS")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
                    Log.d(TAG, "WifiBroadcastReceiver: NETWORK_STATE_CHANGED_ACTION ??????????????????:" + networkInfo.getState());
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        int type = networkInfo.getType();
                        Log.d(TAG, "WifiBroadcastReceiver: ?????????????????? " + type);
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            String ssid = mWifiManager.getConnectionInfo().getSSID().replace("\"", "");
                            etSSID.setText(ssid);

                            // ???SharePreferences?????????????????????
                            SharedPreferences sp = getSharedPreferences("wifi", MODE_PRIVATE);
                            String ssidSp = sp.getString("ssid", "");
                            String pwdSp = sp.getString("pwd", "");
                            if (ssid.equals(ssidSp)) {
                                etPwd.setText(pwdSp);
                            }
                        }
                    } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        Log.d(TAG, "WifiBroadcastReceiver: ????????????");
                    }
                    break;
                }

                case WifiManager.WIFI_STATE_CHANGED_ACTION: { // wifi??????
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

                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION: { // ????????????
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