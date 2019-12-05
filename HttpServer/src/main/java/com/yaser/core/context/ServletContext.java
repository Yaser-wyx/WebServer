package com.yaser.core.context;

import com.yaser.core.servlet.ServletHolder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;

/**
 * Servlet上下文环境
 * 主要工作：
 * 1.服务器启动的时候读取servlet配置文件
 * 2.将所有的servlet信息数据静态保存
 */
public class ServletContext {
    /**
     * url -> servlet别名
     * 一个url对应一个servlet
     * 一个servlet可以有多个url对应
     */
    private HashMap<String, String> servletMapping;

    private HashMap<String, ServletHolder> servlets;

    public ServletContext() {
        //TODO 初始化servlet
    }

    private void init() {
        //读取web.xml配置文件，并加载servlet
        this.servletMapping = new HashMap<>();
        this.servlets = new HashMap<>();

    }

    private void parseConfig() {

       SAXReader reader  = new SAXReader();
        try {
            reader.read(ServletContext.class.getResourceAsStream("/web.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

//        File file = ;
//        Document document =
    }

    public static void main(String[] args) {
        ServletContext servletContext = new ServletContext();
        servletContext.parseConfig();
    }
}
