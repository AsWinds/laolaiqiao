package com.codi.laolaiqiao.common.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.OncePerRequestFilter;

import com.codi.laolaiqiao.common.exception.BaseException;
import com.codi.laolaiqiao.common.util.SignUtil;

@Slf4j
public class RequestSignValidationFilter extends OncePerRequestFilter {

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		String method = request.getMethod();
		
		if ("POST".equals(method) && request.getParameterMap().size() != 0) {
	        String inSign = request.getParameter("sign");
	        if (inSign == null) {
	            throw new BaseException("0002");
            }
	        TreeMap<String, Object> parms = processParams(request.getParameterMap());
	        String requestStr = SignUtil.getRequestStr(parms); 
	        String expectSign = SignUtil.getRequestSign(requestStr);
	        log.debug("Verify Sign for:{}, expect Sign:{}", requestStr, expectSign);
	        if (!SignUtil.verifySignV1(expectSign, inSign)) {
	            log.error("Sign verify fail for request str : " + requestStr + ", sign : " + inSign);
	            throw new BaseException("0002");
            }
        }
        filterChain.doFilter(request, response); 
    }
	
	private TreeMap<String, Object> processParams(Map<String, String[]> origin){
		TreeMap<String, Object> res = new TreeMap<String, Object>();
		for (Map.Entry<String, String[]> entry : origin.entrySet()) {
			String key = entry.getKey();
			if ("sign".equals(key)) {
	            continue;
            }
	        String newVal = String.join(",", Arrays.asList(entry.getValue()));
	        res.put(entry.getKey(), newVal);
        }
		return res;
	}

}
