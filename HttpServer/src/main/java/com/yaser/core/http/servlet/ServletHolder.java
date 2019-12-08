package com.yaser.core.http.servlet;

import lombok.Data;

@Data
public class ServletHolder {
    private String servletClass;
    private Servlet servlet;

    public ServletHolder(String servletClass) {
        this.servletClass = servletClass;
    }
}
