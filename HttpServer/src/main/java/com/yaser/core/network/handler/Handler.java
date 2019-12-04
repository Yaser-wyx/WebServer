package com.yaser.core.network.handler;


import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Handler {
    protected ExecutorService pool;
    protected Socket client;

    public Handler(Socket client) {
        this.client = client;
        pool = Executors.newCachedThreadPool();
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
