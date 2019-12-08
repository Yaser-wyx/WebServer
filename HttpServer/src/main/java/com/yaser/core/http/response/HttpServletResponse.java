package com.yaser.core.http.response;

import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.enumeration.HttpStatus;
import com.yaser.core.http.conversation.Cookie;
import com.yaser.core.http.servlet.Container;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import static com.yaser.core.constant.CharContest.*;

@Slf4j
public class HttpServletResponse {
    @Setter
    private Container container;
    private BufferedOutputStream writer;
    private StringBuilder response;
    private ArrayList<Header> headers;//用户添加的头部信息

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String contentType;
    private String charset;
    private HttpStatus httpStatus;
    private byte[] body;
    private boolean hasBody = false;
    private ArrayList<Cookie> cookies;

    public HttpServletResponse(OutputStream out) {
        writer = new BufferedOutputStream(out);
        response = new StringBuilder();
        headers = new ArrayList<>();
        httpStatus = HttpStatus.OK;
        contentType = HTTPConstant.DEFAULT_CONTENT_TYPE;
        cookies = new ArrayList<>();
        charset = null;
    }

    //构造response的头部信息
    private void buildHeader() {
        //添加默认头部信息
        response.append("HTTP/1.1").append(BLANK).
                append(httpStatus.getCode()).append(BLANK).append(httpStatus).append(CRLF);
        response.append("Date:").append(BLANK).append(new Date()).append(CRLF);
        if (charset != null) {
            String type = contentType;
            int charSetIndex = contentType.indexOf("charset");
            if (charSetIndex != -1) {
                //存在charset，将原来的charset替换掉
                type = contentType.substring(0, charSetIndex);
            }
            if (type.contains(SEMICOLON)) {
                contentType = type + "charset=" + charset;
            } else {
                contentType = type + SEMICOLON + "charset=" + charset;
            }
        }
        response.append("Content-Type:").append(BLANK).append(contentType).append(CRLF);
        if (hasBody) {
            //如果有body的话，添加body体长度信息
            response.append("Content-Length:").append(BLANK).append(body.length).append(CRLF);
        }
        if (!cookies.isEmpty()) {
            //如果有cookie的话
            StringBuilder cookieStr = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieStr.append(cookie.toString()).append(BLANK);
            }
            response.append("Set-Cookie:").append(BLANK).append(cookieStr).append(CRLF);
        }
        for (Header header : headers) {//添加用户头部信息
            response.append(header.getKey()).append(":").append(BLANK).append(header.getVal()).append(CRLF);
        }
        response.append(CRLF);//添加空行
        log.info("header:{}", response);
    }


    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void addHeader(Header header) {
        this.headers.add(header);
    }


    public void write(String data) {
        //将用户传入的数据进行写回操作
        body = data.getBytes(StandardCharsets.UTF_8);
        hasBody = true;
        this.write();
    }

    public void write(byte[] data) {
        body = data;
        hasBody = true;
        this.write();
    }

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    public void write() {
        this.sendToClient();
    }

    //重定向
    public void sendRedirect(String url) {
        log.info("重定向至：{}", url);
        this.addHeader(new Header("Location", url));
        this.setHttpStatus(HttpStatus.MOVED_TEMPORARILY);
        this.sendToClient();
    }

    private void sendToClient() {
        //发送数据给客户端
        try {
            this.buildHeader();
            byte[] header = response.toString().getBytes(StandardCharsets.UTF_8);
            this.writer.write(header);
            if (hasBody) {
                this.writer.write(body);
            }
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("写回客户端失败！");
        }
    }

    public void setCharacterEncoding(String charset) {
        this.charset = charset;
    }
}
