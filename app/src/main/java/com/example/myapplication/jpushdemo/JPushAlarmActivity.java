package com.example.myapplication.jpushdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityJpushAlarmBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class JPushAlarmActivity extends BaseActivity<ActivityJpushAlarmBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {

            // 1. 获取Intent中的参数
            String deviceId = getIntent().getStringExtra("deviceId");
            int type = getIntent().getIntExtra("type", 0);
            Log.i(TAG, "Intent中的透传参数 --- deviceId=" + deviceId + "  type=" + type);


            // 2. 附加字段
            String data = null;
            //获取华为平台附带的jpush信息
            if (getIntent().getData() != null) {
                data = getIntent().getData().toString();
            }

            //获取fcm/oppo/小米/vivo/魅族/荣耀 平台附带的jpush信息
            if (TextUtils.isEmpty(data) && getIntent().getExtras() != null) {
                data = getIntent().getExtras().getString("JMessageExtra");
            }

            Log.i(TAG, "额外字段 - 【" + data + "】");

            if (TextUtils.isEmpty(data)) return;
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject n_extras = jsonObject.optJSONObject("n_extras");
                System.out.println(n_extras);
                Log.i(TAG, "额外字段 - n_extras=【" + n_extras + "】");

                String password = n_extras.optString("password");
                boolean isOnline = n_extras.optBoolean("isOnline");

                Log.i(TAG, "额外字段 - password=【" + password + "】  isOnline=【" + isOnline + "】");

                Toast.makeText(this,
                        deviceId + "\n" + type + "\n" + password + "\n" + isOnline,
                        Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}