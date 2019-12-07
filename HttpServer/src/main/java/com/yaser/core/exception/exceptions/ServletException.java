package com.yaser.core.exception.exceptions;

import com.yaser.core.enumeration.HttpStatus;
import lombok.Getter;

@Getter
public abstract class ServletException extends Exception {
    protected HttpStatus httpStatus;

    public ServletException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
