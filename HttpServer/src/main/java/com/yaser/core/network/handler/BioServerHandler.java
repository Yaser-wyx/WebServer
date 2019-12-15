package com.yaser.core.network.handler;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.request.HttpServletRequest;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.http.servlet.ServletContainer;
import com.yaser.core.network.server.Server;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

//服务器处理程序
@Slf4j
public class BioServerHandler extends Handler implements Runnable{
    public BioServerHandler(Server server) {
        super(server);
    }

    @Override
    public void run() {
        log.info("开始监听。。。");
        while (this.server.isRunning()) {
            HttpServletResponse response = null;
            try {
                this.client = this.server.getServerSocket().accept();
                //先初始化response再初始化request，因为在解析request的时候可能出现异常，避免出现异常后response却未初始化
                response = new HttpServletResponse(client.getOutputStream());
                //读取请求数据流，并解析
                HttpServletRequest request = new HttpServletRequest(client.getInputStream());
                //从线程池中分配线程进行执行
                pool.execute(new ServletContainer(servletContext, response, request, this));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("IO Exception");
            } catch (ServletException e) {
                this.exceptionHandler.handle(e, response, this);
            }
        }
    }
}
