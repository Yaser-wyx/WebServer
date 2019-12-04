package com.yaser.core.network.server;

import java.io.IOException;
import java.net.ServerSocket;

//服务器抽象类
public abstract class Server {
    protected ServerSocket serverSocket;
    protected boolean isRunning = true;

    public Server(int port) {
        try {
            //创建一个服务器进行接受请求
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server start failed!");
        }
    }
    //TODO 添加加载Servlet
    public abstract void start();

    public void close() {
        //关闭服务器
        try {
            this.isRunning = false;
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}