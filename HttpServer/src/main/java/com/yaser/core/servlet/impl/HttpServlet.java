package com.yaser.core.servlet.impl;

import com.yaser.core.enumeration.RequestMethod;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.Servlet;

import java.io.IOException;

public abstract class HttpServlet implements Servlet {
    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod() == RequestMethod.GET) {
            doGet(request, response);
        } else if (request.getMethod() == RequestMethod.POST) {
            doPost(request, response);
        } else if (request.getMethod() == RequestMethod.PUT) {
            doPut(request, response);
        } else if (request.getMethod() == RequestMethod.DELETE) {
            doDelete(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) {

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {

    }
}
