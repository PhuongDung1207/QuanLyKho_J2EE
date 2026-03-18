package com.example.JavaQuanLyKho.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor that exposes the current request URI as a model attribute
 * named "currentUri" for use in Thymeleaf templates.
 *
 * This is required because Thymeleaf 3.1+ no longer exposes #request,
 * #session, #servletContext, and #response as template expression objects.
 */
public class RequestUriInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView != null) {
            modelAndView.addObject("currentUri", request.getRequestURI());
        }
    }
}
