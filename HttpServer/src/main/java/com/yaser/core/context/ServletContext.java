package com.yaser.core.context;

import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.exception.exceptions.ServletNotFoundException;
import com.yaser.core.servlet.Servlet;
import com.yaser.core.servlet.ServletHolder;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.AntPathMatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


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
    //路径匹配器
    private AntPathMatcher matcher;

    private Servlet defaultServlet;

    public ServletContext() {
        init();
    }

    private void init() {
        //读取web.xml配置文件，并加载servlet
        this.servletMapping = new HashMap<>();
        this.servlets = new HashMap<>();
        this.matcher = new AntPathMatcher();
        parseConfig();
        //加载默认servlet
        defaultServlet = this.servlets.get(HTTPConstant.DEFAULT_SERVLET).getServlet();
    }

    public Servlet getServletByUrl(String url) throws ServletNotFoundException {
        log.info("要匹配的URL:{}", url);
        //根据url地址来进行精确匹配对应的servlet
        String servletName = servletMapping.get(url);
        if (servletName == null) {
            //根据url地址进行模糊匹配
            List<String> matchedPath = new ArrayList<>();//所有匹配的路径
            Set<String> needToMatchSet = servletMapping.keySet();//所有需要进行匹配的路径
            for (String needToMatchPath : needToMatchSet) {
                if (matcher.match(needToMatchPath, url)) {
                    matchedPath.add(needToMatchPath);
                }
            }
            if (!matchedPath.isEmpty()) {
                //如果非空，也就是说有路径匹配成功
                //对所有的路径进行排序处理，取出匹配度最高的
                Comparator<String> pathComparator = matcher.getPatternComparator(url);
                matchedPath.sort(pathComparator);
                servletName = servletMapping.get(matchedPath.get(0));//获取匹配度最高的
            } else {
                //说明没有一个符合要求的，使用默认的servlet
                return defaultServlet;
            }
        }
        ServletHolder servletHolder = servlets.get(servletName);
        if (servletHolder == null) {
            throw new ServletNotFoundException();
        }
        //根据匹配的servletName加载servlet
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


}
