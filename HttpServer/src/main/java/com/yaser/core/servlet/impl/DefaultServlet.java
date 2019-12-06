package com.yaser.core.servlet.impl;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        log.info("响应请求！");
        response.write("退出！");
    }
}
