package com.example.myapplication.customdns;

import android.util.Log;

import androidx.annotation.NonNull;

import org.xbill.DNS.Address;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

public class CustomDns implements Dns {
    private String TAG = "CustomDns";
    private final String dnsServerIpAddress;

    public CustomDns(String dnsServerIpAddress) {
        this.dnsServerIpAddress = dnsServerIpAddress;
        // 设置自定义DNS服务器
    }

    @NonNull
    @Override
    public List<InetAddress> lookup(@NonNull String hostname) throws UnknownHostException {
        try {
            // 设置自定义DNS服务器
            Log.i(TAG, "开始使用" + dnsServerIpAddress + "解析的地址");
            SimpleResolver resolver = new SimpleResolver(dnsServerIpAddress);
            Lookup.setDefaultResolver(new ExtendedResolver(new SimpleResolver[]{resolver}));
            // 使用自定义DNS服务器解析主机名
            InetAddress[] addresses = Address.getAllByName(hostname);
            Log.i(TAG, "使用" + dnsServerIpAddress + "解析的地址：lookup : " + Arrays.toString(addresses));
            return Arrays.asList(addresses);
        } catch (Exception  e) {
            try {
                InetAddress[] addresses = Address.getAllByName(hostname);
                Log.i(TAG, "使用" + dnsServerIpAddress + "无法解析的地址：现使用默认DNS服务器进行解析 lookup : " + Arrays.toString(addresses));
                return Arrays.asList(addresses);
            } catch (UnknownHostException uhe) {
                UnknownHostException unknownHostException =
                        new UnknownHostException("Failed to resolve hostname using custom and default DNS servers: " + hostname);
                unknownHostException.initCause(uhe);
                throw unknownHostException;
            }
        }
    }
}
