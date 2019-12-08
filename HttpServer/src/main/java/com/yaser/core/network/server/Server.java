package com.yaser.core.network.server;

import com.yaser.core.http.context.ServletContext;
import com.yaser.core.http.context.WebApplicationContext;

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
            this.servletContext.destroyServlet();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}