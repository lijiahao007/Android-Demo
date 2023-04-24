package com.example.myapplication.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AutoPushService extends Service {


    // 通知管理者
    private final NotificationManagerCompat notificationManager;
    private Timer timer;

    private static final String TAG = "AutoPushService";


    public AutoPushService() {
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification(null, "AutoPushService 前台通知", "AutoPushFore", "AutoPushFore");
        int notificationId = 1;
        showNotification(notificationId, notification);
        startForeground(notificationId, notification);

        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            final AtomicInteger notificationId = new AtomicInteger(2);
            @Override
            public void run() {
                int curNotificationId = notificationId.getAndAdd(1);
                Log.i(TAG, "id：" + curNotificationId);
                Intent intent = new Intent(AutoPushService.this, NotificationDemoActivity.class);
                Notification notification = createNotification(intent, "测试通知" + curNotificationId, "test", "test");
                showNotification(curNotificationId, notification);
            }
        };
        timer.schedule(task, 1000 * 2);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private Notification createNotification(@Nullable Intent intent, String msg, String channelId, String channelName) {
        // 1. 获取通知管理器
        if (notificationManager == null) {
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

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle(msg) // 通知标题
                .setContentText(msg) // 通知内容
                .setContentIntent(pendingIntent) // 内容Intent
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));  // 内容过长时可折叠、展开

        return builder.build();
    }

    private void showNotification(int notificationId, Notification notification) {
        if (notification == null || notificationManager == null) {
            return;
        }
        // 显示时一个notificationId 对应通知栏上的一个通知。
        notificationManager.notify(notificationId, notification);
    }
}