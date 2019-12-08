package com.yaser.core.http.request;

import com.yaser.core.exception.exceptions.ServletException;
import com.yaser.core.http.response.HttpServletResponse;

import java.io.IOException;

public interface RequestDispatcher {
    void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
