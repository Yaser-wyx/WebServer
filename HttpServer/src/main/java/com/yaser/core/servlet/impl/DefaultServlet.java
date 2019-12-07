package com.yaser.core.servlet.impl;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.request.HttpServletRequest;
import com.yaser.core.resource.ResourceHandler;
import com.yaser.core.response.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jmimemagic.*;

@Slf4j
public class DefaultServlet extends HttpServlet {

    //抛出404异常
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //处理静态资源的servlet
        ResourceHandler resourceHandler = new ResourceHandler();
        byte[] resource = resourceHandler.getResourceByUrl(request.getServletPath());
        try {
            response.setContentType(Magic.getMagicMatch(resource).getMimeType());
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            e.printStackTrace();
        }
        response.write(resource);
    }
}
