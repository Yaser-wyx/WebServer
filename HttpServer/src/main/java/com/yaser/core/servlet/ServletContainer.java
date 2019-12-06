package com.yaser.core.servlet;

import com.yaser.core.context.ServletContext;
import com.yaser.core.network.handler.Handler;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.impl.DefaultServlet;

/**
 * 每一次请求传入时，单个请求的Servlet运行时容器
 * 主要工作：
 * 1.根据不同的url请求调用不同的servlet
 * 2.存放每一次的请求和响应
 */
public class ServletContainer implements Runnable {
    //servlet的上下文环境
    private ServletContext servletContext;
    //要调用的servlet
    private Servlet servlet;
    //响应
    private HttpServletResponse response;
    //请求
    private HttpServletRequest request;
    //服务器处理程序
    private Handler serverHandler;

    public ServletContainer(ServletContext servletContext, HttpServletResponse response,
                            HttpServletRequest request, Handler serverHandler) {
        this.servletContext = servletContext;
        this.response = response;
        this.request = request;
        this.serverHandler = serverHandler;
        if (request != null) {
            this.servlet = servletContext.getServletByUrl(request.getServletPath());
        }
    }

    @Override
    public void run() {

        service();
    }

    private void service() {
        try {
            if (servlet != null) {
                servlet.service(request, response);
            } else {
                response.write();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.serverHandler.close();
        }
    }
}
