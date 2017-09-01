package com.laolaiqiao.web.security.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.alibaba.fastjson.JSON;
import com.codi.laolaiqiao.common.web.result.BaseResult;


public class LoginFailAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private String errorMsg;
	
	public LoginFailAuthenticationEntryPoint() {
	    BaseResult result = new BaseResult("1001");
	    this.errorMsg = JSON.toJSONString(result);
    }

	@Override
	public void commence(HttpServletRequest request,
	        HttpServletResponse response, AuthenticationException authException)
	        throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(errorMsg);
		response.flushBuffer();
	}

}
