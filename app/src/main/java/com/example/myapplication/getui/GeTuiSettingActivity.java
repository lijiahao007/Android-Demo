package com.example.myapplication.getui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityGeTuiSettingBinding;
import com.igexin.sdk.PushManager;

public class GeTuiSettingActivity extends BaseActivity<ActivityGeTuiSettingBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPushSwitch();
        initIntentGenerate();
        initDirectToTarget();

    }

    private void initDirectToTarget() {
        binding.llDirectGotoTargetActivity.setOnClickListener(view -> {
            Intent intent = new Intent(this, GTOpenActivity.class);
            startActivity(intent);
        });
    }

    private void initIntentGenerate() {
        binding.etIntentParam.setVisibility(View.GONE);

        binding.btnGenerateIntent.setOnClickListener(view -> {
            binding.etIntentParam.setVisibility(View.VISIBLE);
            binding.etIntentParam.setText("");

            Intent intent = new Intent(this, GTOpenActivity.class);
            //Scheme协议（gtpushscheme://com.getui.push/detail?）开发者可以自定义
            intent.setData(Uri.parse("gtpushscheme://com.example.getuipush/detail?"));
            //如果设置了 package，则表示目标应用必须是 package 所对应的应用
            intent.setPackage(getPackageName());

            //intent 中添加自定义键值对，value 为 String 型
            intent.putExtra("payload", "payloadStr");
            //gttask 不用赋值，添加 gttask 字段后，个推给客户端的 intent 里会自动拼接上 taskid 和 actionid ；app 端接收到参数以后，可通过下方 6.3 的 pushClick 方法解析及上报埋点。
            intent.putExtra("gttask", "");

            // 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息，强烈推荐使用Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);

            // 打印出的intentUri值就是设置到推送消息中intent字段的值
            Log.d(TAG, intentUri);

            binding.etIntentParam.setText(intentUri);
        });
    }

    private void initPushSwitch() {
        // 1. 获取当前是否开启了推送
        boolean pushTurnedOn = PushManager.getInstance().isPushTurnedOn(this);
        binding.switchPush.setChecked(pushTurnedOn);

        binding.switchPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PushManager.getInstance().turnOnPush(GeTuiSettingActivity.this);
                } else {
                    PushManager.getInstance().turnOffPush(GeTuiSettingActivity.this);
                }
            }
        });
    }
}