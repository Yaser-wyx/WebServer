package com.yaser.core.servlet.test;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.impl.HttpServlet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class testServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("have forward! next will redirect!");
        response.sendRedirect("/redirect");
    }
}
