package com.yaser.core.servlet.test;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.impl.HttpServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogoutServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        response.write("退出！");
    }
}
