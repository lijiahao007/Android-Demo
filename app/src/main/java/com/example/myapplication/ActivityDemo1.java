package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.activity.ExampleActivity1;
import com.example.myapplication.album.AlbumActivity;
import com.example.myapplication.album.PhotoVideoDemoActivity;
import com.example.myapplication.animation.AnimationDemoActivity;
import com.example.myapplication.broadcast.BroadcastDemoActivity1;
import com.example.myapplication.contentprovider.ContentProviderDemoActivity;
import com.example.myapplication.customview.CustomViewMenuActivity;
import com.example.myapplication.dialog.DialogActivity;
import com.example.myapplication.eventbus.EventBusDemoActivity;
import com.example.myapplication.fragment.FragmentMenuActivity;
import com.example.myapplication.gesture.GestureDemoActivity;
import com.example.myapplication.glidedemo.GlideDemoActivity;
import com.example.myapplication.handlerDemo.HandlerDemoActivity;
import com.example.myapplication.hotupdate.HotUpdateDemoActivity;
import com.example.myapplication.intent.IntentActivity;
import com.example.myapplication.jpushdemo.JPushAlarmActivity;
import com.example.myapplication.languagedemo.LanguageDemoActivity;
import com.example.myapplication.media.MediaDemoActivity;
import com.example.myapplication.multiThread.MultipleThreadDemoActivity;
import com.example.myapplication.notification.NotificationDemoActivity;
import com.example.myapplication.okhttp.OkHttpDemoActivity;
import com.example.myapplication.permission.PermissionMenuActivity;
import com.example.myapplication.qrcode.QRCodeMenuActivity;
import com.example.myapplication.recycleview.RecyclerViewDemoActivity;
import com.example.myapplication.rubbish.RubbishActivity;
import com.example.myapplication.rxjava.RxJavaDemoActivity;
import com.example.myapplication.service.ServiceDemoActivity;
import com.example.myapplication.setupnet.SetupNetDemoActivity;
import com.example.myapplication.shadowdemo.ShadoeDemoActivity;
import com.example.myapplication.statusinset.StatusInsetMenuActivity;
import com.example.myapplication.surfaceview.SurfaceDemoActivity;
import com.example.myapplication.tablayout.TabLayoutDemoActivity;
import com.example.myapplication.thirdpartlogin.LoginMenuActivity;
import com.example.myapplication.timebardemo.TimeBarDemoActivity;
import com.example.myapplication.unsplashproject.UnSplashMenuActivity;
import com.example.myapplication.viewpager.ViewPagerMenuActivity;
import com.example.myapplication.viewstate.ViewStateDemoActivity;
import com.example.myapplication.wifi.WifiDemoActivity;

import java.util.ArrayList;

public class ActivityDemo1 extends AppCompatActivity {

    private MenuRecyclerView recyclerView;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);

        recyclerView = findViewById(R.id.recycler_view);
        ArrayList<MenuAdapter.MenuInfo> menuList = new ArrayList<MenuAdapter.MenuInfo>() {{
            add(new MenuAdapter.MenuInfo("Glide", GlideDemoActivity.class));
            add(new MenuAdapter.MenuInfo("JPush", JPushAlarmActivity.class));
            add(new MenuAdapter.MenuInfo("语言", LanguageDemoActivity.class));
            add(new MenuAdapter.MenuInfo("Shadow", ShadoeDemoActivity.class));
            add(new MenuAdapter.MenuInfo("ViewState", ViewStateDemoActivity.class));
            add(new MenuAdapter.MenuInfo("TimeBar", TimeBarDemoActivity.class));
            add(new MenuAdapter.MenuInfo("StatusInset", StatusInsetMenuActivity.class));
            add(new MenuAdapter.MenuInfo("Unsplash", UnSplashMenuActivity.class));
            add(new MenuAdapter.MenuInfo("Rubbish", RubbishActivity.class));
            add(new MenuAdapter.MenuInfo("handler", HandlerDemoActivity.class));
            add(new MenuAdapter.MenuInfo("intent相关", IntentActivity.class));
            add(new MenuAdapter.MenuInfo("Activity相关", ExampleActivity1.class));
            add(new MenuAdapter.MenuInfo("Service相关", ServiceDemoActivity.class));
            add(new MenuAdapter.MenuInfo("BroadcastReceiver相关", BroadcastDemoActivity1.class));
            add(new MenuAdapter.MenuInfo("ContentProvider相关", ContentProviderDemoActivity.class));
            add(new MenuAdapter.MenuInfo("RecyclerView相关", RecyclerViewDemoActivity.class));
            add(new MenuAdapter.MenuInfo("TabLayout & ViewPager2相关", TabLayoutDemoActivity.class));
            add(new MenuAdapter.MenuInfo("权限申请相关", PermissionMenuActivity.class));
            add(new MenuAdapter.MenuInfo("自定义View相关", CustomViewMenuActivity.class));
            add(new MenuAdapter.MenuInfo("相册（图片视频来自 ”拍照录像“）", AlbumActivity.class));
            add(new MenuAdapter.MenuInfo("拍照录像", PhotoVideoDemoActivity.class));
            add(new MenuAdapter.MenuInfo("文件读写", MediaDemoActivity.class));
            add(new MenuAdapter.MenuInfo("二维码", QRCodeMenuActivity.class));
            add(new MenuAdapter.MenuInfo("Wifi管理", WifiDemoActivity.class));
            add(new MenuAdapter.MenuInfo("OKHTTP", OkHttpDemoActivity.class));
            add(new MenuAdapter.MenuInfo("tinker热更新（TODO）", HotUpdateDemoActivity.class));
            add(new MenuAdapter.MenuInfo("多线程", MultipleThreadDemoActivity.class));
            add(new MenuAdapter.MenuInfo("EventBus", EventBusDemoActivity.class));
            add(new MenuAdapter.MenuInfo("RxJava", RxJavaDemoActivity.class));
            add(new MenuAdapter.MenuInfo("通知", NotificationDemoActivity.class));
            add(new MenuAdapter.MenuInfo("摄像头配网", SetupNetDemoActivity.class));
            add(new MenuAdapter.MenuInfo("Surface", SurfaceDemoActivity.class));
            add(new MenuAdapter.MenuInfo("手势监听", GestureDemoActivity.class));
            add(new MenuAdapter.MenuInfo("动画", AnimationDemoActivity.class));
            add(new MenuAdapter.MenuInfo("ViewPager", ViewPagerMenuActivity.class));
            add(new MenuAdapter.MenuInfo("Fragment", FragmentMenuActivity.class));
            add(new MenuAdapter.MenuInfo("Navigation", FragmentMenuActivity.class));
            add(new MenuAdapter.MenuInfo("Dialog", DialogActivity.class));
            add(new MenuAdapter.MenuInfo("三方登录", LoginMenuActivity.class));

        }};
        recyclerView.setMenuList(menuList);

    }
}