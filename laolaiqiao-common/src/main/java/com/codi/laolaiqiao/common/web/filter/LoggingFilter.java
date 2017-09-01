package com.codi.laolaiqiao.common.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		ContentCachingRequestWrapper reqWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper respWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(reqWrapper, respWrapper);
        try {
	        String query = reqWrapper.getQueryString(), path = request.getRequestURI();
	        String reqBody = new String(reqWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
	        String respBody = new String(respWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
	        log.debug("Handle Request:{}, query:{}, req body:{}, resp body:{}", path, query, reqBody, respBody);
        } catch (Exception e) {
            log.error("Log Request Response body error", e);
        }
        respWrapper.copyBodyToResponse();
    }

}
