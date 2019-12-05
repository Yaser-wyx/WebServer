package com.yaser.core.network.handler;

import com.yaser.core.context.ServletContext;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.ServletContainer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

//服务器处理程序
@Slf4j
public class BioServerHandler extends Handler implements Runnable {
    public BioServerHandler(Socket client, ServletContext servletContext) {
        super(client, servletContext);
    }

    @Override
    public void run() {
        try {
            //读取输入流，进行解析
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();
            //读取请求的head部分，并解析
            byte[] data = this.readRequest(in);
            HttpServletRequest request = null;
            if (data != null) {
                log.info("data不为空！");
                request = new HttpServletRequest(data);
            }
         /*   BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            writer.write("HTTP/1.1 200 OK\n" +
                    "Date: Sat, 31 Dec 2005 23:59:59 GMT\n" +
                    "Content-Type: text/html;charset=UTF-8\n" +
                    "Connection: close\n" +
                    "Content-Length: 5\n" +
                    "\n" +
                    "nihao");
            writer.flush();*/
            HttpServletResponse response = new HttpServletResponse(out);
            pool.execute(new ServletContainer(servletContext, response, request, this));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
