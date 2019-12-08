package com.yaser.core.http.context;

import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.exception.exceptions.ServletNotFoundException;
import com.yaser.core.http.conversation.Cookie;
import com.yaser.core.http.conversation.HttpSession;
import com.yaser.core.http.conversation.HttpSessionCleaner;
import com.yaser.core.http.response.HttpServletResponse;
import com.yaser.core.http.servlet.Servlet;
import com.yaser.core.http.servlet.ServletHolder;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.AntPathMatcher;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.yaser.core.constant.CharContest.SESSION;


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

    private Map<String, HttpSession> sessionMap;

    private ScheduledExecutorService scheduledService;

    public ServletContext() {
        init();
    }

    private void init() {
        //读取web.xml配置文件，并加载servlet
        this.servletMapping = new HashMap<>();
        this.servlets = new HashMap<>();
        this.matcher = new AntPathMatcher();
        this.sessionMap = new ConcurrentHashMap<>();
        this.parseConfig();
        //加载默认servlet
        this.defaultServlet = this.servlets.get(HTTPConstant.DEFAULT_SERVLET).getServlet();
        this.scheduledService = Executors.newSingleThreadScheduledExecutor();
        this.scheduledService.scheduleAtFixedRate(new HttpSessionCleaner(), 0, 15, TimeUnit.SECONDS);
    }

    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
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

    private Servlet initServlet(String ServletClassName) throws ServletNotFoundException {
        try {
            Servlet servlet = (Servlet) Class.forName(ServletClassName).getDeclaredConstructor().newInstance();
            //调用前初始化
            servlet.init();
            return servlet;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("加载并初始化servlet失败");
            throw new ServletNotFoundException();
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

    //创建session
    public HttpSession createSession(HttpServletResponse response) {
        String sessionId = UUID.randomUUID().toString();
        HttpSession session = new HttpSession(sessionId);
        this.sessionMap.put(sessionId, session);
        response.addCookie(new Cookie(SESSION, sessionId));
        return session;
    }

    public void removeSession(String sessionId) {
        this.sessionMap.remove(sessionId);
    }

    public void detectSessionIsValid() {
        if (!sessionMap.isEmpty()) {
            Set<String> sessionKeySet = sessionMap.keySet();
            for (String key : sessionKeySet) {
                HttpSession session = sessionMap.get(key);
                //计算当前时间减去session的最大过期时间，该结果表示，如果该session是不过期的，那么它的活动时间应该在lastAccessedTime与now之间。
                Instant lastAccessedTime = Instant.now().minusSeconds(session.getMaxInactiveInterval());
                log.info("检测session，sessionID:{}",session.getId());
                if (lastAccessedTime.isAfter(session.getLastAccessed())) {
                    //如果session最后的活动时间在这之前，表示已经过期了
                    session.invalidate();
                }
            }
        } else {
            log.info("当前没有session");
        }
    }
}
