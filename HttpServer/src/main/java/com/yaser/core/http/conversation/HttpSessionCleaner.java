package com.yaser.core.http.conversation;

import com.yaser.core.http.context.WebApplicationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//session过期检测及清除机制
public class HttpSessionCleaner implements Runnable {
    @Override
    public void run() {
        log.info("开始检测session");
        WebApplicationContext.getServletContext().detectSessionIsValid();
        log.info("检测结束");
    }
}
