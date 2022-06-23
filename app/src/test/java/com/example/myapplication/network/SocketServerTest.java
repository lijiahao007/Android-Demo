package com.example.myapplication.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerTest {

    public static void main(String[] args) {
        try {
            // 初始化服务端socket并且绑定9999端口
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("服务端启动成功");
            //等待客户端的连接
            Socket socket = serverSocket.accept();
            //获取输入流
            BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = bufferedReader.readLine();
            System.out.println("服务端收到的消息："+str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
