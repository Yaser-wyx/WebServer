package com.yaser.core.network.handler;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;

import java.io.*;
import java.net.Socket;

//客户端处理程序
public class BioServerHandler extends Handler implements Runnable {
    public BioServerHandler(Socket client) {
        super(client);
    }

    @Override
    public void run() {
        try {
            //读取输入流，进行解析
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            //读取请求的head部分，并解析

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            byte[] data = this.readRequest(in);
            if (data != null) {
                HttpServletRequest request = new HttpServletRequest(data);
            }

            HttpServletResponse response = new HttpServletResponse(out);
            // todo 重写response
            //开始对报文进行解析
            writer.write("HTTP/1.1 200 OK\n" +
                    "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                    "Content-Type: text/html;charset=UTF-8\n" +
                    "Connection: close\n" +
                    "Content-Length: 5\n" +
                    "\n" +
                    "nihao");
            writer.flush();
            System.out.println("=============finished=============");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
