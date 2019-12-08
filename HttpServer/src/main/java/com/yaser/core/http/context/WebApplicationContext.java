package com.yaser.core.http.context;

public class WebApplicationContext {
    private static ServletContext servletContext;

    static {
        //静态持有servletContext
        servletContext = new ServletContext();
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }
}
