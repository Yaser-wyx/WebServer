package com.yaser.core.request;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.exception.exceptions.ServletNotFoundException;
import com.yaser.core.response.HttpServletResponse;

import java.io.IOException;

public interface RequestDispatcher {
    void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
