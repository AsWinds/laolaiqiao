package com.codi.laolaiqiao.common.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.web.result.BaseResult;

@Slf4j
public class ExceptionTranslationFilter implements Filter  {
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
	    
    }

	@Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
	    try {
	        chain.doFilter(request, response);
        } catch (Exception e) {
	        if (e instanceof BaseException) {
	            BaseException be = (BaseException) e;
	            BaseResult result = new BaseResult(be.getCode(), be.getMsgArgs());
	            response.setContentType("application/json");
	            JSON.writeJSONString(response.getOutputStream(), StandardCharsets.UTF_8, result, SerializerFeature.UseSingleQuotes);
	            return;
            }
	        log.error("Unexpect exception", e);
        }
    }

	@Override
    public void destroy() {
	    
    }
	


}
