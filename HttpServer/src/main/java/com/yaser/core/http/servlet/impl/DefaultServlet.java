package com.yaser.core.http.servlet.impl;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.request.HttpServletRequest;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.resource.ResourceHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URLConnection;

@Slf4j
public class DefaultServlet extends HttpServlet {
    //抛出404异常
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //处理静态资源的servlet
        ResourceHandler resourceHandler = new ResourceHandler();
        byte[] resource = resourceHandler.getResourceByUrl(request.getServletPath());
        response.setContentType(URLConnection.guessContentTypeFromName(resourceHandler.getResourceName()));
        response.write(resource);
    }
}
