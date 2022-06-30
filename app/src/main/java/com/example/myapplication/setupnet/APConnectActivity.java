package com.example.myapplication.setupnet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.IntentFlagActivity;
import com.macrovideo.sdk.defines.Defines;
import com.macrovideo.sdk.objects.DeviceInfo;
import com.macrovideo.sdk.objects.LoginParam;

public class APConnectActivity extends AppCompatActivity {

    private Button btnConnectAP;
    private final String TAG = "APConnectActivity";
    private WifiBroadcastReceiver wifiBroadcastReceiver;
    private EditText etSSID;
    private EditText etPwd;

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 2. 注册wifi广播，监听wifi状态变化
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); // 网络状态变化时会发广播（例如从一个wifi连到另外一个wifi，从wifi-》移动网络）
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); // wifi 的开闭状态变化时会发广播（例如打开wifi，关闭wifi）
        wifiBroadcastReceiver = new WifiBroadcastReceiver();
        registerReceiver(wifiBroadcastReceiver, intentFilter);

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
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        String ssid = wifiManager.getConnectionInfo().getSSID();
                        String editSSID = etSSID.getText().toString();
                        String editPwd = etPwd.getText().toString();
                        if (ssid.startsWith("MV") && !editSSID.isEmpty() && !editPwd.isEmpty()) {
                            Toast.makeText(context, "热点连接完成", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "热点连接完成");
                            // 3. 成功连接设备热点，就可以登录设备了
                            // 3.1 获取设备ID
                            String deviceId = ssid.substring(2);
                            DeviceInfo deviceInfo = new DeviceInfo(
                                    -1,
                                    Integer.parseInt(deviceId), // 设备ID (int)
                                    deviceId, // 设备ID (string)
                                    "192.168.1.1", // 路由器IP
                                    8800, // 端口
                                    "admin", // 登录用户名
                                    "", // 登录密码
                                    null, // mac地址
                                    null, // 域名
                                    0 // saveType
                            );
                            // 3.2 登录设备
                            LoginParam loginParam = new LoginParam();
                            loginParam.setDeviceInfo(deviceInfo);
                            loginParam.setConnectType(Defines.LOGIN_FOR_SETTING); // 登录类型：设置



                        } else {

                        }
                    } else {
                        Log.i(TAG, "WifiBroadcastReceiver: 热点连接失败 ");
                    }
                    break;

                case WifiManager.WIFI_STATE_CHANGED_ACTION:

                    break;
            }


        }
    }

}