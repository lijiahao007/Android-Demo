package com.example.myapplication.setupnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
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
import com.macrovideo.sdk.objects.DeviceStatus;
import com.macrovideo.sdk.objects.LoginParam;
import com.macrovideo.sdk.setting.DeviceNetworkSetting;
import com.macrovideo.sdk.setting.NetworkConfigInfo;
import com.macrovideo.sdk.tools.DeviceScanner;

import java.util.ArrayList;

public class APConnectActivity extends AppCompatActivity {

    private Button btnConnectAP;
    private final String TAG = "APConnectActivity";
    private WifiBroadcastReceiver wifiBroadcastReceiver;
    private EditText etSSID;
    private EditText etPwd;
    private volatile int lanScanThreadLabel = 0;
    private DeviceInfo curDeviceInfo; // 当前设备信息
    private final int deviceFoundWhat = 0; // 在局域网找到设备的消息what
    private final int deviceConnectToNetWhat = 1; // 设备配网成功消息what
    private final int wifiChangeFromDeviceAPToOtherWifiWhat = 2; // wifi从设备AP切换到其他wifi消息what
    private final int wifiChangeToTargetWifiWhat = 3; // wifi切换到目标wifi消息what
    private WifiInfo prevWifiInfo = null; // 上一次的网络信息
    private WifiInfo curWifiInfo = null; // 当前的网络信息


    private DeviceInfo testDeviceInfo = new DeviceInfo(
            -1,
            32590556, // 设备ID (int)
            "32590556", // 设备ID (string)
            "192.168.1.1", // 设备摄像头默认IP
            8800, // 端口
            "admin", // 登录用户名
            "", // 登录密码
            null, // mac地址
            null, // 域名
            0 // saveType
    );
    private EditText etUserPassword;
    private EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apconnect);

        // 1. 跳转WIFI界面连接设备热点
        btnConnectAP = findViewById(R.id.btnConnectAP);
        btnConnectAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(APConnectActivity.this)
                        .setMessage("请连接MV开头的热点（该热点由设备创建）")
                        .setPositiveButton("OK", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            }
        });

        etSSID = findViewById(R.id.etSSID);
        etPwd = findViewById(R.id.etPwd);

        // 2. 注册wifi广播，监听wifi状态变化
        wifiBroadcastReceiver = new WifiBroadcastReceiver();

        Button btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curDeviceInfo != null) {
                    Log.i(TAG, "WifiBroadcastReceiver: curDeviceInfo = " + curDeviceInfo.getnDevID() + ", " + curDeviceInfo.getStrName());

                    long lNowTime = System.currentTimeMillis();
                    boolean updateOnLineStatus = (lNowTime - curDeviceInfo.getlOnLineStatChaneTime() >= 30000)
                            || curDeviceInfo.getnOnLineStat() == Defines.ON_LINE_STAT_UNKNOWN
                            || curDeviceInfo.getnOnLineStat() == Defines.ON_LINE_STAT_READY;

                    if (updateOnLineStatus) {
                        // 3. 查询设备状态
                        DeviceStatus deviceStatus = LoginHelper.getDeviceStatus(curDeviceInfo);
                        Log.i(TAG, "互联网查询， internetQueryDevice: deviceStatus: " + deviceStatus + " " + deviceStatus.getnResult());
                        if (deviceStatus != null && deviceStatus.getnResult() == ResultCode.NV_RESULT_SUCCEED) {
                            int onlineStat = deviceStatus.getnOnlineStat();
                            if (onlineStat == 1) {
                                // 设备在线
                                curDeviceInfo.setnOnLineStat(Defines.ON_LINE_STAT_WAN);
                                Log.i(TAG, "internetQueryDevice: 设备在线");
                                deviceFoundHandler.obtainMessage(deviceFoundWhat).sendToTarget();
                            } else if (onlineStat == 0) {
                                curDeviceInfo.setnOnLineStat(Defines.ON_LINE_STAT_OFF);
                                Log.i(TAG, "internetQueryDevice: 设备离线");
                            } else if (onlineStat == 10) {
                                curDeviceInfo.setnOnLineStat(Defines.ON_LINE_STAT_READY);
                                Log.i(TAG, "internetQueryDevice: 设备准备中");
                            }
                        } else {
                            Log.i(TAG, "internetQueryDevice: 设备状态查询失败");
                        }
                    }
                }
            }
        });

        // 3. 获取登录名和登录密码
        etUserName = findViewById(R.id.etUserName);
        etUserPassword = findViewById(R.id.etUserPassword);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 2. 注册wifi广播，监听wifi状态变化
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); // 网络状态变化时会发广播（例如从一个wifi连到另外一个wifi，从wifi-》移动网络）
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // wifi 的开闭状态变化时会发广播（例如打开wifi，关闭wifi）
        registerReceiver(wifiBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiBroadcastReceiver);
        lanScanThreadLabel++; // 结束lan扫描线程
    }

    class WifiBroadcastReceiver extends BroadcastReceiver {
        boolean isDeviceConnectToWifi = false; // 设备是否配网成功

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION: {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        String ssid = wifiManager.getConnectionInfo().getSSID().replace("\"", "");
                        String editSSID = etSSID.getText().toString();
                        String editPwd = etPwd.getText().toString();

                        prevWifiInfo = curWifiInfo;
                        curWifiInfo = wifiManager.getConnectionInfo();
                        Log.i(TAG, "WifiBroadcastReceiver: prevWifiInfo = " + prevWifiInfo + ", curWifiInfo = " + curWifiInfo);

                        if (ssid.startsWith("MV") && !editSSID.isEmpty() && !editPwd.isEmpty()) {
                            Toast.makeText(context, "热点连接完成", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "热点连接完成");
                            // 3. 成功连接设备热点，就可以登录设备了
                            // 3.1 获取设备ID
                            String deviceId = ssid.substring(2);
                            curDeviceInfo = new DeviceInfo(
                                    -1,
                                    Integer.parseInt(deviceId), // 设备ID (int)
                                    deviceId, // 设备ID (string)
                                    "192.168.1.1", // 设备摄像头默认IP
                                    8800, // 端口
                                    etUserName.getText().toString(), // 登录用户名
                                    etUserPassword.getText().toString(), // 登录密码
                                    null, // mac地址
                                    null, // 域名
                                    0 // saveType
                            );
                            Log.i(TAG, "WifiBroadcastReceiver: curDeviceInfo = " + curDeviceInfo.getnDevID() + ", " + curDeviceInfo.getStrName());

                            // 3.2 登录设备
                            LoginParam loginParam = new LoginParam();
                            loginParam.setDeviceInfo(curDeviceInfo);
                            loginParam.setConnectType(Defines.LOGIN_FOR_SETTING); // 登录类型：设置
                            // TODO: 下面登录设备的操作放到子线程中。
                            LoginHelper.loginDevice(APConnectActivity.this, loginParam, new ILoginDeviceCallback() {
                                @Override
                                public void onLogin(LoginHandle loginHandle) {
                                    if (loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                                        // 3.2.1 登录成功，进行配网操作
                                        Log.i(TAG, "登录设备成功 " + loginHandle.getnResult());
                                        SharedPreferences sharedPreferences = getSharedPreferences("wifi", MODE_PRIVATE);
                                        sharedPreferences.edit()
                                                .putString("ssid", deviceId)
                                                .putString("pwd", editPwd)
                                                .apply();

                                        // 3.2.2 进行配网操作
                                        NetworkConfigInfo networkConfigInfo = DeviceNetworkSetting.setNetworkConfig(
                                                loginHandle,
                                                curDeviceInfo,
                                                1002, // 配网模式 1002：路由器模式(默认传这个)
                                                editSSID,
                                                editPwd);
                                        Log.i(TAG, "配网结果：" + networkConfigInfo);
                                        if (networkConfigInfo != null && networkConfigInfo.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                                            // 3.2.3 配网成功
                                            Log.i(TAG, "配网成功");
                                            deviceFoundHandler.obtainMessage(deviceConnectToNetWhat).sendToTarget();
                                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                                                // 3.2.4 手机连接到目标wifi上 (这部分只有在sdk28及以下的版本才有用)
                                                WifiAssistant.connectWifi(context, editSSID, editPwd, false, new WifiAssistant.WifiAutoConnectListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        // (1) 手机成功连接到目标wifi，开启局域网扫描
                                                        Log.i(TAG, "低版本手机连接到目标wifi上:" + editSSID + " " + System.currentTimeMillis());
                                                        deviceFoundHandler.obtainMessage(wifiChangeToTargetWifiWhat).sendToTarget();
                                                    }

                                                    @Override
                                                    public void onFailure() {
                                                        // (2) 手机连接到了其他的wifi，开启互联网查询
                                                        Log.i(TAG, "低版本手机连接到其他wifi上:" + ssid + " " + System.currentTimeMillis());
                                                        deviceFoundHandler.obtainMessage(wifiChangeFromDeviceAPToOtherWifiWhat).sendToTarget();
                                                    }
                                                });
                                            }
                                        } else {
                                            Log.i(TAG, "配网失败 " + loginHandle.getnResult());
                                        }
                                    } else {
                                        Log.i(TAG, "登录设备失败 " + loginHandle.getnResult());
                                    }
                                }
                            });

                        } else if (
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                                        prevWifiInfo != null &&
                                        prevWifiInfo.getSSID().contains("MV")
                        ) {
                            // 3.2.5 对于高版本手机，在设备热点关闭后，会随机切换到一个wifi中。如果是切换到目标wifi，则进行局域网扫描。否则，开启互联网查询。
                            if (curWifiInfo.getSSID().equals("\"" + editSSID + "\"")) {
                                Log.i(TAG, "高版本手机连接到目标wifi上");
                                deviceFoundHandler.obtainMessage(wifiChangeToTargetWifiWhat).sendToTarget();
                            } else {
                                Log.i(TAG, "高版本手机连接到其他wifi上");
                                deviceFoundHandler.obtainMessage(wifiChangeFromDeviceAPToOtherWifiWhat).sendToTarget();
                            }
                        } else {
                            Log.i(TAG, "WifiBroadcastReceiver: 网络状态变更，但不是设备热点连接");
                            // 根据当前wifi连接信息设置wifi ssid、pwd信息
                            etSSID.setText(ssid);
                            SharedPreferences sharedPreferences = getSharedPreferences("wifi", MODE_PRIVATE);
                            String ssid1 = sharedPreferences.getString("ssid",  "");
                            String pwd = sharedPreferences.getString("pwd", "");
                            if (ssid1.equals(ssid)) {
                                etPwd.setText(pwd);
                            }
                            Log.i(TAG, " ssid: " + ssid + " pwd: " + editPwd);
                        }
                    }
                    break;
                }

                case WifiManager.WIFI_STATE_CHANGED_ACTION: {
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                    Log.i(TAG, "WifiBroadcastReceiver: WIFI_STATE_CHANGED_ACTION, wifi状态改变:" + wifiState);
                    break;
                }

            }
        }

    }


    class LanScanThread extends Thread {

        private int label = 0;
        private boolean isFound = false;

        public LanScanThread(int label) {
            super("LanScanThread" + label);
            this.label = label;
        }

        @Override
        public void run() {
            super.run();
            while (label == lanScanThreadLabel) {
                Log.i(TAG, "LanScanThread: 开始扫描设备" + label);
                ArrayList<DeviceInfo> deviceListFromLan = DeviceScanner.getDeviceListFromLan();
                Log.i(TAG, "LanScanThread: deviceListFromLan: " + deviceListFromLan + " size: " + deviceListFromLan.size());

                if (deviceListFromLan.size() > 0) {
                    for (DeviceInfo deviceInfo : deviceListFromLan) {
                        if (deviceInfo.getnDevID() == curDeviceInfo.getnDevID()) {
                            // 找到设备
                            if (label == lanScanThreadLabel) {
                                // 该线程是否最新扫描线程
                                lanScanThreadLabel++;
                                isFound = true;
                            }
                            break;
                        }
                    }
                }
            }
            if (isFound) {
                deviceFoundHandler.obtainMessage(deviceFoundWhat, true).sendToTarget();
            }
        }
    }

    // 该handler专门用于处理设备扫描
    Handler deviceFoundHandler = new Handler(Looper.getMainLooper()) {

        boolean isDeviceConnectToWifi = false;
        boolean isWifiChangeToTargetWifi = false;
        boolean isWifiChangeFromDeviceAPToOtherWifi = false;


        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case deviceFoundWhat: {
                    boolean isFound = (boolean) msg.obj;
                    if (isFound) {
                        Log.i(TAG, "handleMessage: 找到设备");
                        Toast.makeText(APConnectActivity.this, "找到设备", Toast.LENGTH_SHORT).show();
                        SetupNetDemoActivity.setDeviceInfo(curDeviceInfo);
                        Toast.makeText(APConnectActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }

                case deviceConnectToNetWhat: {
                    Log.i(TAG, "设备收到wifi配置信息");
                    isDeviceConnectToWifi = true;
                    if (isWifiChangeToTargetWifi) {
                        lanScanDevice();
                    } else if (isWifiChangeFromDeviceAPToOtherWifi) {
                        internetQueryDevice();
                    }
                    break;
                }

                case wifiChangeToTargetWifiWhat: {
                    Log.i(TAG, "手机切换到目标wifi上，成功");
                    isWifiChangeToTargetWifi = true;
                    if (isDeviceConnectToWifi) {
                        lanScanDevice();
                    }
                    break;
                }

                case wifiChangeFromDeviceAPToOtherWifiWhat: {
                    Log.i(TAG, "手机切换到目标wifi上, 失败");
                    isWifiChangeFromDeviceAPToOtherWifi = true;
                    if (isDeviceConnectToWifi) {
                        internetQueryDevice();
                    }
                    break;
                }
            }
        }

        public void reset() {
            isDeviceConnectToWifi = false;
            isWifiChangeToTargetWifi = false;
            isWifiChangeFromDeviceAPToOtherWifi = false;
        }

        private void lanScanDevice() {
            Log.i(TAG, "开始局域网扫描");
            reset();
            lanScanThreadLabel++;
            LanScanThread lanScanThread = new LanScanThread(lanScanThreadLabel);
            lanScanThread.start();
        }

        private void internetQueryDevice() {
            Log.i(TAG, "开始互联网查询");
            reset();
            new Thread("互联网查询设备是否在线") {
                @Override
                public void run() {
                    super.run();
                    // TODO: 这里查询不到设备在线。
                    DeviceStatus deviceStatus = LoginHelper.getDeviceStatus(curDeviceInfo);
                    Log.i(TAG, "互联网查询， internetQueryDevice: deviceStatus: " + deviceStatus);
                    if (deviceStatus != null && deviceStatus.getnResult() == ResultCode.NV_RESULT_SUCCEED) {
                        int onlineStat = deviceStatus.getnOnlineStat();
                        if (onlineStat == 1) {
                            // 设备在线
                            curDeviceInfo.setnOnLineStat(Defines.ON_LINE_STAT_WAN);
                            Log.i(TAG, "internetQueryDevice: 设备在线");
                            deviceFoundHandler.obtainMessage(deviceFoundWhat).sendToTarget();
                        } else if (onlineStat == 0) {
                            curDeviceInfo.setnOnLineStat(Defines.ON_LINE_STAT_OFF);
                            Log.i(TAG, "internetQueryDevice: 设备离线");
                            deviceFoundHandler.obtainMessage(deviceFoundWhat).sendToTarget();
                        } else if (onlineStat == 10) {
                            curDeviceInfo.setnOnLineStat(Defines.ON_LINE_STAT_READY);
                            Log.i(TAG, "internetQueryDevice: 设备准备中");
                        }
                    }
                }
            }.start();
        }
    };


}