package com.yaser.core.network.server;

import com.yaser.core.http.context.ServletContext;
import com.yaser.core.http.context.WebApplicationContext;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;

//服务器抽象类
public abstract class Server {
    @Getter
    protected ServerSocket serverSocket;
    @Getter
    protected boolean isRunning = true;
    @Getter
    protected ServletContext servletContext;

    public Server(int port) {
        try {
            //创建一个服务器进行接受请求
            serverSocket = new ServerSocket(port);
            servletContext = WebApplicationContext.getServletContext();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server start failed!");
        }
    }

    public abstract void start();

    public void close() {
        try {
            this.isRunning = false;
            this.serverSocket.close();
            this.servletContext.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}