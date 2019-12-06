package com.yaser.core.context;

import com.yaser.core.servlet.Servlet;
import com.yaser.core.servlet.ServletHolder;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Servlet上下文环境
 * 主要工作：
 * 1.服务器启动的时候读取servlet配置文件
 * 2.将所有的servlet信息数据静态保存
 */
@Slf4j
public class ServletContext {
    /**
     * url -> servlet别名
     * 一个url对应一个servlet
     * 一个servlet可以有多个url对应
     */
    private HashMap<String, String> servletMapping;

    private HashMap<String, ServletHolder> servlets;

    public ServletContext() {
        init();
    }

    private void init() {
        //读取web.xml配置文件，并加载servlet
        this.servletMapping = new HashMap<>();
        this.servlets = new HashMap<>();
        parseConfig();
    }

    public Servlet getServletByUrl(String url) {
        log.info("要匹配的URL:{}",url);
        //根据url地址来匹配对应的servlet
        String servletName = servletMapping.get(url);
        ServletHolder servletHolder = servlets.get(servletName);
        Servlet servlet = servletHolder.getServlet();
        if (servlet == null) {
            servlet = initServlet(servletHolder.getServletClass());
            servletHolder.setServlet(servlet);
        }
        return servlet;
    }

    private Servlet initServlet(String ServletClassName) {
        try {
            Servlet servlet = (Servlet) Class.forName(ServletClassName).getDeclaredConstructor().newInstance();
            //调用前初始化
            servlet.init();
            return servlet;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("加载并初始化servlet失败");
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析web.xml的配置文件
     */
    private void parseConfig() {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(ServletContext.class.getResourceAsStream("/web.xml"));
            Element root = document.getRootElement();
            //保存servlet名字与servlet类名的映射
            Iterator<Element> servletList = root.elementIterator("servlet");
            while (servletList.hasNext()) {
                Element servlet = servletList.next();
                String key = servlet.elementText("servlet-name");
                String value = servlet.elementText("servlet-class");
                servlets.put(key.trim(), new ServletHolder(value));//保存servlet的类名
            }
            //保存servlet名字与url地址的映射
            Iterator<Element> servletMappingList = root.elementIterator("servlet-mapping");
            while (servletMappingList.hasNext()) {
                Element servletMap = servletMappingList.next();
                String key = servletMap.elementText("url-pattern");
                String value = servletMap.elementText("servlet-name");
                servletMapping.put(key, value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("加载servlet配置项失败");
        }
    }

    //销毁所有的Servlet
    public void destroyServlet() {
        servlets.values().forEach(servletHolder -> {
            Servlet servlet = servletHolder.getServlet();
            if (servlet != null) {
                servletHolder.getServlet().destroy();
            }
        });
    }

    public static void main(String[] args) {
        ServletContext servletContext = new ServletContext();
        log.info("servlets:{}", servletContext.servlets);
        log.info("servletMapping:{}", servletContext.servletMapping);
        Servlet servlet = servletContext.getServletByUrl("/login");

    }
}
