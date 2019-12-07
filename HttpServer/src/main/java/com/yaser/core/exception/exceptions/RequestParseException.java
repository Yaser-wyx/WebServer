package com.yaser.core.exception.exceptions;

import com.yaser.core.enumeration.HttpStatus;

public class RequestParseException extends ServletException {
    public RequestParseException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
