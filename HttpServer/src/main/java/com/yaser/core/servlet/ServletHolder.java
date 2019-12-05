package com.yaser.core.servlet;

import lombok.Data;

@Data
public class ServletHolder {
    private String servletClass;
    private Servlet servlet;

    public ServletHolder(String servletClass) {
        this.servletClass = servletClass;
    }
}
