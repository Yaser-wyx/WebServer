package com.yaser.core.network.server;

import com.yaser.core.context.ServletContext;

import java.io.IOException;
import java.net.ServerSocket;

//服务器抽象类
public abstract class Server {
    protected ServerSocket serverSocket;
    protected boolean isRunning = true;
    protected ServletContext servletContext;

    public Server(int port) {
        try {
            //创建一个服务器进行接受请求
            serverSocket = new ServerSocket(port);
            servletContext = new ServletContext();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server start failed!");
        }
    }

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