package com.yaser.core;

import com.yaser.core.network.server.Server;
import com.yaser.core.network.server.BioServer;

public class App {
    public static void main(String[] args) {
        //todo 使用文件读取配置文件设置端口
        Server server = new BioServer(8081);
        server.start();
    }
}
