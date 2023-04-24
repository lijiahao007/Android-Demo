package com.example.myapplication.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;

public class NotificationUtils {

    // 渠道Id
    private String channelId = "test";
    // 渠道名
    private String channelName = "测试通知";
    // 通知管理者
    private NotificationManagerCompat notificationManager;
    // 通知
    private Notification notification;
    // 通知Id
    private int notificationId = 1;
    private int index = 1;

    private void createNotification(Context context, Intent intent, String msg) {
        // 1. 获取通知管理器
        if (notificationManager == null) {
            notificationManager = NotificationManagerCompat.from(context);
        }
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 2. 对于Android 8以上, 创建channelID
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        // 3. 创建PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String content = "测试的内容是报警消息跳转测试的内容是报警消息跳转测试的内容是报警消息跳转测试的内容是报警消息跳转测试的内容是报警消息跳转";
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("【" + msg + "】这是一个测试的通知" + index++) // 通知标题
                .setContentText(content) // 通知内容
                .setContentIntent(pendingIntent) // 内容Intent
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));  // 内容过长时可折叠、展开

        notification = builder.build();
    }

    private void showNotification() {
        if (notification == null || notificationManager == null) {
            return;
        }
        // 显示时一个notificationId 对应通知栏上的一个通知。
        notificationManager.notify(notificationId++, notification);
    }
}
