package com.yaser.core.http.servlet;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.request.HttpServletRequest;
import com.yaser.core.http.response.HttpServletResponse;

import java.io.IOException;

public interface Servlet {
    void init();

    void destroy();

    void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
