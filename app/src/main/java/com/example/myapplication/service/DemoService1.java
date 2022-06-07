package com.example.myapplication.service;

import static android.app.PendingIntent.getActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.activity.Teacher;

import java.util.Timer;
import java.util.TimerTask;

public class DemoService1 extends Service {

    static final String TAG = "DemoService1";
    private final IBinder binder = new ServiceBinder();

    public DemoService1() {
        Log.i(TAG, "DemoService1()" + this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind" + this);
        Toast.makeText(this, "服务绑定成功", Toast.LENGTH_SHORT).show();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind" + this);
        Toast.makeText(this, "服务解绑", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand" + this);
        Toast.makeText(this, "服务开启成功", Toast.LENGTH_SHORT).show();

        Notification.Builder builder = new Notification.Builder(this); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, ServiceDemoActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, PendingIntent.FLAG_IMMUTABLE))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("title") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("要显示的内容") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间


        // 为notification builder 设置 channel
        String CHANNEL_ONE_ID = "com.example.myapplication";
        String CHANNEL_ONE_NAME = "服务";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //修改安卓8.1以上系统报错
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
            notificationChannel.setShowBadge(false);//是否显示角标
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(CHANNEL_ONE_ID);
        }

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);// 开始前台服务
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy" + this);
        Toast.makeText(this, "服务销毁", Toast.LENGTH_SHORT).show();
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }

    class ServiceBinder extends Binder {
        public DemoService1 getDemoService1() {
            return DemoService1.this;
        }
    }
}