package com.yaser.core.http.request;

import com.yaser.core.constant.CharContest;
import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.enumeration.RequestMethod;
import com.yaser.core.exception.exceptions.RequestInvalidException;
import com.yaser.core.exception.exceptions.RequestParseException;
import com.yaser.core.http.context.WebApplicationContext;
import com.yaser.core.http.conversation.Cookie;
import com.yaser.core.http.conversation.HttpSession;
import com.yaser.core.http.servlet.ServletContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import static com.yaser.core.constant.CharContest.*;

@Slf4j
public class HttpServletRequest {
    @Setter
    private ServletContainer container;
    @Getter
    private RequestMethod method = null;//请求方式

    private String url = "";//请求路径信息

    private String forwardUrl;

    private boolean forward;
    private HashMap<String, String> params = new HashMap<>();//请求参数信息

    private HashMap<String, String> headers = new HashMap<>();//请求头部信息

    private HashMap<String, Cookie> cookieMap = new HashMap<>();//cookie
    @Getter
    private String body = "";//body体信息

    private HashMap<String, Object> attributes = new HashMap<>();//用户属性

    private HttpSession session;//用户会话

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean createIfNone) {
        if (this.session != null) {
            //如果Session已经有了，则直接返回
            return this.session;
        }
        if (cookieMap.containsKey(SESSION)) {
            //判断Cookie中是否存在Session ID
            Cookie cookie = cookieMap.get(SESSION);
            String sessionId = cookie.getValue();//获取sessionId
            HttpSession currentSession = WebApplicationContext.getServletContext().
                    getSession(sessionId);//使用ID获取Session实例
            if (currentSession != null) {
                this.session = currentSession;
                return this.session;
            }
        }
        if (createIfNone) {
            //如果不存在，则创建
            this.session = WebApplicationContext.getServletContext().createSession(this.container.getResponse());
        }
        return this.session;
    }

    public HttpServletRequest() {
        method = RequestMethod.GET;
    }

    public HttpServletRequest(InputStream in) throws RequestInvalidException, RequestParseException {
        this.parseRequest(readRequest(in));
    }

    //读取全部的输入流数据，并返回
    public byte[] readRequest(InputStream in) throws RequestInvalidException {
        BufferedInputStream bin = new BufferedInputStream(in);
        byte[] buf = null;
        try {
            buf = new byte[bin.available()];
            //将数据读取到缓冲区中
            int len = bin.read(buf);
            if (len <= 0) {
                throw new RequestInvalidException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    public String getHeader(String key) {
        return headers.get(key.toLowerCase());
    }

    public String getServletPath() {
        //如果进行转发，则返回转发的地址，否则返回浏览器请求的地址
        if (this.forward) {
            return this.forwardUrl;
        }
        return this.url;
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key.toLowerCase());
    }

    public void setAttribute(String key, Object val) {
        this.attributes.put(key, val);
    }

    //解析body体数据
    private void parseBody(String body) throws RequestParseException {
        //将body数据读取出来
        int len = Math.min(body.length(), Integer.parseInt(this.getHeader("content-length")));
        this.body = body.substring(0, len);
        String contentType = getHeader("content-type");
        if (contentType.equals(HTTPConstant.TYPE_FORM) || contentType.equals(HTTPConstant.TYPE_TEXT)) {
            //表单格式，则直接调用表单解析
            this.params.putAll(this.parseParams(this.body));
        }
        log.info("body数据：{}", this.body);
    }

    //解析头部信息
    private void parseHeader(String[] lines) throws RequestParseException {
        int lineNum = 0;
        //先解析第一行数据
        this.analysis(lines[lineNum++]);
        //解析head的其它行
        for (; lineNum < lines.length; lineNum++) {
            if (lines[lineNum].length() == 0) {
                //如果是空行直接退出
                break;
            }
            //提取请求报文的每一行属性值
            int colonIndex = lines[lineNum].indexOf(':');
            if (colonIndex == -1) {
                throw new RequestParseException();
            }
            String key = lines[lineNum].substring(0, colonIndex).toLowerCase().trim();
            String value = lines[lineNum].substring(colonIndex + 1).trim();
            this.headers.put(key, value);
        }
        log.info("headers:{}", this.headers);
        if (this.headers.containsKey("cookie")) {
            //如果存在cookie，则对cookie进行解析操作
            String cookies = this.headers.get("cookie");
            //使用分号进行分割
            String[] cookieList = cookies.split(SEMICOLON);
            for (String rawCookie : cookieList) {
                //遍历键值对
                String[] cookieNameVal = rawCookie.trim().split(EQUATION);
                //按照等号分割
                if (cookieNameVal.length < 2) {
                    //不合法，则抛弃该cookie
                    continue;
                }
                //将cookie进行实例化
                Cookie cookie = new Cookie(cookieNameVal[0], cookieNameVal[1]);
                cookieMap.put(cookieNameVal[0], cookie);
            }
        }
    }

    private void parseRequest(byte[] data) throws RequestInvalidException, RequestParseException {
        String mergedData = new String(data, StandardCharsets.UTF_8);//使用UTF-8进行编码
        //将数据进行解析
        String[] lines = URLDecoder.decode(mergedData, StandardCharsets.UTF_8).split(CharContest.CRLF);
        if (lines.length <= 1) {
            throw new RequestInvalidException();
        }
        log.info("Request 数据读取完毕");
        log.info("行数：{}", lines.length);
        log.info("数据：{}", Arrays.toString(lines));
        log.info("============================");
        this.parseHeader(lines);
        String len = this.getHeader("content-length");
        if (len != null && Integer.parseInt(len.trim()) != 0) {
            log.info("要解析body");
            parseBody(lines[lines.length - 1]);
        } else {
            log.info("不要解析body");
        }
    }


    //解析提交方式，路径以及协议
    private void analysis(String line) throws RequestParseException {
        //将首行按照空格进行分割
        String[] firstLine = line.split(CharContest.BLANK);
        //判断请求方式
        this.method = RequestMethod.valueOf(firstLine[0].toUpperCase());
        log.info("method:{}", this.method);
        //解析路径
        String pathAndParams = firstLine[1];
        int start = pathAndParams.indexOf("?");
        if (start != -1) {
            //包含参数信息
            this.url = pathAndParams.substring(0, start);
            log.info("path:{}", this.url);
            String params = pathAndParams.substring(start + 1);
            //解析url参数
            this.params = this.parseParams(params);
            log.info("params:{}", this.params);
        } else {
            this.url = pathAndParams;
        }
    }

    //解析参数信息
    private HashMap<String, String> parseParams(String params) throws RequestParseException {
        if (params == null) {
            return new HashMap<>();
        }
        String[] paramList = params.split("&");//将参数进行拆分
        HashMap<String, String> paramsMap = new HashMap<>();
        for (String param : paramList) {
            String[] oneParam = param.split("=");
            if (oneParam.length != 2) {
                throw new RequestParseException();
            }
            paramsMap.put(oneParam[0].trim().toLowerCase(), oneParam[1]);
        }
        return paramsMap;
    }

    public RequestDispatcher getRequestDispatcher(String url) {
        return new HttpRequestDispatcher(url);
    }

    public String getParameter(String key) {
        return params.get(key.toLowerCase());
    }

    public Cookie getCookie(String name) {
        return cookieMap.get(name);
    }

    public void setForward(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        this.forward = true;
    }
}
