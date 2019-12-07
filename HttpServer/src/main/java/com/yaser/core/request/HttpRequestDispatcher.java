package com.yaser.core.request;

import com.yaser.core.context.WebApplicationContext;
import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.exception.exceptions.ServletNotFoundException;
import com.yaser.core.response.HttpServletResponse;
import com.yaser.core.servlet.Servlet;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class HttpRequestDispatcher implements RequestDispatcher {
    private String url;
    //转发请求
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Servlet servlet = WebApplicationContext.getServletContext().getServletByUrl(url);
        servlet.service(request,response);
    }
}
