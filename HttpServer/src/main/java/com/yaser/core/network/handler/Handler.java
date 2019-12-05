package com.yaser.core.network.handler;


import com.yaser.core.context.ServletContext;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class Handler {
    protected ExecutorService pool;
    protected Socket client;
    protected ServletContext servletContext;
    public Handler(Socket client,ServletContext servletContext) {
        this.client = client;
        this.servletContext = servletContext;
        pool = Executors.newFixedThreadPool(10);
    }

    public void close() {
        try {
            log.info("close socket");
            this.client.shutdownInput();
            this.client.shutdownOutput();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("关闭socket时出现异常!");
        }

    }

    //读取全部的输入流数据，并返回
    public byte[] readRequest(InputStream in) {
        BufferedInputStream bin = new BufferedInputStream(in);
        byte[] buf = null;
        try {
            buf = new byte[bin.available()];
            //将数据读取到缓冲区中
            int len = bin.read(buf);
            if (len <= 0) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }
}
