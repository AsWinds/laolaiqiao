package com.laolaiqiao.sys.web.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import com.codi.superman.base.common.Const;
import com.codi.superman.base.domain.SysUser;

@Slf4j
public class LoginUserDetectFilter implements Filter {

	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        log.debug("sessionId={}", session.getId());
        SysUser sysUser = (SysUser) session.getAttribute(Const.SESSION_LOGIN_USER);
        SysUserUtil.setUser(sysUser);
        try {
	        chain.doFilter(request, response);
        }finally{
        	SysUserUtil.clear();
        }
    }

	@Override
    public void destroy() {
	    // TODO Auto-generated method stub
	    
    }

}
