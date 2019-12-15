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
        stringBuilder.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<link rel=\"shortcut icon\" href=\"http://www.google.com/favicon.ico\" />\n"+
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>login success</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"main\">\n" +
                "    <div class=\"title\">计算机网络课程设计<br>HTTP Web服务器</div>\n" +
                "    <div class=\"login-box\">\n" +
                "        <div class=\"login-text\">登陆成功！</div>\n" +
                "        <div class=\"login-text\">欢迎：");
        stringBuilder.append(username);
        stringBuilder.append("</div>\n" +
                "        <div class=\"login-text\">登陆时间：").append(loginTime);
        stringBuilder.append("</div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div class=\"footer\">\n" +
                "    <div class=\"footer-text\">计算机网络课程设计@万宇轩 201733050050</div>\n" +
                "    <div class=\"footer-text\">南京信息工程大学</div>\n" +
                "</div>\n" +
                "</body>\n" +
                "<style>\n" +
                "    .footer {\n" +
                "        position: absolute;\n" +
                "        bottom: 0;\n" +
                "        margin-left: calc(50% - 250px);\n" +
                "        width: 500px;\n" +
                "        height: 50px;\n" +
                "        padding: 5px;\n" +
                "        text-align: center;\n" +
                "    }\n" +
                "\n" +
                "    .footer-text {\n" +
                "        color: #566573;\n" +
                "    }\n" +
                "\n" +
                "    body {\n" +
                "        background-image: url(\"./picture/background.jpg\");\n" +
                "    }\n" +
                "\n" +
                "    .title {\n" +
                "        margin-top: 5px;\n" +
                "        text-align: center;\n" +
                "        font-size: 30px;\n" +
                "        font-family: \"黑体\", serif;\n" +
                "        color: #34495E;\n" +
                "    }\n" +
                "\n" +
                "    .main {\n" +
                "        width: 450px;\n" +
                "        height: 300px;\n" +
                "        margin-left: auto;\n" +
                "        margin-right: auto;\n" +
                "        background-color: rgba(255, 255, 255, 0.9);\n" +
                "        padding: 10px;\n" +
                "        border-radius: 10px;\n" +
                "        margin-top: 150px;\n" +
                "        box-shadow: 0px 6px 6px -3px rgba(0, 0, 0, 0.2), 0px 10px 14px 1px rgba(0, 0, 0, 0.14), 0px 4px 18px 3px rgba(0, 0, 0, 0.12) !important;\n" +
                "    }\n" +
                "\n" +
                "    .login-box {\n" +
                "        margin-top: 10px;\n" +
                "        width: 400px;\n" +
                "        height: 100px;\n" +
                "        margin-left: auto;\n" +
                "        margin-right: auto;\n" +
                "    }\n" +
                "    .login-text {\n" +
                "        margin-top: 5px;\n" +
                "        font-size: 18px;\n" +
                "        font-family: \"黑体\", serif;\n" +
                "        text-align: center;\n" +
                "        color: #34495E;\n" +
                "        width: 300px;\n" +
                "        margin-left: auto;\n" +
                "        margin-right: auto;\n" +
                "    }\n" +
                "</style>\n" +
                "</html>");
        response.write(stringBuilder.toString());
    }
}
