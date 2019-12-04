package com.yaser.core.servlet;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;

public interface Servlet {
    void init();

    void destroy();

    void service(HttpServletRequest request, HttpServletResponse response);
}
