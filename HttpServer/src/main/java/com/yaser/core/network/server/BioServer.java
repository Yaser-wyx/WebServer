package com.yaser.core.network.server;

import com.yaser.core.network.handler.BioServerHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
//服务器
public class BioServer extends Server {
    public BioServer(int port) {
        super(port);
    }

    @Override
    public void start() {
        //创建服务器处理程序对象
        BioServerHandler bioServerHandler = new BioServerHandler(this);
        Thread thread = new Thread(bioServerHandler);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        String cmd;
        while (scanner.hasNext()) {
            cmd = scanner.nextLine();
            if (cmd.equals("EXIT")) {
                bioServerHandler.close();
                this.close();
                break;
            }
        }
    }
}
