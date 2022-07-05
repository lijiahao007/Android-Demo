package com.example.myapplication.setupnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.macrovideo.sdk.setting.AccountConfigInfo;
import com.macrovideo.sdk.setting.DeviceAccountSetting;
import com.macrovideo.sdk.tools.DeviceScanner;
import com.macrovideo.sdk.tools.QRCodeUtils;

import java.util.ArrayList;

import kotlin.random.Random;

public class QRCodeConnectActivity extends AppCompatActivity {
    private EditText etSSID;
    private EditText etPwd;
    WifiBroadcastReceiver wifiBroadcastReceiver;
    private Button btnGenerateQRCode;
    private int mConfigID = 0;
    private int qrcodeSize = 0;
    private ImageView ivQRCode;
    volatile int lanScanThreadLabel = 0;
    private final static String TAG = "QRCodeConnectActivity";
    private final int deviceFoundWhat = 0;
    private DeviceInfo curDeviceInfo = null;
    private EditText etUserPassword;
    private EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_connect);

        etSSID = findViewById(R.id.etSSID);
        etPwd = findViewById(R.id.etPwd);
        wifiBroadcastReceiver = new WifiBroadcastReceiver();
        qrcodeSize = getResources().getDimensionPixelSize(R.dimen.qrcode_size);

        ivQRCode = findViewById(R.id.ivQRCode);

        btnGenerateQRCode = findViewById(R.id.btnGenerateQRCode);
        btnGenerateQRCode.setOnClickListener(view -> {
            mConfigID = Random.Default.nextInt(1, 256);
            String ssid = etSSID.getText().toString();
            String pwd = etPwd.getText().toString();
            Bitmap configNetQRCodeBitmap = QRCodeUtils.createConfigNetQRCodeBitmap(
                    QRCodeUtils.CONFIG_NET_MODE_WAN, // 配网模式，默认选择该模式即可（广域网模式）
                    mConfigID, // 随机config, 1~255
                    "", // 账号id， 传“”即可
                    ssid,
                    pwd,
                    qrcodeSize,
                    qrcodeSize
            );
            ivQRCode.setImageBitmap(configNetQRCodeBitmap);
            lanScanThreadLabel++;
            LanScanThread lanScanThread = new LanScanThread(lanScanThreadLabel);
            lanScanThread.start();
        });

        // 获取登录名和登录密码
        etUserName = findViewById(R.id.etUserName);
        etUserPassword = findViewById(R.id.etUserPassword);

    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); // 网络状态变化时会发广播（例如从一个wifi连到另外一个wifi，从wifi-》移动网络）
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // wifi 的开闭状态变化时会发广播（例如打开wifi，关闭wifi）
        registerReceiver(wifiBroadcastReceiver, intentFilter);
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
                ArrayList<DeviceInfo> deviceListFromLan = DeviceScanner.getDeviceListFromLan(mConfigID);
                Log.i(TAG, "LanScanThread: deviceListFromLan: " + deviceListFromLan + " size: " + deviceListFromLan.size());

                if (deviceListFromLan.size() > 0) {
                    curDeviceInfo = deviceListFromLan.get(0);
                    SetupNetDemoActivity.setDeviceInfo(curDeviceInfo);
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                deviceFoundHandler.obtainMessage(deviceFoundWhat, true).sendToTarget();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiBroadcastReceiver);
    }

    class WifiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION: {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        String ssid = wifiManager.getConnectionInfo().getSSID().replace("\"", "");
                        etSSID.setText(ssid);
                    }
                    break;
                }
            }
        }
    }


    // 该handler专门用于处理设备扫描
    Handler deviceFoundHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case deviceFoundWhat: {
                    boolean isFound = (boolean) msg.obj;
                    if (isFound) {
                        Log.i(TAG, "handleMessage: 找到设备");
                        Toast.makeText(QRCodeConnectActivity.this, "找到设备", Toast.LENGTH_SHORT).show();
                        // 登录设备，设置账号密码
                        LoginParam loginParam = new LoginParam(SetupNetDemoActivity.getDeviceInfo(), Defines.LOGIN_FOR_SETTING);
                        DeviceInfo deviceInfo = SetupNetDemoActivity.getDeviceInfo();
                        LoginHelper.loginDevice(QRCodeConnectActivity.this, loginParam, new ILoginDeviceCallback() {
                            @Override
                            public void onLogin(LoginHandle loginHandle) {
                                if (loginHandle != null && loginHandle.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                                    Log.i(TAG, "handleMessage: 登录成功");
                                    AccountConfigInfo accountConfig = DeviceAccountSetting.getAccountConfig(deviceInfo, loginHandle);
                                    AccountConfigInfo newAccountConfig = DeviceAccountSetting.setAccountConfig(deviceInfo, etUserName.getText().toString(), etUserPassword.getText().toString(), accountConfig.getnUserID(), loginHandle);
                                    if (newAccountConfig != null && newAccountConfig.getnResult() == ResultCode.RESULT_CODE_SUCCESS) {
                                        Log.i(TAG, "handleMessage: 设置账号密码成功");
                                        deviceInfo.setStrUsername(etUserName.getText().toString());
                                        deviceInfo.setStrPassword(etUserPassword.getText().toString());
                                        SetupNetDemoActivity.setDeviceInfo(deviceInfo);
                                        Toast.makeText(QRCodeConnectActivity.this, "设置账号密码成功", Toast.LENGTH_SHORT).show();
                                        QRCodeConnectActivity.this.finish();
                                    } else {
                                        Log.i(TAG, "handleMessage: 设置账号密码失败");
                                        Toast.makeText(QRCodeConnectActivity.this, "设置账号密码失败", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.i(TAG, "handleMessage: 登录失败");
                                    Toast.makeText(QRCodeConnectActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    break;
                }
            }
        }
    };
}