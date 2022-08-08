package com.example.myapplication.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityNotificationDemoBinding;
// 参考 https://llw-study.blog.csdn.net/article/details/125446355?spm=1001.2014.3001.5502
public class NotificationDemoActivity extends AppCompatActivity {
    private ActivityNotificationDemoBinding binding;

    // 1. channel Id
    private String channelId = "test";
    // 2. channel 名称
    private String channelName = "测试通知";
    // 3. channel 重要级别
    private int channelImportant = NotificationManagerCompat.IMPORTANCE_HIGH;
    // 4. 通知管理者
    private NotificationManager notificationManager;
    // 5. 通知
    private Notification notification;
    // 6. 通知id
    private int notificationId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. 获取系统通知服务
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // 2. 初始化通知
        initNotification();

        // 3. 显示通知
        binding.btnNormalNotification.setOnClickListener(v -> {

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int channelImportant) {
        // 1. 创建通知渠道
        NotificationChannel channel = new NotificationChannel(channelId, channelName, channelImportant);
        notificationManager.createNotificationChannel(channel);
    }

    private void initNotification() {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 1. Android 8.0以上，需要创建通道，并向通知构建者
            createNotificationChannel(channelId, channelName, channelImportant);
            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            // 2. Android 8.0以下，不需要创建通道，直接创建通知构建者
            builder = new NotificationCompat.Builder(this);
        }

        // 3. 设置通知属性
        builder.setSmallIcon(R.drawable.ic_note)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_note))
                .setContentTitle("title:note")
                .setContentText("text:搞钱");

        // 4. 构建通知
        notification = builder.build();
    }


}