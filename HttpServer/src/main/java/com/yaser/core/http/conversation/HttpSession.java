package com.yaser.core.http.conversation;

import com.yaser.core.http.context.WebApplicationContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.yaser.core.constant.HTTPConstant.DEFAULT_MAX_INACTIVE_INTERVAL;

@Slf4j
public class HttpSession {
    @Getter
    private String id;//sessionId

    private Map<String, Object> attribute;//保存在session中的属性值
    @Getter
    private Instant lastAccessed;
    @Getter
    @Setter
    private int maxInactiveInterval;//最大过期时间，单位是秒
    //当前session是否合法，因为在session过期到被删除的期间，session还有可能被使用，所以为了防止被使用，使用该字段做标识符
    private boolean isValid;

    public HttpSession(String id) {
        this.id = id;
        this.attribute = new ConcurrentHashMap<>();
        this.lastAccessed = Instant.now();
        this.isValid = true;
        this.maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL;
    }

    public void setAttribute(String name, Object val) {
        if (this.isValid) {
            this.attribute.put(name, val);
            this.lastAccessed = Instant.now();
        } else {
            throw new IllegalStateException("session has invalidated");
        }
    }

    public Object getAttribute(String name) {
        if (this.isValid) {
            this.lastAccessed = Instant.now();
            return this.attribute.get(name);
        }
        throw new IllegalStateException("session has invalidated");
    }

    public void invalidate() {
        this.isValid = false;
        this.attribute.clear();
        log.info("当前session已过期，sessionID:{}", id);
        WebApplicationContext.getServletContext().removeSession(this.id);
    }

    public void removeAttribute(String name) {
        if (this.isValid) {
            this.attribute.remove(name);
            this.lastAccessed = Instant.now();
        } else {
            throw new IllegalStateException("session has invalidated");
        }
    }
}
