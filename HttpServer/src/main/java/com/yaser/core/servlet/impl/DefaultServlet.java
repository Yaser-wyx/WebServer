package com.yaser.core.servlet.impl;

import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.response.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        log.info("响应请求！");
        response.setContentType("text/html; charset=utf-8");
        response.write("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<style>\n" +
                "\n" +
                "    .test{\n" +
                "        background: red;\n" +
                "        width: 100px;\n" +
                "        height: 100px;\n" +
                "    }\n" +
                "</style>\n" +
                "<body>\n" +
                "    <div class=\"test\">welcome!</div>\n" +
                "</body>\n" +
                "\n" +
                "\n" +
                "</html>");
    }
}
