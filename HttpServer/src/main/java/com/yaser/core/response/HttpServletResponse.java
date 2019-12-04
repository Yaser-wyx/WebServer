package com.yaser.core.response;

import com.yaser.core.constant.CharContest;
import com.yaser.core.constant.HTTPConstant;
import com.yaser.core.enumeration.HttpStatus;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import static com.yaser.core.constant.CharContest.CRLF;
import static com.yaser.core.constant.CharContest.BLANK;


public class HttpServletResponse {
    BufferedWriter writer;
    private StringBuilder response;
    private ArrayList<Header> headers;
    private String contentType;
    private HttpStatus httpStatus;
    private byte[] body;

    public HttpServletResponse(OutputStream out) {
        writer = new BufferedWriter(new OutputStreamWriter(out));
        response = new StringBuilder();
        headers = new ArrayList<>();
        httpStatus = HttpStatus.OK;
        contentType = HTTPConstant.DEFAULT_CONTENT_TYPE;
    }

    //构造response的头部信息
    private void buildHeader() {
        //添加默认头部信息
        response.append("HTTP/1.1").append(BLANK).
                append(httpStatus.getCode()).append(httpStatus).append(CRLF);
        response.append("Data:").append(BLANK).append(new Date()).append(CRLF);
        response.append("Content-type:").append(BLANK).append(contentType).append(CRLF);
        if (body != null) {
            response.append("Content-Length:").append(BLANK).append(body.length).append(CRLF);
        }
        //todo 添加所有用户自己的头部信息

    }

    private void buildResponse() {
        this.buildHeader();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void addHeader(Header header) {
        this.headers.add(header);
    }

    public void write(String data) {
        //将用户传入的数据进行写回操作
        body = data.getBytes();

    }
}
