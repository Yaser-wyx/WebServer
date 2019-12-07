package com.yaser.core.servlet;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;

import java.io.IOException;

public interface Servlet {
    void init();

    void destroy();

    void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
