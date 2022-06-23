package com.example.myapplication.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.R;

public class NotificationDemoActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "default";
    private static final int NOTIFICATION_REQUEST_CODE = 1;
    private static final int STATUS_BAR_NOTIFICATION_ID = 1;
    private static final int POPUP_NOTIFICATION = 2;
    private static final int LOCK_NOTIFICATION = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_demo);

        // 状态栏通知
        findViewById(R.id.btn_status_bar_notification).setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "default_channel", NotificationManager.IMPORTANCE_HIGH);
                getSystemService(NotificationManager.class).createNotificationChannel(channel);
            }

            Intent intent = new Intent(this, NotificationDemoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("通知demo")
                    .setContentText("状态栏通知内容！！！")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("状态栏通知内容"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setProgress(100, 0, false);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(STATUS_BAR_NOTIFICATION_ID, builder.build());


            new Thread(() -> {
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 更新进度，不需要在主线程中也可以更新
                    builder.setProgress(100, i, false);
                    notificationManager.notify(STATUS_BAR_NOTIFICATION_ID, builder.build());
                }
                // 取消
                builder.setProgress(0, 0, false);
                notificationManager.notify(STATUS_BAR_NOTIFICATION_ID, builder.build());
            }).start();
        });

        // 提醒式通知
        findViewById(R.id.btn_popup_notification).setOnClickListener(view -> {
            // TODO: 提醒式通知
        });

        // 锁屏通知
        findViewById(R.id.btn_lock_notification).setOnClickListener(view -> {
            // TODO: 锁屏通知
        });
    }
}