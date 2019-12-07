package com.yaser.core.request;

import com.yaser.core.enumeration.RequestMethod;
import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.constant.CharContest;
import com.yaser.core.exception.exceptions.RequestInvalidException;
import com.yaser.core.exception.exceptions.RequestParseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

@Slf4j

//用于解析请求报文
public class HttpServletRequest {
    @Getter
    private RequestMethod method = null;//请求方式

    private String url = "";//请求路径信息

    private HashMap<String, String> params = new HashMap<>();//请求参数信息

    private HashMap<String, String> headers = new HashMap<>();//请求头部信息

    @Getter
    private String body = "";//body体信息

    private HashMap<String, Object> attributes = new HashMap<>();//用户属性

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
        //TODO 解析cookie
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

    public String getParameter(String key) {
        return params.get(key.toLowerCase());
    }
}
