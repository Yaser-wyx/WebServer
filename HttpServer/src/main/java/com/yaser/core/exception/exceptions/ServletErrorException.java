package com.yaser.core.exception.exceptions;

import com.yaser.core.enumeration.HttpStatus;

public class ServletErrorException extends ServletException {
    public ServletErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
