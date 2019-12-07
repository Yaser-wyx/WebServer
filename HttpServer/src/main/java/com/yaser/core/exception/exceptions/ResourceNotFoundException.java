package com.yaser.core.exception.exceptions;

import com.yaser.core.enumeration.HttpStatus;

public class ResourceNotFoundException extends ServletException {
    public ResourceNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
