package com.yaser.core.servlet.test;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.impl.HttpServlet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LoginServlet extends HttpServlet {
    @Override
    public void init() {
        log.info("hello!");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("into LoginServlet,and will forward to testServlet!");
        request.getRequestDispatcher("/forward").forward(request, response);
    }
}
