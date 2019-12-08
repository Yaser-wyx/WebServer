package com.yaser.servlet;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.conversation.HttpSession;
import com.yaser.core.http.request.HttpServletRequest;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.http.servlet.impl.HttpServlet;

import java.io.IOException;
import java.time.Instant;

public class LoginServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("username");
        HttpSession session = request.getSession();
        if (session.getAttribute("isLogin") != null) {
            doGet(request, response);
        } else {
            session.setAttribute("username", name);
            session.setAttribute("loginTime", Instant.now().toString());
            session.setAttribute("isLogin", true);
            session.setMaxInactiveInterval(30);//设置30秒过期
            hasLogin(session, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if (isLogin == null) {
            response.setCharacterEncoding("UTF-8");
            //还没有登录，转发到login页面
            request.getRequestDispatcher("/login.html").forward(request, response);
        } else {
            //已经登陆了，显示用户登录信息
            hasLogin(session, response);
        }
    }

    private void hasLogin(HttpSession session, HttpServletResponse response) {

        StringBuilder stringBuilder = new StringBuilder();
        String username = (String) session.getAttribute("username");
        String loginTime = (String) session.getAttribute("loginTime");
        stringBuilder.append("userName: ").append(username).append("<br>");
        stringBuilder.append("loginTime: ").append(loginTime).append("<br>");
        response.write(stringBuilder.toString());
    }
}
