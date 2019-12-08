package com.yaser.core.http.conversation;

import lombok.Data;

import static com.yaser.core.constant.CharContest.*;

@Data
public class Cookie {
    private String name;
    private String value;
    private int maxAge = -1;
    private String domain;
    private String path;
    private boolean secure;

    public Cookie() {
    }

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("=").append(value).append(SEMICOLON);
        if (maxAge > -1) {
            stringBuilder.append(BLANK).append("Max-Age").append(EQUATION).append(maxAge).append(SEMICOLON);
        }
        if (path != null) {
            stringBuilder.append(BLANK).append("Path").append(EQUATION).append(path).append(SEMICOLON);
        }
        if (domain != null) {
            stringBuilder.append(BLANK).append("Domain").append(EQUATION).append(domain).append(SEMICOLON);
        }
        if (secure) {
            stringBuilder.append(BLANK).append("Secure").append(SEMICOLON);
        }

        return stringBuilder.toString();
    }
}
