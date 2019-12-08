package com.yaser.core.http.request;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.context.WebApplicationContext;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.http.servlet.Servlet;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class HttpRequestDispatcher implements RequestDispatcher {
    private String url;
    //转发请求
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Servlet servlet = WebApplicationContext.getServletContext().getServletByUrl(url);
        request.setForward(url);
        servlet.service(request, response);
    }
}
