package com.example.myapplication.network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost",8080);
            System.out.println("客户端启动成功");
            //获取输出流
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("hello world");
            writer.flush();
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
