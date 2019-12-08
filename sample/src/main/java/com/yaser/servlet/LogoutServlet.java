package com.yaser.servlet;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.conversation.HttpSession;
import com.yaser.core.http.request.HttpServletRequest;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.http.servlet.impl.HttpServlet;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("isLogin");
        session.removeAttribute("username");
        session.removeAttribute("loginTime");
        response.write("已退出！");
    }
}
