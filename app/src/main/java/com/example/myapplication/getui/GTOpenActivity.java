package com.example.myapplication.getui;

import static com.igexin.base.api.GTBase.context;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.databinding.ActivityGtopenBinding;
import com.igexin.sdk.PushManager;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public class GTOpenActivity extends BaseActivity<ActivityGtopenBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (null != intent) {
            String payload = intent.getStringExtra("payload");
            Log.d(TAG, "onCreate payload = " + payload + "   action=" + intent.getAction());
            binding.textView2.setText(payload);
        }

        pushClick(intent);

    }

    //或者在onNewIntent中接收（某些特殊情况下，例如：DemoActivity的启动模式是singleTop、singleTask或者是singleInstance，并且DemoActivity是打开的情况下）
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //获取自定义透传参数值
        if (null != intent) {
            String payload = intent.getStringExtra("payload");
            Log.d( TAG, "onNewIntent payload = " + payload + "  action=" + intent.getAction());
            binding.textView2.setText(payload);
        }
        pushClick(intent);
    }


    /**
     * 由于华为、oppo 无点击数报表返回，vivo无单推点击数报表返回，所以需要您在客户端埋点上报。
     * 点击厂商通知以后，在触发的activity的onCreate()方法里面接收相关参数，上报这 3 个离线厂商消息的点击数据。
     * 开发者可直接使用此方法示例
     *
     * @param intent
     * @return
     */
    public boolean pushClick(Intent intent) {
        boolean result = false;
        try {
            String taskid = intent.getStringExtra("gttask");
            String gtaction = intent.getStringExtra("gtaction");
            String clientid = PushManager.getInstance().getClientid(context);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 这里的messageid需要自定义， 保证每条消息汇报的都不相同
            String contentToDigest = taskid + clientid + uuid;
            byte[] md5s = MessageDigest.getInstance("MD5").digest(contentToDigest.getBytes(StandardCharsets.UTF_8));
            String messageid = new BigInteger(1, md5s).toString(16);

            /***
             * 第三方回执调用接口，可根据业务场景执行
             * 注意：只能用下面回执对应的机型进行上报点击测试，其它机型获取不到 gttask 字段
             *
             * 60020 华为点击
             * 60030 oppo点击
             * 60040 vivo点击
             * 60070 荣耀点击
             *
             * 埋点接口对应填写获取到的actionid值，如果有获取到 actionid 值，就上报埋点，如 果没有则不用上报。
             *
             */
            if (gtaction != null) {
                int actionid = Integer.parseInt(gtaction);
                result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, actionid);
            }
        } catch (Exception e) {
            Log.e(TAG, "pushClick: ", e);
        }
        return result;
    }
}