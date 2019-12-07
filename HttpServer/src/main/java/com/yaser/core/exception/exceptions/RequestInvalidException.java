package com.yaser.core.exception.exceptions;

import com.yaser.core.enumeration.HttpStatus;


public class RequestInvalidException extends ServletException {
    public RequestInvalidException() {
        super(HttpStatus.BAD_REQUEST);
    }
}
