package com.example.myapplication.customdns;

import android.util.Log;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import okhttp3.Dns;

public class DefaultDns implements Dns {
    private String TAG = "DefaultDns";

    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String s) throws UnknownHostException {
        Log.i(TAG, "开始使用默认DNS服务器：" );
        List<InetAddress> lookup = Dns.SYSTEM.lookup(s);
        Log.i(TAG, "默认DNS服务器：" + lookup);
        return lookup;
    }
}
