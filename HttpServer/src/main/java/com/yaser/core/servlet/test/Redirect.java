package com.yaser.core.servlet.test;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.impl.HttpServlet;

import java.io.IOException;

public class Redirect extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.write("this is redirect!");
    }
}
