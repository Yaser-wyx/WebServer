package com.yaser.core.exception.exceptions;

import com.yaser.core.enumeration.HttpStatus;

public class ServletNotFoundException extends ServletException {
    public ServletNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
