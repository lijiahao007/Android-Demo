package com.example.myapplication.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityNotificationDemoBinding;

public class NotificationDemoActivity extends BaseActivity<ActivityNotificationDemoBinding> {
    private ActivityNotificationDemoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 3. 显示通知
        binding.btnNormalNotification.setOnClickListener(v -> {
            Notification notification = createNotification(null, "Helloworld");

            // 通知Id
            int notificationId = 1;
            showNotification(notificationId, notification);
        });

        binding.btnForegroundService.setOnClickListener(v -> {
            Intent intent = new Intent(this, AutoPushService.class);
            startService(intent);
        });

        binding.btnCloseForegroundService.setOnClickListener(v -> {
            Intent intent = new Intent(this, AutoPushService.class);
            stopService(intent);
        });
    }


    // 渠道Id
    private String channelId = "test";
    // 渠道名
    private String channelName = "测试通知";
    // 通知管理者
    private NotificationManagerCompat notificationManager;
    // 通知
    private int index = 1;

    private Notification createNotification(Intent intent, String msg) {
        // 1. 获取通知管理器
        if (notificationManager == null) {
            notificationManager = NotificationManagerCompat.from(this);
        }
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2. 对于Android 8以上, 创建channelID
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        // 3. 创建PendingIntent
        PendingIntent pendingIntent = intent == null ? null : PendingIntent.getActivity(this, 0, intent, 0);

        String content = "测试的内容是报警消息跳转测试的内容是报警消息跳转测试的内容是报警消息跳转测试的内容是报警消息跳转测试的内容是报警消息跳转";
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("【" + msg + "】这是一个测试的通知" + index++) // 通知标题
                .setContentText(content) // 通知内容
                .setContentIntent(pendingIntent) // 内容Intent
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));  // 内容过长时可折叠、展开

        return builder.build();
    }


    private void showNotification(int notificationId, Notification notification) {
        if (notification == null || notificationManager == null) {
            return;
        }
        // 显示时一个notificationId 对应通知栏上的一个通知。
        notificationManager.notify(notificationId, notification);
    }


    private String channelIdFore = "test_foreground";
    // 渠道名
    private String channelNameFore = "前台通知";

    private Notification createForegroundService() {
        // 1. 获取通知管理器
        if (notificationManager == null) {
            notificationManager = NotificationManagerCompat.from(this);
        }

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2. 对于Android 8以上, 创建channelID
            notificationManager.createNotificationChannel(new NotificationChannel(channelIdFore, channelNameFore, NotificationManager.IMPORTANCE_HIGH));
            builder = new NotificationCompat.Builder(this, channelIdFore);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("这是一个前台服务"); // 通知标题

        return builder.build();
    }


}