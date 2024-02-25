package com.kokusz19.udinfopark.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

public class HttpRequestLoggerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestLoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info(
                "({})\t{}{}",
                request.getMethod(),
                request.getRequestURI(),
                Optional.ofNullable(request.getQueryString()).map(qString -> "?"+qString).orElse(""));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
