package com.yaser.core.exception;

import com.yaser.core.exception.exceptions.RequestInvalidException;
import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.network.handler.Handler;
import com.yaser.core.response.Header;
import com.yaser.core.response.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ExceptionHandler {
    public void handle(ServletException e, HttpServletResponse response, Handler serverHandler) {
        log.error("抛出异常:{}", e.getClass().getName());
        log.error("异常数据:{}", getErrorMessage(e));
        if (!(e instanceof RequestInvalidException)) {
            response.setHttpStatus(e.getHttpStatus());
            response.addHeader(new Header("Connection", "close"));
            response.write();
        }
        serverHandler.close();
    }

    public static String getErrorMessage(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.getBuffer().toString();
    }
}
