package com.yaser.core.network.handler;


import com.yaser.core.exception.ExceptionHandler;
import com.yaser.core.http.context.ServletContext;
import com.yaser.core.network.server.Server;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class Handler {
    protected ExecutorService pool;
    protected Socket client;
    protected Server server;
    protected ServletContext servletContext;

    @Getter
    protected ExceptionHandler exceptionHandler;

    public Handler(Server server) {
        this.server = server;
        this.servletContext = server.getServletContext();
        exceptionHandler = new ExceptionHandler();
        //创建一个线程池
        pool = Executors.newFixedThreadPool(10);
    }

    public Socket getClient() {
        return client;
    }

    public abstract void start();

    public void close() {
        try {
            log.info("close socket");
            if (!client.isInputShutdown()) {
                this.client.shutdownInput();
            }
            if (!client.isOutputShutdown()) {
                this.client.shutdownOutput();
            }
            if (!client.isClosed()) {
                this.client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("关闭socket时出现异常!");
        }
    }
}
