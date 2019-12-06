package com.yaser.core.servlet;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.impl.HttpServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class testServlet extends HttpServlet {
    @Override
    public void init() {
      log.info("hello!");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        response.write("登录！");
    }
}
