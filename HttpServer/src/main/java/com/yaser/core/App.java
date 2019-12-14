package com.yaser.core;

import com.yaser.core.network.server.Server;
import com.yaser.core.network.server.BioServer;

public class App {
    public static void main(String[] args) {
        Server server = new BioServer(8080);
        server.start();
    }
}
