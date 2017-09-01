package com.laolaiqiao.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AppVersionInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AppVersionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appVersion = request.getHeader("Client-Version");
        String appId = request.getHeader("SysApp-Identifier");
        logger.info("Request from app-version: {}, os-type: {}, app-identifier: {}", appVersion, appId);
        //TODO
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
