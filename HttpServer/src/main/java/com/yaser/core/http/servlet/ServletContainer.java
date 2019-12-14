package com.yaser.core.http.servlet;

import com.yaser.core.exception.exceptions.RequestInvalidException;
import com.yaser.core.exception.exceptions.RequestParseException;
import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.exception.exceptions.ServletNotFoundException;
import com.yaser.core.http.context.ServletContext;
import com.yaser.core.http.request.HttpServletRequest;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.network.handler.Handler;
import lombok.Getter;

import java.io.IOException;

/**
 * 每一次请求传入时，单个请求的运行时容器
 * 主要工作：
 * 1.根据不同的url请求调用不同的servlet
 * 2.存放每一次的请求和响应
 */
public class ServletContainer implements Runnable {

    //要调用的servlet
    private Servlet servlet;
    //响应
    @Getter
    private HttpServletResponse response;
    //请求
    @Getter
    private HttpServletRequest request;
    //服务器处理程序
    private Handler serverHandler;

    public ServletContainer(ServletContext servletContext, HttpServletResponse response, HttpServletRequest request, Handler serverHandler) throws ServletNotFoundException, IOException, RequestInvalidException, RequestParseException {
        this.response = response;
        this.request = request;
        this.request.setContainer(this);
        this.response.setContainer(this);
        this.serverHandler = serverHandler;
        this.servlet = servletContext.getServletByUrl(request.getServletPath());
    }

    @Override
    public void run() {
        try {
            servlet.service(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            this.serverHandler.getExceptionHandler().handle(e, response, serverHandler);
        } finally {
            this.serverHandler.close();
        }
    }
}
