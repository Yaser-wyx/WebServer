package com.yaser.core.network.server;

import com.yaser.core.network.handler.BioServerHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class BioServer extends Server {
    public BioServer(int port) {
        super(port);
    }

    @Override
    public void start() {
        while (isRunning) {
            try {
                log.info("等待连接中。。。。");
                Socket client = this.serverSocket.accept();
                Thread server = new Thread(new BioServerHandler(client), "server");
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("与客户端连接失败。。");
            }
        }
    }
}
