package com.yaser.core.exception;

import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.exception.exceptions.RequestInvalidException;
import com.yaser.core.exception.exceptions.ResourceNotFoundException;
import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.network.handler.Handler;
import com.yaser.core.resource.ResourceHandler;
import com.yaser.core.http.response.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
public class ExceptionHandler {
    public void handle(ServletException e, HttpServletResponse response, Handler serverHandler) {
        log.error("抛出异常:{}", e.getClass().getName());
        log.error("异常数据:{}", getErrorMessage(e));
        if (!(e instanceof RequestInvalidException)) {
            String url = "/errors/" + e.getHttpStatus().getCode() + ".html";
            ResourceHandler resourceHandler = new ResourceHandler();
            byte[] resource = new byte[0];
            try {
                resource = resourceHandler.getResourceByUrl(url);
            } catch (ResourceNotFoundException ex) {
                ex.printStackTrace();
            }
            response.setContentType(HTTPConstant.DEFAULT_CONTENT_TYPE);
            response.write(resource);
        }
        serverHandler.close();
    }

    public static String getErrorMessage(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.getBuffer().toString();
    }
}
